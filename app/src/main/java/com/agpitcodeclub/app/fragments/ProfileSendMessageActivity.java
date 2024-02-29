package com.agpitcodeclub.app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Adapters.ConnectToChatsAdapter;
import com.agpitcodeclub.app.Models.ConnectToMsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProfileSendMessageActivity  extends AppCompatActivity {

    private EditText msg_edt;
    private RecyclerView connectto_recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_msg);
        Intent intent = getIntent();
        initUi(intent);
    }
    TextView username,User_Des,guide_txt;
    String chatpath;
    private void initUi(Intent intent) {

        SharedPreferences prefs = getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        String user_des = prefs.getString(Credentials.USER_DESIGNATION,null);
        String uid = prefs.getString(Credentials.USER_ID,null);

        username = findViewById(R.id.username_connecttomsg);
        User_Des = findViewById(R.id.User_Des);
        msg_edt=findViewById(R.id.msg_edt);
        connectto_recycle=findViewById(R.id.connectto_recycle);
        guide_txt=findViewById(R.id.guide_txt);

        ImageView imageView = findViewById(R.id.imgprofile_main);
        imageView.setVisibility(View.GONE);
        String sender = intent.getExtras().get("sender").toString();
        username.setText(sender.split("___")[1]);
        User_Des.setVisibility(View.GONE);
        guide_txt.setVisibility(View.GONE);
        if(user_des==FirebasePath.DEVELOPER){
            chatpath = user_des+"/"+uid+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+sender;
        }else{
            chatpath = user_des+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+sender;
        }



        findViewById(R.id.send_imgbtn).setOnClickListener((view -> {
            sendMessage(chatpath);
        }));


        loadChats();


    }

    private void loadChats() {
        ArrayList<ConnectToMsgModel> dataList = new ArrayList<ConnectToMsgModel>();
        ConnectToChatsAdapter connectToChatsAdapter = new ConnectToChatsAdapter(getApplicationContext());
        connectToChatsAdapter.setINTENT_ID(1);
        final boolean[] v = {true};

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(chatpath);
        Log.v("Chat Path : ",chatpath);
// Add a ValueEventListener to retrieve data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing data
                dataList.clear();

                // Iterate through the dataSnapshot to get your data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(v[0]){
                        guide_txt.setVisibility(View.GONE);
                        v[0] =false;
                    }
                    ConnectToMsgModel  data = snapshot.getValue(ConnectToMsgModel.class);
                    data.setUser(snapshot.getKey().split("___")[1]);
                    Log.v("Chat Path : ",data.getMsg());
                    dataList.add(data);
                }

                // Notify the adapter that the data has changed
                if(dataList.size()>0){

                    connectToChatsAdapter.setList(dataList);
                    connectto_recycle.setAdapter(connectToChatsAdapter);
                    connectto_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    connectToChatsAdapter.notifyDataSetChanged();
                    connectto_recycle.scrollToPosition(dataList.size()-1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        connectto_recycle.scrollToPosition(dataList.size()-1);


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
        mDatabase.child(mDatabase.getRef().push().getKey()+"___receiver").setValue(intimsg);

        msg_edt.setText("");
//        chatpath = FirebasePath.DEVELOPER+"/"+model.getUserid()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;

//        chatpath =model.getDesignation()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;
    }


}