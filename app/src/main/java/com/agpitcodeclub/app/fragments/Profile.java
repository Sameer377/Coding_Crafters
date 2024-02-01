package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.agpitcodeclub.app.Dashboard.userLogin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Profile extends Fragment implements View.OnClickListener{

    private FirebaseUser user;
    private String UserID;
    private TextView name_profile;
//    private TextInputLayout tfl_des,tfl_yr;
    private LinearLayout lin_data;
    private LinearProgressIndicator profile_prg;
    private RelativeLayout rel_line_teal_profile;
    private AppCompatButton btn_profile_logout;
    public Profile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_1, container, false);
        initUit(view);
        return view;
    }

//    private TextInputEditText tf_designation,tf_email,persuing_profile,tf_password;
    private TextView designation_profile,email_profile,persuing_profile,phone, tf_password;
    private ShapeableImageView imgprofile_main;
//    private boolean isMember=false;


    
    private void initUit(View view) {
        name_profile=view.findViewById(R.id.name_profile);
        phone=view.findViewById(R.id.phone_profile);
        designation_profile=view.findViewById(R.id.designation_profile);
        email_profile=view.findViewById(R.id.email_profile);
        persuing_profile=view.findViewById(R.id.persuing_profile);
        tf_password=view.findViewById(R.id.password_profile);
        imgprofile_main=view.findViewById(R.id.imgprofile_main);
        btn_profile_logout=view.findViewById(R.id.logout_profile);
        btn_profile_logout.setOnClickListener(this);
        setUsetData();
    }




    private DatabaseReference reference;

    private DatabaseReference databaseReference;
    private String token_des = "";

    private void setUsetData() {
        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        String email = prefs.getString(Credentials.USER_EMAIL, null);
        String pass = prefs.getString(Credentials.USER_PASS, null);
        String name = prefs.getString(Credentials.USER_NAME, null);
        String year = prefs.getString(Credentials.USER_YEAR, null);
        String designation = prefs.getString(Credentials.USER_DESIGNATION, null);
        String image = prefs.getString(Credentials.USER_PROFILE_IMG, null);
        String phone_str="8999596143";
        
        phone.setText(phone_str);
        name_profile.setText(name);
        if(designation!=null){
            designation_profile.setText(designation.substring(2));
            persuing_profile.setText(year);
            Glide.with(getContext()).load(image).into(imgprofile_main);

        }

        email_profile.setText(email);
        tf_password.setText(pass);
    }

    private void logOut(){
        getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE).edit().clear().commit();
        userLogin=false;
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dashboardframe, new Login(), "NewFragmentTag");
        ft.commit();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logout_profile:
                logOut();
                break;
        }
    }
}