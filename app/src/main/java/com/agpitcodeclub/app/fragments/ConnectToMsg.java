package com.agpitcodeclub.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;
import com.bumptech.glide.Glide;

public class ConnectToMsg extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_msg);
        Intent intent = getIntent();
        initUi(intent);
    }
    TextView username;
    private void initUi(Intent intent) {

        username = findViewById(R.id.username_connecttomsg);

        Glide.with(getApplicationContext()).load(intent.getExtras().get("ImageUrl")).into((ImageView) findViewById(R.id.imgprofile_main));
        username.setText(intent.getExtras().get("username").toString());
    }

}