package com.agpitcodeclub.app;

import static com.agpitcodeclub.app.fragments.Home.UPI_PAYMENT_REQUEST_CODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.agpitcodeclub.app.Models.Updates;
import com.agpitcodeclub.app.fragments.Announcement;
import com.agpitcodeclub.app.fragments.CheckInternet;
import com.agpitcodeclub.app.fragments.Community;
import com.agpitcodeclub.app.fragments.Home;
import com.agpitcodeclub.app.fragments.Login;
import com.agpitcodeclub.app.fragments.Post;
import com.agpitcodeclub.app.fragments.Profile;
import com.agpitcodeclub.app.utils.Credentials;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        checkUpdates();
        nav=findViewById(R.id.btm_nav_main);
        mainframe=findViewById(R.id.dashboardframe);
        dash_prg=findViewById(R.id.dash_prg);
      /*  header=findViewById(R.id.rel_frag_header);
        title=findViewById(R.id.frag_title_txt);
        back=findViewById(R.id.frag_title_back);
*/
        nav.setSelectedItemId(R.id.home);
//        header.setVisibility(View.GONE);
        if(InternetIsConnected()){
            loginExistingUser();
            loadFragment(new Home(), false);

        }
        else {
            loadFragment(new CheckInternet(), false);

        }
    }
//    private void initListener() {
//        back.setOnClickListener(this);
//    }


    boolean ishomefragment = false;
    private void navbar() {

        nav.setSelectedItemId(R.id.home);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.home:

//                        header.setVisibility(View.GONE);


                        if(InternetIsConnected()){
                            loginExistingUser();
                            loadFragment(new Home(), false);
                            ishomefragment=true;
                        }
                        else {
                            loadFragment(new CheckInternet(), false);
                            ishomefragment=true;
                        }


                        break;
                    case R.id.community:
//                        title.setText("Community");
//                        header.setVisibility(View.VISIBLE);
                        if(InternetIsConnected()){
                            loadFragment(new Community(Dashboard.this), false);
                            ishomefragment=false;

                        }
                        else {
                            loadFragment(new CheckInternet(), false);
                            ishomefragment=false;

                        }
                        break;
                    case R.id.post:
//                        title.setText("Post");
//                        header.setVisibility(View.VISIBLE);

                        if(InternetIsConnected()){
                            loadFragment(new Post(), false);
                            ishomefragment=false;

                        }
                        else {
                            loadFragment(new CheckInternet(), false);
                            ishomefragment=false;

                        }
                        break;
                    case R.id.announcement:
//                        title.setText("Inbox");
//                        header.setVisibility(View.VISIBLE);

                        if(InternetIsConnected()){
                            loadFragment(new Announcement(), false);
                            ishomefragment=false;

                        }
                        else {
                            loadFragment(new CheckInternet(), false);
                            ishomefragment=false;

                        }
                        break;
                    case R.id.profile:
//                        title.setText("Login");
//                        header.setVisibility(View.VISIBLE);


                        if(InternetIsConnected()){
                            if(userLogin){
                                loadFragment(new Profile(), false);
                                ishomefragment=false;

                            }else{
                                loadFragment(new Login(), false);
                                ishomefragment=false;
                            }

                        }
                        else {
                            loadFragment(new CheckInternet(), false);
                            ishomefragment=false;

                        }
                        break;

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

        if(email!=null){
            dash_prg.setVisibility(View.INVISIBLE);
            userLogin=true;
        }else {
            dash_prg.setVisibility(View.INVISIBLE);
        }
    }

    public boolean InternetIsConnected() {
        getFCMToken();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    private DatabaseReference mDatabase;
// ...

    public static final float APPLICATION_VERSION_CODE=(float) 2.0;
    void checkUpdates(){
        mDatabase   = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("update").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Updates updates = snapshot.getValue(Updates.class);
               if(updates.getAppurl()!=null && APPLICATION_VERSION_CODE<updates.getVersion()){
                  SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(
                           Dashboard.this, SweetAlertDialog.NORMAL_TYPE);
                   sweetAlertDialog.setTitleText("Update app");
                   sweetAlertDialog .setContentText("Update available please download to version "+updates.getVersion()+".");
                   sweetAlertDialog .setConfirmButton("Download", new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   String url = updates.getAppurl();
                                   Intent i = new Intent(Intent.ACTION_VIEW);
                                   i.setData(Uri.parse(url));
                                   startActivity(i);
                               }
                           });
                   sweetAlertDialog.setCancelable(false);
                   sweetAlertDialog  .show();
               }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Payment successful
                String response = data.getStringExtra("response");
                // Handle the payment response as needed
            } else if (resultCode == RESULT_CANCELED) {
                // Payment was canceled by the user
            } else {
                // Payment failed
                String response = data.getStringExtra("response");
                // Handle the payment failure
            }
        }
    }

    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.d("token...",token);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(!ishomefragment){
            if(InternetIsConnected()){
                loginExistingUser();
                loadFragment(new Home(), false);
                ishomefragment=true;
                nav.setSelectedItemId(R.id.home);

            }
            else {
                loadFragment(new CheckInternet(), false);
                ishomefragment=true;

            }
        }else {
            finish();
        }
    }
}