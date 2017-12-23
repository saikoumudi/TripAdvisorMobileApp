package com.example.homework9_parta;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TripsActivity extends AppCompatActivity implements AddTripParticipant.removebuttonclick{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fu = mAuth.getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    ArrayList<User> friends;
    ArrayList<Trip> trips=new ArrayList<Trip>();
    User currentUser;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        FloatingActionButton button= (FloatingActionButton) findViewById(R.id.addTrip);
        friends= (ArrayList<User>) getIntent().getExtras().getSerializable(AddTripActivity.PARAM_FRIENDS);
        currentUser= (User) getIntent().getExtras().getSerializable(AddTripActivity.PARAM_CURRENT_USER);
        reference.child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trips.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d("demo",""+snapshot.getValue());
                    Trip temp=snapshot.getValue(Trip.class);
                    if(temp.getOwner().equals(fu.getUid())||temp.getMembers().contains(fu.getUid())){
                        trips.add(temp);
                    }
                    else if(currentUser.getFriends()!=null&&currentUser.getFriends().contains(temp.getOwner())){
                        trips.add(temp);
                    }

                }
                Collections.sort(trips, new Comparator<Trip>() {
                    @Override
                    public int compare(Trip o1, Trip o2) {
                        if(o1.getMembers().contains(currentUser.getUid())||o1.getOwner().equals(currentUser.getUid())){
                            return 0;
                        }
                        return 1;
                    }
                });
                //get listview adapter and update
                ListView listView= (ListView) findViewById(R.id.tripscontainer);
                TripDisplayAdapter adapter=new TripDisplayAdapter(TripsActivity.this,R.layout.tripsdisplaychild,trips,TripsActivity.this);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripsActivity.this,AddTripActivity.class);
                intent.putExtra(AddTripActivity.PARAM_FRIENDS,friends);
                startActivity(intent);
                Log.d("demo","clicked floating");
            }
        });
        setTitle("Trips");

    }




    @Override
    public void removeParticiipant(User user) {

    }

    @Override
    public void joinTrip(Trip trip) {
        ArrayList<String> members=trip.getMembers();
        if(members==null){
            members=new ArrayList<String>();
        }
        if(!members.contains(fu.getUid())){
            members.add(fu.getUid());
        }
        trip.setMembers(members);

        reference.child("Trips").child(trip.getKey()).setValue(trip);
    }

    @Override
    public void viewTrip(Trip trip) {
        Intent intent=new Intent(TripsActivity.this,ChatScreen.class);
        intent.putExtra(ChatScreen.PARAM_TRIP,trip);
        startActivity(intent);
    }
}
