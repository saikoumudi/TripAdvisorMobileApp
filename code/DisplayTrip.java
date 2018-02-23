package com.example.homework9_parta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class DisplayTrip extends AppCompatActivity implements OnMapReadyCallback,AsynGeooder.GetAddress,DownloadTask.routeListener,LocationListener {


    static String ARGS_TRIP="trip";
    Trip trip;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    ArrayList<String> participants=new ArrayList<String>();
    ArrayList<String> participantNames=new ArrayList<String>();
    ListView participantListview,placesDisplay;
    LocationListener mLocationListener;
    FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
    GoogleMap map=null;
    LatLng currentLocation;
    ProgressDialog pd=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trip);
        trip= (Trip) getIntent().getExtras().getSerializable(ARGS_TRIP);
        participants=trip.getMembers();
        participantListview= (ListView) findViewById(R.id.participants);
        placesDisplay= (ListView) findViewById(R.id.placesContainer);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Demochange", location.getLatitude() + " " + location.getLongitude());



                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        getCurrentLocation();
        TextView title= (TextView) findViewById(R.id.titleTrip);
        title.setText(trip.getTitle());
        findViewById(R.id.searchPlace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText placeToSearch= (EditText) findViewById(R.id.placeToSearch);
                if(placeToSearch.getText().toString().isEmpty()){
                    Toast.makeText(DisplayTrip.this,"Please enter place to search",Toast.LENGTH_LONG).show();

                }
                else{
                    pd=new ProgressDialog(DisplayTrip.this);
                    pd.setMessage("Searching Place");
                    pd.setCancelable(false);
                    pd.show();
                    AsynGeooder task=new AsynGeooder(DisplayTrip.this);
                    task.execute(placeToSearch.getText().toString().trim());
                    placeToSearch.getText().clear();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.child("Trips").child(trip.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             trip=dataSnapshot.getValue(Trip.class);
              reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      participantNames.clear();
                      Log.d("demo","participant names adding started");
                      for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                          User temp=snapshot.getValue(User.class);
                          if(temp.getUid().equals(fu.getUid())){
                              participantNames.add("You");
                          }
                         else if(trip.getMembers().contains(temp.getUid())){
                           participantNames.add(temp.getFname()+" "+temp.getLname());
                          }


                      }
                      Log.d("demo","names participants"+participantNames);
                      //set new adapter for participants adapter
                      ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(DisplayTrip.this,android.R.layout.simple_list_item_1,participantNames);
                      participantListview.setAdapter(arrayAdapter);
                      plotMarks();
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
                //setting adapter for addresses;
                PlacesDisplayAdapter adapt=new PlacesDisplayAdapter(DisplayTrip.this,R.layout.tripaddchild,trip.getLocationAddresses(),trip);
                placesDisplay.setAdapter(adapt);
             }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getCurrentLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Log.d("demo","current location fetching");
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLocation=new LatLng(location.getLatitude(),location.getLongitude());


        }
       catch(Exception e){
                e.printStackTrace();
            }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map=googleMap;

        map.getUiSettings().setZoomControlsEnabled(true);
        plotMarks();
    }

    @Override
    public void Get(final Address address) {
        pd.dismiss();
      if(address!=null){
          AlertDialog.Builder builder=new AlertDialog.Builder(DisplayTrip.this);
          builder.setTitle("Add Place to Trip")
                  .setMessage("Place found :"+address.getLocality()+" "+address.getCountryName())
                  .setPositiveButton("Add Place to Trip", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          ArrayList<LocationAddress> addresses=trip.getLocationAddresses();
                          LocationAddress address1=new LocationAddress();
                          address1.setLocationLat(address.getLatitude());
                          address1.setLocationLong(address.getLongitude());
                          address1.setAdddressString(address.getLocality());
                          addresses.add(address1);
                          trip.setLocationAddresses(addresses);
                          reference.child("Trips").child(trip.getKey()).setValue(trip);
                          Toast.makeText(DisplayTrip.this,"New Place Added Succesfully",Toast.LENGTH_LONG).show();
                      }
                  })
                  .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });
          builder.create().show();
      }
      else{
          Toast.makeText(DisplayTrip.this,"Place not found..!!Please try another",Toast.LENGTH_LONG).show();      }
    }

    @Override
    public void sendRoute(PolylineOptions lineOptions) {
        pd.dismiss();
        if(map!=null&&lineOptions!=null){
           map.addPolyline(lineOptions);
            List<LatLng> points=lineOptions.getPoints();
            LatLngBounds.Builder b   = new LatLngBounds.Builder();


            b.include(currentLocation);
            for(LocationAddress address:trip.getLocationAddresses()) {
                LatLng temp=new LatLng(address.getLocationLat(),address.getLocationLong());
                map.addMarker(new MarkerOptions().position(temp));
                b.include(temp);
            }
            LatLngBounds bounds = b.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,10);
            map.moveCamera(cu);


        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadRoundTrip:
                String url= DownloadTask.getDirectionsUrl(currentLocation,currentLocation,trip.getLocationAddresses());
                if (pd != null) {
                    pd.dismiss();
                }
                pd=new ProgressDialog(DisplayTrip.this);
                pd.setMessage("Loading round trip route");
                pd.setCancelable(false);
                pd.show();
                DownloadTask downloadTask=new DownloadTask(DisplayTrip.this);
                downloadTask.execute(url);
                break;
            case R.id.loadMAp:
                String mapUrl = "https://maps.google.com/maps?saddr=San+Francisco,+CA&daddr=Los+Angeles,+CA+to:Phoenix,+AZ+to:Houston,+TX+to:Jacksonville,+FL+to:New+York,+NY+to:Buffalo,+NY+to:Chicago,+IL+to:Seattle,+WA+to:San+Jose,+CA";
                String mapsurl="https://maps.google.com/maps?saddr="+currentLocation.latitude+","+currentLocation.longitude+"&daddr=";
                ArrayList<LocationAddress> addresses=trip.getLocationAddresses();
                mapsurl+=addresses.get(0).getLocationLat()+","+addresses.get(0).getLocationLong();
                for(int j=1;j<addresses.size();j++){
                    mapsurl+="+to:"+addresses.get(j).getLocationLat()+","+addresses.get(j).getLocationLong();
                }
                mapsurl+="+to:"+currentLocation.latitude+","+currentLocation.longitude;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsurl));
                startActivity(i);
                break;
        }
        return true;
    }
    public void plotMarks(){
        map.clear();
        map.addMarker(new MarkerOptions().position(currentLocation));
        LatLngBounds.Builder b   = new LatLngBounds.Builder();
        b.include(currentLocation);
        for(LocationAddress address:trip.getLocationAddresses()) {
            LatLng temp=new LatLng(address.getLocationLat(),address.getLocationLong());
            map.addMarker(new MarkerOptions().position(temp));
            b.include(temp);
        }
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(b.build(),200,200,5);
        map.moveCamera(cu);
       // map.moveCamera(CameraUpdateFactory.zoomBy(0f));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_displaytrip, menu);
        return true;
    }
}
