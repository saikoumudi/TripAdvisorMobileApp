package com.example.homework9_parta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by KS Koumudi on 4/21/2017.
 */

public class AddTripParticipant extends ArrayAdapter<User> {

    int res;
    removebuttonclick listener;
    Context context;
    private ArrayList<User> participants;

    public static interface removebuttonclick{
        public void removeParticiipant(User user);
        public void joinTrip(Trip trip);
        public void viewTrip(Trip trip);
    }

    public AddTripParticipant(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<User> objects,removebuttonclick listener) {
        super(context,resource,objects);
        this.context=context;
        this.res=resource;
        this.participants=objects;
        this.listener=listener;
    }



    public ArrayList<User> getParticipants() {
        return participants;
    }


    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(res, parent, false);
        }
            TextView name = (TextView) convertView.findViewById(R.id.textView5);
            final User temp = participants.get(position);
            name.setText(temp.getFname() + " " + temp.getLname());
            convertView.findViewById(R.id.removeParticipant).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.removeParticiipant(temp);
                }
            });

        return  convertView;
    }
}
