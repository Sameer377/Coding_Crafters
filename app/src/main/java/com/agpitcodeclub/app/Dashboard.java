package com.agpitcodeclub.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.agpitcodeclub.app.fragments.Announcement;
import com.agpitcodeclub.app.fragments.Community;
import com.agpitcodeclub.app.fragments.Home;
import com.agpitcodeclub.app.fragments.Post;
import com.agpitcodeclub.app.fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back;
    private RelativeLayout header;
    private BottomNavigationView nav;
    private FrameLayout mainframe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initui();
        initListener();
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

        header=findViewById(R.id.rel_frag_header);
        title=findViewById(R.id.frag_title_txt);
        back=findViewById(R.id.frag_title_back);

        nav.setSelectedItemId(R.id.home);
        header.setVisibility(View.GONE);
        loadFragment(new Home(),true);

    }
    private void initListener() {
        back.setOnClickListener(this);
    }

    private void navbar() {
        nav.setSelectedItemId(R.id.home);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        header.setVisibility(View.GONE);
                        loadFragment(new Home(), false);
                        break;
                    case R.id.community:
                        title.setText("Community");
                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Community(), false);
                        break;
                    case R.id.post:
                        title.setText("Post");
                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Post(), false);
                        break;
                    case R.id.announcement:
                        title.setText("Inbox");
                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Announcement(), false);
                        break;
                    case R.id.profile:
                        title.setText("Profile");

                        header.setVisibility(View.VISIBLE);
                        loadFragment(new Profile(), false);
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
            case R.id.frag_title_back: nav.setSelectedItemId(R.id.home); header.setVisibility(View.GONE); loadFragment(new Home(),false); break;
        }
    }
}