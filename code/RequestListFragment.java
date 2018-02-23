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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PEOPLE = "friends";
    static final String ARG_USER = "currentUser";

    // TODO: Rename and change types of parameters
    User currentUser;
    ArrayList<User> peopleList;


    public RequestListFragment() {
        // Required empty public constructor
    }


    public static RequestListFragment newInstance(String param1, String param2) {
        RequestListFragment fragment = new RequestListFragment();

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
        PeopleListViewAdapter adapter=new PeopleListViewAdapter(getActivity(),R.layout.child_request,peopleList,currentUser);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("demo","adapter set");
        return  view;
    }


}
