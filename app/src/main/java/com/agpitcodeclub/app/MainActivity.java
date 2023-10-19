package com.agpitcodeclub.app;


import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.credentials.AddMember;


public class MainActivity extends AppCompatActivity {




    private TextView splashtag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayTag();
            }
        }, 300);


    }



    private void displayTag() {
        String tag="Coding Crafters";
//        final MediaPlayer mediaPlayer =MediaPlayer.create(this,R.raw.hacking2);
//        mediaPlayer.start();
        for(int i=0,delay=300;i<15;i++,delay+=100){
            char c=tag.charAt(i);
            if(i==0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splashtag.setText(c+"");
                    }
                }, delay);
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splashtag.append(c+"");
                    }
                }, delay);
            }

            if(i==14){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent adduser = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(adduser);
                        finish();
                    }
                }, delay+800);

            }
        }


    }




    private void initUi() {
        splashtag=findViewById(R.id.splashtag);
    }
}