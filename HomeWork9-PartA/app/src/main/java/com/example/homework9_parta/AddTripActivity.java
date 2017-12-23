package com.example.homework9_parta;

import android.content.DialogInterface;
import android.location.Address;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity implements OnMapReadyCallback,AsynGeooder.GetAddress,AddTripParticipant.removebuttonclick{
    GoogleMap map=null;
    Address address;
    ArrayList<User> friends;
    ArrayList<User> addedFriends;
    ListView listView;
    static String PARAM_FRIENDS="friends";
    static String PARAM_CURRENT_USER="user";
    FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        addedFriends=new ArrayList<User>();
        friends= (ArrayList<User>) getIntent().getExtras().getSerializable(PARAM_FRIENDS);
        Log.d("demo","friends recieved"+friends);
        Log.d("demo","friends recieved count"+friends.size());
        listView= (ListView) findViewById(R.id.particpants);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AddTripParticipant participantAdapter=new AddTripParticipant(AddTripActivity.this,R.layout.tripaddchild,addedFriends,AddTripActivity.this);
        listView.setAdapter(participantAdapter);

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText city= (EditText) findViewById(R.id.editText2);
                if(city.getText().toString().isEmpty()){
                    Toast.makeText(AddTripActivity.this,"Please enter location name ",Toast.LENGTH_LONG).show();
                }
                else{
                    new AsynGeooder(AddTripActivity.this).execute(city.getText().toString().trim());
                }
            }
        });
        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<User> toDisplay= (ArrayList<User>) friends.clone();
                for(User user:addedFriends){
                    toDisplay.remove(user);
                }
                Log.d("demo","adding to alert dialog");
                Log.d("demo","added friends count"+addedFriends.size());
                Log.d("demo","friends count "+friends.size()+" ");
                Log.d("demo","display count"+toDisplay.size());
                ArrayList<String> names=new ArrayList<String>();
                for (User user:toDisplay){
                    names.add(user.getFname()+" "+user.getLname());
                }
                final ArrayList<User> selectedParticipants=new ArrayList<User>();
                AlertDialog.Builder builder=new AlertDialog.Builder(AddTripActivity.this);
                builder.setTitle("Add Participants")
                        .setMultiChoiceItems(names.toArray(new CharSequence[toDisplay.size()]), null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    selectedParticipants.add(toDisplay.get(which));

                                }
                                else{
                                    selectedParticipants.remove(toDisplay.get(which));
                                }
                            }
                        })
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               addedFriends.addAll(selectedParticipants);
                                AddTripParticipant adapter= new AddTripParticipant(AddTripActivity.this,R.layout.tripaddchild,addedFriends,AddTripActivity.this);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                  builder.create().show();
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text= (EditText) findViewById(R.id.editText);

                if(address==null){
                    Toast.makeText(AddTripActivity.this,"Please enter location",Toast.LENGTH_LONG).show();

                }
                else if(text.getText().toString().isEmpty()){
                    Toast.makeText(AddTripActivity.this,"Please enter title",Toast.LENGTH_LONG).show();
                }
                else{
                    Trip trip=new Trip();
                    trip.setTitle(text.getText().toString().trim());
                    trip.setOwner(fu.getUid());
                    ArrayList<String> participants=new ArrayList<String>();
                    for(User use:addedFriends){
                        participants.add(use.getUid());
                    }
                    participants.add(fu.getUid());
                    trip.setMembers(participants);
                    LocationAddress locationAddress=new LocationAddress();
                    locationAddress.setLocationLat(address.getLatitude());
                    locationAddress.setLocationLong(address.getLongitude());
                    locationAddress.setAdddressString(address.getLocality());
                    ArrayList<LocationAddress> addresses=new ArrayList<LocationAddress>();
                    addresses.add(locationAddress);
                    Log.d("demo","addresses"+addresses.toString());
                    trip.setLocationAddresses(addresses);
                    trip.setKey(reference.child("Trips").push().getKey());
                    reference.child("Trips").child(trip.getKey()).setValue(trip);
                    Toast.makeText(AddTripActivity.this,"trip added succesfully",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void Get(Address address) {
        this.address=address;
        if(map!=null&&address!=null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()),10f));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    map=googleMap;
        if(address!=null&&map!=null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 20f));
        }
    }

    @Override
    public void removeParticiipant(User user) {

        AddTripParticipant adapter= (AddTripParticipant) listView.getAdapter();
        if(adapter!=null){
            addedFriends.remove(user);
            AddTripParticipant adapter2= new AddTripParticipant(AddTripActivity.this,R.layout.tripaddchild,addedFriends,AddTripActivity.this);
            listView.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void joinTrip(Trip trip) {

    }

    @Override
    public void viewTrip(Trip trip) {

    }

}
