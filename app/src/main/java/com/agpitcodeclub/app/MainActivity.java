package com.agpitcodeclub.app;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


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

       String tag = "Coding Crafters";
       final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

       for (int i = 0, delay = 300; i < 15; i++, delay += 100) {
           char c = tag.charAt(i);


           final int finalI = i; // Need to use final variable inside the inner class
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {



                   if (finalI >= 6) {
                       spannableStringBuilder.append(c);
                       spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.soft_red)),
                               spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                       spannableStringBuilder.setSpan(new StyleSpan(Typeface.NORMAL),
                               spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);

                   } else {
                       spannableStringBuilder.append(c);
                       spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                               spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);

                   }

                   splashtag.setText(spannableStringBuilder); // Update the TextView with colored text
               }
           }, delay);

           if (i == 14) {
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       Intent adduser = new Intent(MainActivity.this, Dashboard.class);
                       startActivity(adduser);
                       finish();
                   }
               }, delay + 800);
           }
       }
   }



    private void initUi() {
        splashtag=findViewById(R.id.splashtag);
    }
}