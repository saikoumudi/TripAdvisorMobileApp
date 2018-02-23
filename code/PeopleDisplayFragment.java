package com.example.homework9_parta;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;




public class PeopleDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
     static final String ARG_PEOPLE = "friends";
     static final String ARG_USER = "currentUser";
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    // TODO: Rename and change types of parameters
    private User currentUser;
    private ArrayList<User> peopleList;



    public PeopleDisplayFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PeopleDisplayFragment newInstance() {
        PeopleDisplayFragment fragment = new PeopleDisplayFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable(ARG_USER);
            peopleList= (ArrayList<User>) getArguments().getSerializable(ARG_PEOPLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("demo","create view called");
        View view= inflater.inflate(R.layout.fragment_people_display, container, false);
        ListView listView= (ListView) view.findViewById(R.id.containerPeople);
        PeopleListViewAdapter adapter=new PeopleListViewAdapter(getActivity(),R.layout.child_people,peopleList,currentUser);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("demo","adapter set");

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
