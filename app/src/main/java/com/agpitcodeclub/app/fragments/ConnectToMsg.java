package com.agpitcodeclub.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ConnectToMsg extends AppCompatActivity {

    private EditText msg_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_msg);
        Intent intent = getIntent();
        initUi(intent);
    }
    TextView username,User_Des;
    private void initUi(Intent intent) {

        username = findViewById(R.id.username_connecttomsg);
        User_Des = findViewById(R.id.User_Des);
        msg_edt=findViewById(R.id.msg_edt);

        Glide.with(getApplicationContext()).load(intent.getExtras().get("ImageUrl")).into((ImageView) findViewById(R.id.imgprofile_main));
        username.setText(intent.getExtras().get("username").toString());
        User_Des.setText(intent.getExtras().get("User_Des").toString().substring(2));

        String pt =intent.getExtras().get("chatpath").toString();
        findViewById(R.id.send_imgbtn).setOnClickListener((view -> {
            sendMessage(pt);
        }));

    }

     DatabaseReference mDatabase;

    private void sendMessage(String chatpath) {

        String msg = msg_edt.getText().toString().trim();
        if(msg.isEmpty()){
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        HashMap<String,String> intimsg = new HashMap<>();
        intimsg.put("msg",msg);
        intimsg.put("date", timeFormat.format(new Date())+"\t"+dateFormat.format(new Date()));

        mDatabase =  FirebaseDatabase.getInstance().getReference().child(FirebasePath.COMMUNITY).child(chatpath);
        mDatabase.child("sender_"+mDatabase.getRef().push().getKey()).setValue(intimsg);

        msg_edt.setText("");
//        chatpath = FirebasePath.DEVELOPER+"/"+model.getUserid()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;

//        chatpath =model.getDesignation()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;
    }


}