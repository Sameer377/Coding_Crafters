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
    TextView username,User_Des,guide_txt;
    private void initUi(Intent intent) {

        username = findViewById(R.id.username_connecttomsg);
        User_Des = findViewById(R.id.User_Des);
        guide_txt=findViewById(R.id.guide_txt);
        Glide.with(getApplicationContext()).load(intent.getExtras().get("ImageUrl")).into((ImageView) findViewById(R.id.imgprofile_main));
        username.setText(intent.getExtras().get("username").toString());
        User_Des.setText(intent.getExtras().get("User_Des").toString().substring(2));

        String gt="Hey \uD83D\uDC4B\uD83C\uDFFB this is "+ intent.getExtras().get("username").toString().split(" ")[0]+" ask me anything i will guide you :) ";

        guide_txt.setText(gt);
    }

}