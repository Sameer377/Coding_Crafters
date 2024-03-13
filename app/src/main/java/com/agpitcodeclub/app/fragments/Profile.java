package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.agpitcodeclub.app.Dashboard.userLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Profile extends Fragment implements View.OnClickListener{

    private FirebaseUser user;
    private String UserID;
    private CardView check_msg_cv;
    private TextView name_profile;
//    private TextInputLayout tfl_des,tfl_yr;
    private LinearLayout lin_data,social_frame_lin,pro_lin_messages;
    private LinearProgressIndicator profile_prg;
    private RelativeLayout rel_line_teal_profile;
    private AppCompatButton btn_profile_logout;

    private ImageView pro_twitter_logo,pro_linkedin_logo,pro_instagram_logo,pro_github_logo;

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

    private ListView messegelist_recycle;
    
    private void initUit(View view) {

        pro_github_logo=view.findViewById(R.id.pro_github_logo);
        pro_twitter_logo=view.findViewById(R.id.pro_twitter_logo);
        pro_linkedin_logo=view.findViewById(R.id.pro_linkedin_logo);
        pro_instagram_logo=view.findViewById(R.id.pro_instagram_logo);

        name_profile=view.findViewById(R.id.name_profile);
        social_frame_lin=view.findViewById(R.id.social_frame_lin);
        pro_lin_messages=view.findViewById(R.id.pro_lin_messages);
        phone=view.findViewById(R.id.phone_profile);
        designation_profile=view.findViewById(R.id.designation_profile);
        email_profile=view.findViewById(R.id.email_profile);
        persuing_profile=view.findViewById(R.id.persuing_profile);
        tf_password=view.findViewById(R.id.password_profile);
        imgprofile_main=view.findViewById(R.id.imgprofile_main);
        btn_profile_logout=view.findViewById(R.id.logout_profile);
        check_msg_cv=view.findViewById(R.id.check_msg_cv);
//        messegelist_recycle=view.findViewById(R.id.messegelist_recycle);
        btn_profile_logout.setOnClickListener(this);




        setUsetData();
    }




    private DatabaseReference reference;

    private DatabaseReference databaseReference;

    private String userpath,designation;

    private void setUsetData() {
        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        String email = prefs.getString(Credentials.USER_EMAIL, null);
        String pass = prefs.getString(Credentials.USER_PASS, null);
        String name = prefs.getString(Credentials.USER_NAME, null);
        String year = prefs.getString(Credentials.USER_YEAR, null);
        designation = prefs.getString(Credentials.USER_DESIGNATION, null);
        String image = prefs.getString(Credentials.USER_PROFILE_IMG, null);
        String phone_str="8999596143";
        UserID = prefs.getString(Credentials.USER_ID,null);
        userpath=prefs.getString(Credentials.USER_PATH,null);
        persuing_profile.setText(year);
        phone.setText(phone_str);
        name_profile.setText(name);
        if(designation!=null){
            designation_profile.setText(designation.substring(2));

            Glide.with(getContext()).load(image).into(imgprofile_main);

        }

        email_profile.setText(email);
        tf_password.setText(pass);
        if(designation!=null ){
            social_frame_lin.setVisibility(View.VISIBLE);
            if(designation!=FirebasePath.MEMBER){
                pro_lin_messages.setVisibility(View.VISIBLE);
            }else {
                pro_lin_messages.setVisibility(View.GONE);

            }
        }else{
            social_frame_lin.setVisibility(View.GONE);
            pro_lin_messages.setVisibility(View.GONE);

        }

        check_msg_cv.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), ProfileMessages.class));
        });
        
//        loadMsgSender(designation);
        setUrls();
    }
    DatabaseReference database;
    public void setUrls(){



        if(designation!=null){
            if(designation.equals(FirebasePath.DEVELOPER) || designation.equals(FirebasePath.MEMBER)){
                database = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(designation).child(UserID);
            }else{
                database = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(designation);
            }
            pro_github_logo.setOnClickListener((view -> {
                EditTextDialog editTextDialog = new EditTextDialog(getContext(),database.child(FirebasePath.SOCIAL).child(FirebasePath.USER_GITHUB),"Github");
                editTextDialog.show();
            }));
            pro_instagram_logo.setOnClickListener((view -> {
                EditTextDialog editTextDialog = new EditTextDialog(getContext(),database.child(FirebasePath.SOCIAL).child(FirebasePath.USER_INSTAGRAM),"Instagram");
                editTextDialog.show();
                database.child(designation).child(FirebasePath.USER_INSTAGRAM).setValue(editTextDialog.getEnteredText());
            }));
            pro_linkedin_logo.setOnClickListener((view -> {
                EditTextDialog editTextDialog = new EditTextDialog(getContext(),database.child(FirebasePath.SOCIAL).child(FirebasePath.USER_LINKEDIN),"Linkedin");
                editTextDialog.show();
                database.child(designation).child(FirebasePath.USER_LINKEDIN).setValue(editTextDialog.getEnteredText());
            }));
            pro_twitter_logo.setOnClickListener((view -> {
                EditTextDialog editTextDialog = new EditTextDialog(getContext(),database.child(FirebasePath.SOCIAL).child(FirebasePath.USER_TWITTER),"Twitter");
                editTextDialog.show();
                database.child(designation).child(FirebasePath.USER_TWITTER).setValue(editTextDialog.getEnteredText());
            }));



        }
    }

    private ArrayList<String> senderList = new ArrayList<>();
    private ArrayList<String> keylist = new ArrayList<>();
/*
    private void loadMsgSender(String designation){
        messegelist_recycle.isNestedScrollingEnabled();
        String finalpath = userpath+"/";
        if(designation.equals(FirebasePath.DEVELOPER)){
            finalpath +=UserID+"/"+FirebasePath.CONNECT_TO_CHATS;
        }else {
            finalpath += FirebasePath.CONNECT_TO_CHATS;
        }

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

                    senderList.add(snapshot.getKey().toString().split("___")[1]);
                    keylist.add(snapshot.getKey());
                }

                // Notify the adapter that the data has changed
                if(senderList.size()>0){
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.sender_list_profile_item,R.id.sender_list_item_text,senderList);
                    messegelist_recycle.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();

                    messegelist_recycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getContext(),ProfileSendMessageActivity.class);
                            intent.putExtra("sender",keylist.get(i));
                            getContext().startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



    }
*/

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