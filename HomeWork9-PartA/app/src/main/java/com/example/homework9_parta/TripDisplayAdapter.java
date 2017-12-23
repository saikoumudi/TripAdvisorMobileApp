package com.example.homework9_parta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by KS Koumudi on 4/22/2017.
 */

public class TripDisplayAdapter extends ArrayAdapter<Trip> {
    Context context;
    int resource;
    ArrayList<Trip> trips;
    AddTripParticipant.removebuttonclick listener;
    FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
    public TripDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Trip> objects,AddTripParticipant.removebuttonclick listener) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.trips=objects;
        this.listener=listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView title= (TextView) convertView.findViewById(R.id.triptitle);
        title.setText(trips.get(position).getTitle());
        Button join= (Button) convertView.findViewById(R.id.jointrip);
        if(trips.get(position).getMembers().contains(fu.getUid())||trips.get(position).getOwner().equals(fu.getUid())){
            join.setVisibility(View.INVISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewTrip(trips.get(position));
                }
            });
        }
        else{
            join.setVisibility(View.VISIBLE);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.joinTrip(trips.get(position));
                }
            });
            convertView.setOnClickListener(null);
        }
        return  convertView;
    }
}
