package com.agpitcodeclub.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.agpitcodeclub.app.fragments.Announcement;
import com.agpitcodeclub.app.fragments.Community;
import com.agpitcodeclub.app.fragments.Home;
import com.agpitcodeclub.app.fragments.Post;
import com.agpitcodeclub.app.fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    private BottomNavigationView nav;
    private FrameLayout mainframe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initui();

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
        nav.setSelectedItemId(R.id.home);

    }

    private void navbar() {
        nav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:
                        loadFragment(new Home(), true);
                        break;
                    case R.id.community:
                        loadFragment(new Community(), false);
                        break;
                    case R.id.post:
                        loadFragment(new Post(), false);
                        break;
                    case R.id.announcement:
                        loadFragment(new Announcement(), false);
                        break;
                    case R.id.profile:
                        loadFragment(new Profile(), false);
                        break;
                    default:
                        Toast.makeText(Dashboard.this, "btm", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}