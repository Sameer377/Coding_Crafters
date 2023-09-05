package com.agpitcodeclub.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.agpitcodeclub.app.Models.User;
import com.agpitcodeclub.app.fragments.Announcement;
import com.agpitcodeclub.app.fragments.Community;
import com.agpitcodeclub.app.fragments.Home;
import com.agpitcodeclub.app.fragments.Login;
import com.agpitcodeclub.app.fragments.Post;
import com.agpitcodeclub.app.fragments.Profile;
import com.agpitcodeclub.app.utils.Credentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

//    private TextView title;
//    private ImageView back;
//    private RelativeLayout header;
    private BottomNavigationView nav;
    private FrameLayout mainframe;
    private LinearProgressIndicator dash_prg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initui();
        loginExistingUser();
//        initListener();
        navbar();
    }



    public void loadFragment(Fragment fragment, boolean flag){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();

        if(flag) {
            ft.add(R.id.dashboardframe, fragment);
        }
        else {
            ft.replace(R.id.dashboardframe,fragment);
        }
        ft.commit();

    }

    private void initui() {
        nav=findViewById(R.id.btm_nav_main);
        mainframe=findViewById(R.id.dashboardframe);
        dash_prg=findViewById(R.id.dash_prg);
      /*  header=findViewById(R.id.rel_frag_header);
        title=findViewById(R.id.frag_title_txt);
        back=findViewById(R.id.frag_title_back);
*/
        nav.setSelectedItemId(R.id.home);
//        header.setVisibility(View.GONE);
        loadFragment(new Home(),true);

    }
//    private void initListener() {
//        back.setOnClickListener(this);
//    }

    private void navbar() {

        nav.setSelectedItemId(R.id.home);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        loginExistingUser();
//                        header.setVisibility(View.GONE);
                        loadFragment(new Home(), false);
                        nav.setVisibility(View.VISIBLE);

                        break;
                    case R.id.community:
//                        title.setText("Community");
//                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Community(Dashboard.this), false);
                        break;
                    case R.id.post:
//                        title.setText("Post");
//                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Post(), false);

                        break;
                    case R.id.announcement:
//                        title.setText("Inbox");
//                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Announcement(), false);

                        break;
                    case R.id.profile:
//                        title.setText("Login");
//                        header.setVisibility(View.VISIBLE);
                        if(userLogin){
                            loadFragment(new Profile(), false);

                        }else{
                            loadFragment(new Login(), false);

                        }


                        break;
                    default:
                        Toast.makeText(Dashboard.this, "btm", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
//            case R.id.frag_title_back: nav.setSelectedItemId(R.id.home); header.setVisibility(View.GONE); loadFragment(new Home(),false); break;
        }
    }

    private FirebaseAuth mAuth;
    public static boolean userLogin=false;

    private void loginExistingUser(){
        SharedPreferences prefs = getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        String email = prefs.getString(Credentials.USER_EMAIL, null);
        String pass = prefs.getString(Credentials.USER_PASS, null);
        mAuth = FirebaseAuth.getInstance();

        if(email!=null){
            mAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(Dashboard.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            dash_prg.setVisibility(View.INVISIBLE);

                        }
                    });
            userLogin=true;

        }else {
            dash_prg.setVisibility(View.INVISIBLE);
        }
    }


}