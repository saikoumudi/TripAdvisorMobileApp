package com.example.homework9_parta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KS Koumudi on 4/22/2017.
 */

public class ChatDisplayAdapter extends ArrayAdapter<Message> {
    Context context;
    int resource;
    ArrayList<Message> messages;
    PrettyTime prettyTime=new PrettyTime();
    FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
    public ChatDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.messages=objects;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        Message message=messages.get(position);
        if(message.getSenderUid().equals(fu.getUid())){
            if(message.getMsgType()==MainActivity.MSG_TYPE_TEXT){
                Log.d("demo","right chat child");
               convertView=LayoutInflater.from(context).inflate(R.layout.rightchatchild, parent, false);
                Log.d("demo",convertView.getLayoutParams().toString());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;

                convertView.setLayoutParams(params);
            }
            else{
                convertView=LayoutInflater.from(context).inflate(R.layout.chatimagechild, parent, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;

                convertView.setLayoutParams(params);
                ImageView img= (ImageView) convertView.findViewById(R.id.imageView4);
                Picasso.with(context).load(message.getPhotourl()).into(img);
            }
        }
        else{
            if(message.getMsgType()==MainActivity.MSG_TYPE_TEXT){
                convertView=LayoutInflater.from(context).inflate(R.layout.chatchild, parent, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                params.gravity = Gravity.LEFT;

                convertView.setLayoutParams(params);
            }
            else{
                convertView=LayoutInflater.from(context).inflate(R.layout.chatimagechild, parent, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.LEFT;

                convertView.setLayoutParams(params);
                ImageView img= (ImageView) convertView.findViewById(R.id.imageView4);
                Picasso.with(context).load(message.getPhotourl()).into(img);
            }
        }
        if(message.getMsgType()==MainActivity.MSG_TYPE_TEXT) {
            TextView textView = (TextView) convertView.findViewById(R.id.message);
            textView.setText(message.getMsg());
        }
        else{

        }
        TextView sender= (TextView) convertView.findViewById(R.id.messager);
        if(fu.getUid().equals(message.getSenderUid())){
            sender.setText("You");

        }
        else{
        sender.setText(message.getSenderName());

        }
        TextView time= (TextView) convertView.findViewById(R.id.time);
        time.setText(prettyTime.format(new Date(message.getTime())));

            return convertView;
    }
}
