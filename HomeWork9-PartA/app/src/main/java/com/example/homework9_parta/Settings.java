package com.example.homework9_parta;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Settings extends AppCompatActivity implements EditUserDataFragment.OnFragmentInteractionListener {
User user;
FirebaseStorage fs=FirebaseStorage.getInstance();
    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    ImageView profilePic,editImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Profile Settings");
        user= (User) getIntent().getExtras().getSerializable(HomeScreen.CURRENT_USER);

       findViewById(R.id.editicon).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EditUserDataFragment dataFragment=new EditUserDataFragment();
               Bundle bundle=new Bundle();
               bundle.putSerializable(EditUserDataFragment.ARG_PARAM1,user);
               dataFragment.setArguments(bundle);
               getFragmentManager().beginTransaction().replace(R.id.container,dataFragment).commit();
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        profilePic = (ImageView) findViewById(R.id.imageView2);
        editImage= (ImageView) findViewById(R.id.imageView3);
        if(user.getPhotoURL()!=null){
            Picasso.with(Settings.this).load(user.getPhotoURL()).into(profilePic);
        }
        else{
            if(user.getGender().equals("Male")){
                profilePic.setImageResource(R.drawable.men);
            }
            else{
                profilePic.setImageResource(R.drawable.female);
            }
        }
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","edit button clicekd");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(intent, 100);
            }
        });
        ShowDataFragment fragment=new ShowDataFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(ShowDataFragment.ARG_USER,user);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data!=null) {
            final Uri uri = data.getData();

            fs.getReference("/images/").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri uri1=taskSnapshot.getMetadata().getDownloadUrl();
                    user.setPhotoURL(uri1.toString());
                    if(user.getPhotoURL()!=null){
                        Picasso.with(Settings.this).load(user.getPhotoURL()).into(profilePic);
                    }
                    else {
                        if (user.getGender().equals("Male")){
                            profilePic.setImageResource(R.drawable.men);
                        }
                        else {
                            profilePic.setImageResource(R.drawable.female);
                        }
                    }
                    reference.child("Users").child(user.getUid()).setValue(user);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("demo", e.getMessage());
                }
            });
        }
    }

    @Override
    public void replaceFragment() {
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                ShowDataFragment fragment=new ShowDataFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable(ShowDataFragment.ARG_USER,user);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
