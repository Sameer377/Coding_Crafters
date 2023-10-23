package com.agpitcodeclub.app.fragments;

import static com.agpitcodeclub.app.utils.FirebasePath.FCM_TOPIC;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.agpitcodeclub.app.Adapters.FileTime;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.ApiUtilities;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.utils.PushNotification;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessege extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText tf_title_msg,tf_msg,tf_msg_loc;
    AppCompatButton btn_sendmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messege);
        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC);
        initUi();
        initListener();
    }

    private void initUi() {
        tf_title_msg=findViewById(R.id.tf_title_msg);
        tf_msg=findViewById(R.id.tf_msg);
        btn_sendmsg=findViewById(R.id.btn_sendmsg);
        tf_msg_loc=findViewById(R.id.tf_msg_loc);
    }

    private void initListener() {
        btn_sendmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sendmsg:
            sendMsg();
            break;
        }
    }
    private void sendMsg() {
        Toast.makeText(SendMessege.this,"", Toast.LENGTH_SHORT).show();
        MsgModel model = new MsgModel(tf_title_msg.getText().toString().trim(),tf_msg.getText().toString().trim(),tf_msg_loc.getText().toString().trim(),"");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        databaseReference.child(new FileTime().getFileTime()).setValue(model);


        tf_title_msg.setText("");
        tf_msg.setText("");
        tf_msg_loc.setText("");
    }


    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification (notification).enqueue (new Callback<PushNotification>() {
            @Override
            public void onResponse (Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(  SendMessege.this,  "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SendMessege.this,  "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure (Call<PushNotification> call, Throwable t) {
                Toast.makeText( SendMessege.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }
}