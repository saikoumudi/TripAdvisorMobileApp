package com.example.homework9_parta;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ShowDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_USER = "param1";
    // TODO: Rename and change types of parameters
    private User mParam1;


    public ShowDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowDataFragment newInstance(String param1, String param2) {
        ShowDataFragment fragment = new ShowDataFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (User) getArguments().getSerializable(ARG_USER);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_edit_data, container, false);
        TextView firstName= (TextView) view.findViewById(R.id.firstname);
        TextView lastName= (TextView) view.findViewById(R.id.lastname);
        TextView gender= (TextView) view.findViewById(R.id.gender);
        firstName.setText(mParam1.getFname());
        lastName.setText(mParam1.getLname());
        gender.setText(mParam1.getGender());
        return view;
    }







}
