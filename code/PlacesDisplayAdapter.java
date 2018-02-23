package com.example.homework9_parta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by praka on 4/28/2017.
 */

public class PlacesDisplayAdapter extends ArrayAdapter<LocationAddress> {
    int resource;
    Context context;
    ArrayList<LocationAddress> addresses;
    Trip trip;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    public PlacesDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<LocationAddress> objects,Trip trip) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.addresses=objects;
        this.trip=trip;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView textView= (TextView) convertView.findViewById(R.id.textView5);
        textView.setText(addresses.get(position).getAdddressString());
        convertView.findViewById(R.id.removeParticipant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addresses.size()>1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm Your Action")
                            .setMessage("Are you sure that you want to remove " + addresses.get(position).getAdddressString() + " from trip")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ProgressDialog pd = new ProgressDialog(context);
                                    pd.setMessage("Removing " + addresses.get(position).getAdddressString() + " from trip");
                                    pd.setCancelable(false);
                                    pd.show();
                                    addresses.remove(position);
                                    trip.setLocationAddresses(addresses);
                                    reference.child("Trips").child(trip.getKey()).setValue(trip);
                                    pd.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                }
                else{
                    Toast.makeText(context,"Trip should have atleast one place",Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }
}
