package com.agpitcodeclub.app.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.agpitcodeclub.app.Adapters.FileTime;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendMessege extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText tf_title_msg,tf_msg,tf_msg_loc;
    AppCompatButton btn_sendmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messege);
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
        MsgModel model = new MsgModel(tf_title_msg.getText().toString().trim(),tf_msg.getText().toString().trim(),tf_msg_loc.getText().toString().trim(),"");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        databaseReference.child(new FileTime().getFileTime()).setValue(model);
        tf_title_msg.setText("");
        tf_msg.setText("");
        tf_msg_loc.setText("");
    }
}