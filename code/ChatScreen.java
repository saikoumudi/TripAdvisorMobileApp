package com.example.homework9_parta;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatScreen extends AppCompatActivity {

    static String PARAM_TRIP="trip";
    Trip trip;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage fs=FirebaseStorage.getInstance();
    EditText editTextMessage;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        final ListView listView= (ListView) findViewById(R.id.chatcontainer);
       trip= (Trip) getIntent().getExtras().getSerializable(PARAM_TRIP);
        editTextMessage= (EditText) findViewById(R.id.editText3);
        reference.child("ChatWindows").child(trip.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Message> messages=new ArrayList<Message>();
                Log.d("demo","called chats");
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                   Message messageTemp= snapshot.getValue(Message.class);
                    messages.add(messageTemp);
                    if(messageTemp.getRemovedFrom()!=null&&messageTemp.getRemovedFrom().contains(fu.getUid())){
                        messages.remove(messageTemp);
                    }
                }
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return  (int)(o1.getTime()-o2.getTime());
                    }
                });
                ChatDisplayAdapter adapter=new ChatDisplayAdapter(ChatScreen.this,R.layout.chatchild,messages);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.sendImgView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextMessage.getText().toString().isEmpty()){
                    Toast.makeText(ChatScreen.this,"Please enter messsage",Toast.LENGTH_LONG).show();
                }
                else{
                    Message message=new Message();
                    message.setMsg(editTextMessage.getText().toString().trim());
                    message.setSenderName(fu.getDisplayName());
                    message.setSenderUid(fu.getUid());
                    message.setMsgType(MainActivity.MSG_TYPE_TEXT);
                    message.setTime(System.currentTimeMillis());
                    message.setKey(reference.child("ChatWindows").child(trip.getKey()).push().getKey());
                    reference.child("ChatWindows").child(trip.getKey()).child(message.getKey()).setValue(message);

                }
            }
        });
        findViewById(R.id.galleryImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","edit button clicekd");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data!=null) {
            final Uri uri = data.getData();

            fs.getReference("/images/").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri uri1 = taskSnapshot.getMetadata().getDownloadUrl();
                    Message message=new Message();
                    message.setMsgType(MainActivity.MSG_TYPE_PIC);
                    message.setPhotourl(uri1.toString());
                    message.setTime(System.currentTimeMillis());
                    message.setSenderName(fu.getDisplayName());
                    message.setSenderUid(fu.getUid());
                    message.setKey(reference.child("ChatWindows").child(trip.getKey()).push().getKey());
                    reference.child("ChatWindows").child(trip.getKey()).child(message.getKey()).setValue(message);
                    Log.d("demo","message added"+message);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewTripDetails:
                Intent intent=new Intent(ChatScreen.this,DisplayTrip.class);
                intent.putExtra(DisplayTrip.ARGS_TRIP,trip);
                startActivity(intent);
                break;
            case R.id.logout:

                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chatscreen, menu);
        return true;
    }
}
