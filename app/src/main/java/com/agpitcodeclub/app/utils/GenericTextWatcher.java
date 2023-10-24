package com.agpitcodeclub.app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;


public class GenericTextWatcher extends AppCompatActivity implements TextWatcher
{
    private View view;
    public GenericTextWatcher(View view)
    {
        this.view = view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();
        switch(view.getId())
        {

            case R.id.otp_edt_1:
                if(text.length()==1)
                    findViewById(R.id.otp_edt_2).requestFocus();
                break;
            case R.id.otp_edt_2:
                if(text.length()==1)
                    findViewById(R.id.otp_edt_3).requestFocus();
                else if(text.length()==0)
                    findViewById(R.id.otp_edt_1).requestFocus();
                break;
            case R.id.otp_edt_3:
                if(text.length()==1)
                    findViewById(R.id.otp_edt_4).requestFocus();
                else if(text.length()==0)
                    findViewById(R.id.otp_edt_2).requestFocus();
                break;
            case R.id.otp_edt_4:
                if(text.length()==0)
                    findViewById(R.id.otp_edt_3).requestFocus();
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }
}