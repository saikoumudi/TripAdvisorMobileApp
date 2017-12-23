package com.example.homework9_parta;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditUserDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditUserDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "user";

   DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    // TODO: Rename and change types of parameters
    private User mParam1;

    private OnFragmentInteractionListener mListener;

    public EditUserDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditUserDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditUserDataFragment newInstance(String param1, String param2) {
        EditUserDataFragment fragment = new EditUserDataFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (User) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final OnFragmentInteractionListener listener= (OnFragmentInteractionListener) getActivity();
        View view= inflater.inflate(R.layout.fragment_edit_user_data, container, false);
        final EditText fname= (EditText) view.findViewById(R.id.firstname);
        final EditText lname= (EditText) view.findViewById(R.id.lastname);
        final RadioGroup radioGroup= (RadioGroup) view.findViewById(R.id.radioSex);
        if(!mParam1.getGender().equals("Male")){
            radioGroup.check(R.id.radioFemale);
        }
        fname.setText(mParam1.getFname());
        lname.setText(mParam1.getLname());
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().isEmpty()||lname.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter first name and last name",Toast.LENGTH_LONG).show();
                }
                else{
                    mParam1.setFname(fname.getText().toString().trim());
                    mParam1.setLname(lname.getText().toString().trim());
                    if(radioGroup.getCheckedRadioButtonId()==R.id.radioMale){
                        mParam1.setGender("Male");
                    }
                    else{
                        mParam1.setGender("Female");
                    }
                    reference.child("Users").child(mParam1.getUid()).setValue(mParam1);
                    listener.replaceFragment();

                }
            }
        });
        return view;
    }







    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void replaceFragment();
    }
}
