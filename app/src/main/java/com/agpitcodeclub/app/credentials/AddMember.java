package com.agpitcodeclub.app.credentials;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.agpitcodeclub.app.FirebasePath;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.Models.User;
import com.agpitcodeclub.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddMember extends AppCompatActivity implements View.OnClickListener {
    private Spinner designationspinner;
    private ImageView back;
    private AppCompatEditText name,email;
    private char[] password;
    private String designation="";
    private String userId="";

    /* Firebase variables */
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initui();
        initListeners();
        designationspinner();
        initiateFirebase();
    }

    private void initui() {
        designationspinner=findViewById(R.id.designation);
        back=findViewById(R.id.img_back_adduser);
        mAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.edt_name);
        email=findViewById(R.id.edt_email_adduser);
    }

    private void initListeners() {
        back.setOnClickListener(this);
        findViewById(R.id.btn_add_addmember).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }

    private void initiateFirebase(){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(FirebasePath.COMMUNITY);
    }


    private void designationspinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select designation");
        arrayList.add("President");
        arrayList.add("Vice President");
        arrayList.add("Secretary");
        arrayList.add("Vice Secretary");
        arrayList.add("Revenue Manger");
        arrayList.add("Assistant Revenue Manger");
        arrayList.add("Developer");
        arrayList.add("Member");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        designationspinner.setAdapter(arrayAdapter);
        designationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Toast.makeText(AddMember.this,"Select Designation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void toast(String t){
        Toast.makeText(AddMember.this,t,Toast.LENGTH_SHORT).show();
    }

    private String _name="",_email="",_password="";
    void addmember(){
        _name=name.getText().toString().trim();
        _email=email.getText().toString().trim();

        /* Password generator */

        PasswordGenerator obj =new PasswordGenerator();
        password = obj.password();

        if(_name.isEmpty()){

            toast("Invalid name");
        }else if(_email.isEmpty()){
            toast("Invalid email");

        }else if(designation=="Select designation"){
            toast("select designation");
        }
        else{
            addcredential();
        }

    }


    private void addcredential() {

        _password = _email.substring(0,3)+String.valueOf(password);
        toast("Password : "+_password);

        mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(AddMember.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            FirebaseUser userI = FirebaseAuth.getInstance().getCurrentUser();

                            if (userI != null) {
                                userId=userI.getUid();
                            }

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("status : ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                adddetailstoDB();
                                Toast.makeText(AddMember.this, "User Added",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display the message to the user.
                                Toast.makeText(AddMember.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                                Log.w("UserCreation", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });


    }

    void adddetailstoDB(){
        CommunityModel user;
        /* Adding details in realtime */
        if(designation=="Developer"||designation=="Member") {
            user = new CommunityModel (_name,_email,_password,"profile","hello","ds");
            mFirebaseDatabase.child(designation).child(userId).setValue(user);
        }
        else{
            user = new CommunityModel (_name,_email,_password,"profile","hello","des");
            mFirebaseDatabase.child(designation).setValue(user);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back_adduser:
                finish();
                break;
            case R.id.btn_add_addmember:
                    addmember();
                break;
        }
    }
}