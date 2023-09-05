package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agpitcodeclub.app.Dashboard;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.Models.User;
import com.agpitcodeclub.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;


public class Login extends Fragment implements View.OnClickListener {

    private EditText cre_name, cre_email, cre_password;
    private AppCompatButton login_btn;
    private CheckBox checkbox_mem;
    private String userId="";
    private TextView l_txt,txt_page_t,txt_link_signup;
    private LinearLayout lin_page_t;
    private LinearProgressIndicator signup_status_prg;
    private boolean signup =true;

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference databaseReference;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context context ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initUi(view);
        initListener();
        context=container.getContext();

        return view;
    }

    private void initListener() {
        login_btn.setOnClickListener(this);
        lin_page_t.setOnClickListener(this);
    }

    private void initUi(View view) {
        cre_name = view.findViewById(R.id.cre_name);
        cre_email = view.findViewById(R.id.cre_email);
        cre_password = view.findViewById(R.id.cre_password);
        login_btn = view.findViewById(R.id.login_btn);
        checkbox_mem = view.findViewById(R.id.checkbox_mem);
        l_txt=view.findViewById(R.id.l_txt);
        lin_page_t=view.findViewById(R.id.lin_page_t);
        txt_link_signup=view.findViewById(R.id.txt_link_signup);
        txt_page_t=view.findViewById(R.id.txt_page_t);
        signup_status_prg=view.findViewById(R.id.signup_status_prg);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(FirebasePath.USERS);


    }


    private void signUp(){
        cre_name.setVisibility(View.VISIBLE);
        l_txt.setText("Sign Up");
        login_btn.setText("Sign Up");
        txt_link_signup.setText("Login");
        txt_page_t.setText("Already have  an Account ?");
        checkbox_mem.setVisibility(View.INVISIBLE);
    }

    private void login(){
        cre_name.setVisibility(View.INVISIBLE);
        l_txt.setText("Login");
        login_btn.setText("Login");
        txt_page_t.setText("New User? ");
        txt_link_signup.setText("Sign Up");
        checkbox_mem.setVisibility(View.VISIBLE);
    }

    public static boolean isMember=false;

    private void adduser() {

        String name = cre_name.getText().toString().trim();
        String email = cre_email.getText().toString().trim();
        String password = cre_password.getText().toString().trim();

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);

        if (name.isEmpty()) {
            cre_name.setError("Invalid name");
            cre_name.requestFocus();
            return;
        }  if (email.isEmpty() || !Pattern.compile(regex).matcher(email).matches()) {
            cre_email.setError("Invalid email");
            cre_email.requestFocus();
            return;
        }  if (password.isEmpty()) {
            cre_password.setError("Invalid Password");
            cre_password.requestFocus();
            return;
        }

            signup_status_prg.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            FirebaseUser userI = FirebaseAuth.getInstance().getCurrentUser();

                            if (userI != null) {
                                userId=userI.getUid();
                            }

                            if (task.isSuccessful()) {
                                storeUserData(email,password);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("status : ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                User user1=new User(name,email,password);
                                mFirebaseDatabase       .child(userId).setValue(user1);
                                signup_status_prg.setVisibility(View.GONE);

                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.dashboardframe, new Profile(), "NewFragmentTag");
//                                ft.add(R.id.dashboardframe, ldf);
                                ft.commit();
                                Toast.makeText(getContext(), "User Added",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                signup_status_prg.setVisibility(View.GONE);
                                // If sign in fails, display the message to the user.
                                Toast.makeText(getContext(), "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                                Log.w("UserCreation", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case   R.id.login_btn:
                if (signup){
                    adduser();
                }else {
                    loginUser();
                }

                break;

            case R.id.lin_page_t:

                if (signup){
                    login();

                    signup =false;
                }else {
                    signUp();

                    signup =true;
                }

                break;
        }
    }

    private void loginUser() {
        String name = cre_name.getText().toString().trim();
        String email = cre_email.getText().toString().trim();
        String password = cre_password.getText().toString().trim();


        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);

        if (email.isEmpty() || !Pattern.compile(regex).matcher(email).matches()) {
            cre_email.setError("Invalid email");
            cre_email.requestFocus();
            return;
        }  if (password.isEmpty()) {
            cre_password.setError("Invalid Password");
            cre_password.requestFocus();
            return;
        }
        signup_status_prg.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            signup_status_prg.setVisibility(View.GONE);
                            storeUserData(email,password);
                            isMember=checkbox_mem.isChecked();

                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.dashboardframe, new Profile(), "NewFragmentTag");
                            ft.commit();

                        } else {
                            signup_status_prg.setVisibility(View.GONE);
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    private void storeUserData(String email,String pass){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE).edit();
        editor.putString(Credentials.USER_EMAIL, email);
        editor.putString(Credentials.USER_PASS, pass);
        if(checkbox_mem.isChecked()) {
            databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.MEMBER_USERTOKENS);
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
//                        Toast.makeText(getContext(),"des "+task.getResult().getValue().toString(),Toast.LENGTH_SHORT).show();

//                        editor.putString(Credentials.USER_DES, FirebasePath.DEVELOPER);
                    }
                }
            });
        }
        editor.apply();

    }
}