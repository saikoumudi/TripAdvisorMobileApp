package com.example.homework9_parta;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverPpl extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fu = mAuth.getCurrentUser();
    User currentUser=null;
    ArrayList<User> people=new ArrayList<User>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_ppl);
        setTitle("People");

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("Users").child(fu.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             currentUser= dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      Log.d("demo","on resume discover people");
      databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              people.clear();
              Log.d("demo","reference data change called");
              for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                  people.add(snapshot.getValue(User.class));
              }
              people.remove(currentUser);
              Log.d("demo","people "+people.toString());
              if(!people.isEmpty()){
                  PeopleDisplayFragment fragment=new PeopleDisplayFragment();
                  Bundle bundle=new Bundle();
                  bundle.putSerializable(PeopleDisplayFragment.ARG_PEOPLE,people);
                  bundle.putSerializable(PeopleDisplayFragment.ARG_USER,currentUser);
                  fragment.setArguments(bundle);
                  getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                  Log.d("demo","fragment replaced");
              }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friendRequests:
                final ArrayList<String> requestsRecieved=currentUser.getRequestRecieved();
                if(requestsRecieved!=null&&!requestsRecieved.isEmpty()) {
                databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<User> requests=new ArrayList<User>();


                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                requests.add(ds.getValue(User.class));
                            }
                            ArrayList<User> clone= (ArrayList<User>) requests.clone();
                            for (User userTemp : requests) {

                               if(!requestsRecieved.contains(userTemp.getUid())){
                                   clone.remove(userTemp);
                               }
                            }
                        RequestListFragment fragment=new RequestListFragment();
                        Bundle arguments=new Bundle();
                        arguments.putSerializable(RequestListFragment.ARG_USER,currentUser);
                        arguments.putSerializable(RequestListFragment.ARG_PEOPLE,clone);
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,fragment).commit();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });}
                else{
                    Toast.makeText(DiscoverPpl.this,"No requests to show",Toast.LENGTH_LONG).show();
                }
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
        inflater.inflate(R.menu.menu_people, menu);
        return true;
    }
}
