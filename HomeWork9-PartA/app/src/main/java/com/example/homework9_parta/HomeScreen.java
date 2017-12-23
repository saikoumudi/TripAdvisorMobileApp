package com.example.homework9_parta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ArrayList<User> friends=new ArrayList<>();
    TextView hiUser;
    Button trips,discover;
    User currentUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fu = mAuth.getCurrentUser();
    DatabaseReference muid=databaseReference.child("Users").child(fu.getUid());
    ImageView profilePhoto;
    String gender;
    public static final String CURRENT_USER="USER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Welcome");

    }

    @Override
    protected void onResume() {
        super.onResume();
        hiUser = (TextView) findViewById(R.id.textViewHI);
        trips = (Button) findViewById(R.id.buttonTrips);
        discover = (Button) findViewById(R.id.buttonDiscoverPoeple);
        profilePhoto = (ImageView) findViewById(R.id.addPhoto);
        final ProgressDialog progressDialog=new ProgressDialog(HomeScreen.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        muid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                currentUser=dataSnapshot.getValue(User.class);
                hiUser.setText("HI " +currentUser.getFname()+" "+currentUser.getLname());
                if(currentUser.getPhotoURL()!=null){
                    Picasso.with(HomeScreen.this).load(currentUser.getPhotoURL()).into(profilePhoto);
                }
                else{
                    if(currentUser.getGender().equals("Male")){
                        profilePhoto.setImageResource(R.drawable.men);
                    }
                    else{
                        profilePhoto.setImageResource(R.drawable.female);
                    }
                }
                databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("demo","current users"+currentUser);
                        if(currentUser.getFriends()!=null&&!currentUser.getFriends().isEmpty()) {
                            friends.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                User temp = snapshot.getValue(User.class);
                                if(currentUser.getFriends().contains(temp.getUid())) {
                                    friends.add(temp);
                                }
                            }

                        }
                        Log.d("demo","friends "+friends);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Log.d("demo","email "+fu.getEmail()+" display name "+fu.getDisplayName());



        trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this,TripsActivity.class);
                intent.putExtra(AddTripActivity.PARAM_FRIENDS,friends);
                 intent.putExtra(AddTripActivity.PARAM_CURRENT_USER,currentUser);
                startActivity(intent);

            }
        });
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this,DiscoverPpl.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent=new Intent(HomeScreen.this,Settings.class);
                intent.putExtra(CURRENT_USER,currentUser);
                startActivity(intent);
                break;
            case R.id.logout:
                mAuth.signOut();
                finish();
                break;
        }
        return true;
        }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }





}
