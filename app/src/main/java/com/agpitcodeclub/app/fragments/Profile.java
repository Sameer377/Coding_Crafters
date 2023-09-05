package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.agpitcodeclub.app.Dashboard.userLogin;
import static com.agpitcodeclub.app.fragments.Login.isMember;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Profile extends Fragment implements View.OnClickListener{

    private FirebaseUser user;
    private String UserID;
    private TextView profile_name_head;
    private TextInputLayout tfl_des,tfl_yr;
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUit(view);
        return view;
    }

    private TextInputEditText tf_designation,tf_email,tf_year,tf_password;
    private ShapeableImageView imgprofile_main;
//    private boolean isMember=false;


    private void initUit(View view) {
        profile_name_head=view.findViewById(R.id.profile_name_head);
        tf_designation=view.findViewById(R.id.tf_designation);
        tf_email=view.findViewById(R.id.tf_email);
        tf_year=view.findViewById(R.id.tf_year);
        tf_password=view.findViewById(R.id.tf_password);
        imgprofile_main=view.findViewById(R.id.imgprofile_main);
        tfl_des=view.findViewById(R.id.tfl_des);
        tfl_yr=view.findViewById(R.id.tfl_yr);
        lin_data=view.findViewById(R.id.lin_data);
        profile_prg=view.findViewById(R.id.profile_prg);
        rel_line_teal_profile=view.findViewById(R.id.rel_line_teal_profile);
        btn_profile_logout=view.findViewById(R.id.btn_profile_logout);
        btn_profile_logout.setOnClickListener(this);
//        isMember= getArguments().getBoolean("isMember");

        initiateUser();
    }

    private DatabaseReference reference;

    private DatabaseReference databaseReference;
    private String token_des = "";


    private void initiateUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dashboardframe, new Login(), "NewFragmentTag");
        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = prefs.getString(Credentials.USER_DES, null);
//        Toast.makeText(getContext(),"des "+des,Toast.LENGTH_SHORT).show();

        if(des!=null){
            isMember=true;
        }
        SharedPreferences.Editor editor = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE).edit();
        if(isMember){
            databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.MEMBER_USERTOKENS);

            databaseReference.child(UserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NotNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {


                        if (task.getResult().getValue()==null){
                          /*  if(des!=null){
                                token_des=des;
                            }else{*/
                                Toast.makeText(getContext(),"User not found ! ",Toast.LENGTH_SHORT).show();
                                ft.commit();
//                            }

                            return;
                        }else{
                            token_des = task.getResult().getValue().toString();
                        }

                        reference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);



                        if(!token_des.equals(FirebasePath.DEVELOPER)&& !token_des.equals(FirebasePath.MEMBER)) {
                            Toast.makeText(getContext(),"en not dev",Toast.LENGTH_SHORT).show();

                            reference.child(token_des).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        if(token_des.equals(FirebasePath.PRESIDENT)){
                                            editor.putString(Credentials.USER_DES, FirebasePath.PRESIDENT);
                                            editor.apply();
                                        }
                                        profile_prg.setVisibility(View.INVISIBLE);
                                        rel_line_teal_profile.setVisibility(View.VISIBLE);
                                        lin_data.setVisibility(View.VISIBLE);
                                        profile_name_head.setText(user.getName());
                                        tf_designation.setText(token_des.substring(2));
                                        tf_email.setText(user.getemail());
                                        tf_password.setText(user.getPassword());
                                        tf_year.setText(user.getPersuing());
                                        Glide.with(getContext()).load(user.getProfile()).into(imgprofile_main);
                                    }else {
                                        Toast.makeText(getContext(),"User not found ! not dev",Toast.LENGTH_SHORT).show();
                                        ft.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    ft.commit();

                                }
                            });
                        }
                       else if(token_des.equals(FirebasePath.DEVELOPER)) {
                            reference.child(token_des).child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        profile_prg.setVisibility(View.INVISIBLE);
                                        rel_line_teal_profile.setVisibility(View.VISIBLE);

                                        lin_data.setVisibility(View.VISIBLE);
                                        profile_name_head.setText(user.getName());
                                        tf_designation.setText(token_des.substring(2));
                                        tf_email.setText(user.getemail());
                                        tf_password.setText(user.getPassword());
                                        tf_year.setText(user.getPersuing());
                                        Glide.with(getContext()).load(user.getProfile()).into(imgprofile_main);
                                    }else {
                                        Toast.makeText(getContext(),"User not found ! dev",Toast.LENGTH_SHORT).show();
                                        ft.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    ft.commit();

                                }
                            });
                        }else if(token_des.equals(FirebasePath.MEMBER)) {
                            reference.child(token_des).child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        profile_prg.setVisibility(View.INVISIBLE);
                                        rel_line_teal_profile.setVisibility(View.VISIBLE);

                                        lin_data.setVisibility(View.VISIBLE);
                                        profile_name_head.setText(user.getName());
                                        tf_designation.setText(token_des.substring(2));
                                        tf_email.setText(user.getemail());
                                        tf_password.setText(user.getPassword());
                                        tf_year.setText(user.getPersuing());
                                        Glide.with(getContext()).load(user.getProfile()).into(imgprofile_main);
                                    }else {
                                        Toast.makeText(getContext(),"User not found ! mem",Toast.LENGTH_SHORT).show();
                                        ft.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    ft.commit();

                                }
                            });
                        }
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });
        }else{

            databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.USERS);


                            databaseReference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    CommunityModel user = snapshot.getValue(CommunityModel.class);
                                    if (user != null) {
                                        profile_prg.setVisibility(View.INVISIBLE);
                                        rel_line_teal_profile.setVisibility(View.VISIBLE);

                                        lin_data.setVisibility(View.VISIBLE);
                                        lin_data.setVisibility(View.VISIBLE);
                                        tfl_des.setVisibility(View.GONE);
                                        tfl_yr.setVisibility(View.GONE);
                                        profile_name_head.setText(user.getName());
                                        tf_email.setText(user.getemail());
                                        tf_password.setText(user.getPassword());
                                        imgprofile_main.setPadding(40,40,40,40);
                                        imgprofile_main.setImageResource(R.drawable.person_filled);

                                    }else{
                                        Toast.makeText(getContext(),"User not found ! user",Toast.LENGTH_SHORT).show();
                                        ft.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    ft.commit();

                                }
                            });


        }


    }

    private void logOut(){
        getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE).edit().clear().commit();
        userLogin=false;
        FirebaseAuth.getInstance().signOut();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dashboardframe, new Login(), "NewFragmentTag");
        ft.commit();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_profile_logout:
                logOut();
                break;
        }
    }
}