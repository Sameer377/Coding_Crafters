package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileMessages extends AppCompatActivity {
    private ListView messegelist_recycle;
    private ImageView btn_profile_msg_back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_messages);
        messegelist_recycle=findViewById(R.id.messegelist_recycle);
        btn_profile_msg_back=findViewById(R.id.btn_profile_msg_back);

        btn_profile_msg_back.setOnClickListener((view -> {
            finish();
        }));

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        UserID = prefs.getString(Credentials.USER_ID,null);
        userpath=prefs.getString(Credentials.USER_PATH,null);
        String  des=prefs.getString(Credentials.USER_DESIGNATION,null);
                loadMsgSender(des);
    }


    private ArrayList<String> senderList = new ArrayList<>();
    private ArrayList<String> keylist = new ArrayList<>();
    private String userpath;
    private String UserID;
    private void loadMsgSender(String designation){

        String finalpath = userpath+"/";
        if(designation.equals(FirebasePath.DEVELOPER)){
            finalpath +=UserID+"/"+FirebasePath.CONNECT_TO_CHATS;
        }else {
            finalpath += FirebasePath.CONNECT_TO_CHATS;
        }
        final boolean[] flag = {true};
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(finalpath);
        Log.v("Chat Path profile : ",finalpath);
// Add a ValueEventListener to retrieve data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing data
                senderList.clear();
                keylist.clear();

                // Iterate through the dataSnapshot to get your data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(flag[0]){
                        findViewById(R.id.no_msg_dialog).setVisibility(View.GONE);
                        flag[0] =false;
                    }
                    senderList.add(snapshot.getKey().toString().split("___")[1]);
                    keylist.add(snapshot.getKey());
                }

                // Notify the adapter that the data has changed
                if(senderList.size()>0){
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.sender_list_profile_item,R.id.sender_list_item_text,senderList);
                    messegelist_recycle.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();

                    messegelist_recycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getApplicationContext(),ProfileSendMessageActivity.class);
                            intent.putExtra("sender",keylist.get(i));
                          startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}