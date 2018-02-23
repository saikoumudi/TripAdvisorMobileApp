package com.example.homework9_parta;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.Contacts;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KS Koumudi on 4/20/2017.
 */

public class PeopleListViewAdapter extends ArrayAdapter<User> {

    ArrayList<User> peopleList;
    User currentUser;
    Context context;
    int resouce;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    public PeopleListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<User> objects,User currentUser) {
        super(context, resource, objects);
        this.context=context;
        this.peopleList=objects;
        this.resouce=resource;
        this.currentUser=currentUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(resouce, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.textView);
       final User toFriend = peopleList.get(position);
        name.setText(peopleList.get(position).getFname() + " " + peopleList.get(position).getLname());
        if(resouce==R.layout.child_people) {


            ImageView icon = (ImageView) convertView.findViewById(R.id.imageButton);

            ArrayList<String> fiends = currentUser.getFriends();
            ArrayList<String> requestSent = currentUser.getRequestSent();
            if (fiends.contains(peopleList.get(position).getUid())) {
                icon.setImageResource(R.drawable.firiendicon);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to unfriend "+toFriend.getFname()+" "+toFriend.getLname()+" ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                      ArrayList<String> friends=currentUser.getFriends();
                                        friends.remove(toFriend.getUid());
                                        currentUser.setFriends(friends);
                                        ArrayList<String> toFriendFriends=toFriend.getFriends();
                                        toFriendFriends.remove(currentUser.getUid());
                                        toFriend.setFriends(toFriendFriends);
                                        reference.child("Users").child(currentUser.getUid()).setValue(currentUser);
                                        reference.child("Users").child(toFriend.getUid()).setValue(toFriend);
                                        notifyDataSetChanged();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                                );
                        builder.create().show();
                        return true;
                    }
                });
            } else if (requestSent.contains(peopleList.get(position).getUid())) {
                icon.setImageResource(R.drawable.tickfriend);
            } else {
                icon.setImageResource(R.drawable.friendreqicon);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> requestSent = currentUser.getRequestSent();
                        if (requestSent == null) {
                            requestSent = new ArrayList<String>();
                        }

                        requestSent.add(toFriend.getUid());
                        currentUser.setRequestSent(requestSent);

                        ArrayList<String> tofriendRequests = toFriend.getRequestRecieved();
                        if (tofriendRequests == null) {
                            tofriendRequests = new ArrayList<String>();
                        }
                        tofriendRequests.add(currentUser.getUid());
                        toFriend.setRequestRecieved(tofriendRequests);
                        reference.child("Users").child(currentUser.getUid()).setValue(currentUser);
                        reference.child("Users").child(toFriend.getUid()).setValue(toFriend);
                        notifyDataSetChanged();
                    }
                });

            }
            Log.d("demo", "getview called");
        }
        else{
           convertView.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   User tobeFriend=peopleList.get(position);
                   ArrayList<String> requestsRecieved=currentUser.getRequestRecieved();
                   requestsRecieved.remove(tobeFriend.getUid());
                   currentUser.setRequestRecieved(requestsRecieved);
                   ArrayList<String> friends=currentUser.getFriends();
                   if(friends==null){
                       friends=new ArrayList<String>();
                   }
                   friends.add(tobeFriend.getUid());
                   currentUser.setFriends(friends);
                   tobeFriend.getRequestSent().remove(currentUser.getUid());
                   ArrayList<String> friendsOfFriend=tobeFriend.getFriends();
                   if(friendsOfFriend==null){
                       friendsOfFriend=new ArrayList<String>();
                   }
                   friendsOfFriend.add(currentUser.getUid());
                   tobeFriend.setFriends(friendsOfFriend);
                   reference.child("Users").child(currentUser.getUid()).setValue(currentUser);
                   reference.child("Users").child(tobeFriend.getUid()).setValue(tobeFriend);
                   notifyDataSetChanged();
               }
           });
            convertView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User tobeFriend=peopleList.get(position);
                    ArrayList<String> requestsRecieved=currentUser.getRequestRecieved();
                    requestsRecieved.remove(tobeFriend.getUid());
                    currentUser.setRequestRecieved(requestsRecieved);

                    tobeFriend.getRequestSent().remove(currentUser.getUid());
                    reference.child("Users").child(currentUser.getUid()).setValue(currentUser);
                    reference.child("Users").child(tobeFriend.getUid()).setValue(tobeFriend);

                    notifyDataSetChanged();


                }
            });
        }
        return  convertView;
    }
}
