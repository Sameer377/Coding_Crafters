package com.agpitcodeclub.app.credentials;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddMember extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private Spinner designationspinner;
    private ImageView back;
    private AppCompatEditText name,email;
    private char[] password;
    private String designation="";
    private String userId="";
    private ImageView profileActionImg;
    private  ImageView profileImg;
    private ArrayList arrayListFileObjects=null;

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
        profileImg=findViewById(R.id.imgprofile_addmember);
        profileActionImg=findViewById(R.id.profile_img_action);
    }

    private void initListeners() {
        back.setOnClickListener(this);
        profileImg.setOnClickListener(this);
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
        switch (designation){
            case "President" :
                user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.PRESIDENT).setValue(user);
                break;
            case "Vice President" :   user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.VPRESIDENT).setValue(user); break;
            case "Secretary" :  user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.SECRETARY).setValue(user); break;
            case "Vice Secretary" :  user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.VSECRETARY).setValue(user); break;
            case "Revenue Manger" :  user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.REVENUE_MANAGER).setValue(user); break;
            case "Assistant Revenue Manger" :  user = new CommunityModel (_name,_email,_password,"profile","hello","des");
                mFirebaseDatabase.child(FirebasePath.VREVENUE_MANAGER).setValue(user); break;
            case "Developer" :      user = new CommunityModel (_name,_email,_password,"profile","hello","ds");
                mFirebaseDatabase.child(FirebasePath.DEVELOPER).child(userId).setValue(user);
                break;
            case "Member" :
                user = new CommunityModel (_name,_email,_password,"profile","hello","ds");
                mFirebaseDatabase.child(FirebasePath.MEMBER).child(userId).setValue(user);
                break;
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
            case R.id.imgprofile_addmember:
                pickImage();
                break;

        }
    }
    public String intentType;
    private void pickImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data.getData()!=null){
            Uri img=data.getData();
            if(img!=null){
                profileActionImg.setImageResource(R.drawable.edit_img_profile);
                profileImg.setImageURI(img);
            }else{
                profileActionImg.setImageResource(R.drawable.camera_white);
            }

        }
    }

    boolean isPermissionGranted = false;

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        isPermissionGranted = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        isPermissionGranted = false;
    }

    public ArrayList getArrayListFileObjects() {
        return arrayListFileObjects;
    }

    public void setArrayListFileObjects(ArrayList arrayListFileObjects) {
        this.arrayListFileObjects = arrayListFileObjects;
    }




}