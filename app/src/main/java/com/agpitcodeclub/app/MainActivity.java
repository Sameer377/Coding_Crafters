package com.agpitcodeclub.app;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


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

        splashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this, AddUser.class);
//                startActivity(intent);
            }
        });
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


        }

    }




    private void initUi() {
        splashtag=findViewById(R.id.splashtag);
    }
}