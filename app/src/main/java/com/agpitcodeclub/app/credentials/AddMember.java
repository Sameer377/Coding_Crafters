package com.agpitcodeclub.app.credentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddMember extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private Spinner designationspinner,persuingyr;
    private ImageView back;
    private AppCompatEditText name,email,edt_phone_adduser;
    private char[] password;
    private String designation="",year="";
    private String userId="";
    private ImageView profileActionImg;
    private  ImageView profileImg;
    private ArrayList arrayListFileObjects=null;
    private Uri fileUrl;
    /* Firebase variables */
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private StorageReference storageReference;
    private String phoneNo="8999596143",message;

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    private String profilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initui();
        initListeners();
        designationspinner();
        yearSpinner();
        initiateFirebase();
    }



    private void initui() {
        persuingyr=findViewById(R.id.persuingyr);
        edt_phone_adduser=findViewById(R.id.edt_phone_adduser);
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


    private void yearSpinner() {
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Select designation");
        arrayList1.add("1st year");
        arrayList1.add("2nd year");
        arrayList1.add("3rd year");
        arrayList1.add("Final year");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        persuingyr.setAdapter(arrayAdapter1);
        persuingyr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Toast.makeText(AddMember.this,"Select year", Toast.LENGTH_SHORT).show();
            }
        });
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


    void uploadFileInStorage(){
        Toast.makeText(AddMember.this, "Entered in file upload",Toast.LENGTH_LONG).show();

        storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.STORAGE_MEMBER_PROFILE+"/"+userId);
        storageReference.putFile(fileUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("Login path : "+uri.toString());
                            }
                        });
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMember.this, "Login "+e,Toast.LENGTH_LONG).show();

            }
        });

        Toast.makeText(AddMember.this, "Entered in file upload",Toast.LENGTH_LONG).show();

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
//                                sendSMSMessage();

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

    public String tempUri="";

    public String getTempUri() {
        return tempUri;
    }

    public void setTempUri(String tempUri) {
        this.tempUri = tempUri;
    }

    void adddetailstoDB(){
        final CommunityModel[] user = new CommunityModel[1];

        storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.STORAGE_MEMBER_PROFILE+"/"+userId);
        storageReference.putFile(fileUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                tempUri=uri.toString();
                                Log.v("profileurrrii","Above user path : "+tempUri);
                                user[0] = new CommunityModel (_name,_email,_password,tempUri   ,year,"des","ok");
                                switch (designation){
                                    case "President" :
                                        mFirebaseDatabase.child(FirebasePath.PRESIDENT).setValue(user[0]);
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.PRESIDENT);

                                        break;
                                    case "Vice President" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.VPRESIDENT);
                                        mFirebaseDatabase.child(FirebasePath.VPRESIDENT).setValue(user[0]); break;
                                    case "Secretary" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.SECRETARY);
                                        mFirebaseDatabase.child(FirebasePath.SECRETARY).setValue(user[0]); break;
                                    case "Vice Secretary" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.VSECRETARY);
                                        mFirebaseDatabase.child(FirebasePath.VSECRETARY).setValue(user[0]); break;
                                    case "Revenue Manger" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.REVENUE_MANAGER);
                                        mFirebaseDatabase.child(FirebasePath.REVENUE_MANAGER).setValue(user[0]); break;
                                    case "Assistant Revenue Manger" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.VREVENUE_MANAGER);
                                        mFirebaseDatabase.child(FirebasePath.VREVENUE_MANAGER).setValue(user[0]); break;
                                    case "Developer" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.DEVELOPER);
                                        mFirebaseDatabase.child(FirebasePath.DEVELOPER).child(userId).setValue(user[0]);
                                        break;
                                    case "Member" :
                                        mFirebaseInstance.getReference(FirebasePath.MEMBER_USERTOKENS).child(userId).setValue(FirebasePath.MEMBER);
                                        mFirebaseDatabase.child(FirebasePath.MEMBER).child(userId).setValue(user[0]);
                                        break;
                                }
                                System.out.println("Login path : "+uri.toString());
                            }
                        });





                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMember.this, "Login "+e,Toast.LENGTH_LONG).show();

            }
        });



        System.out.println("final path : "+getProfilePath());
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
            fileUrl=data.getData();
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


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    protected void sendSMSMessage() {
        phoneNo ="8999596143";
//        phoneNo =edt_phone_adduser.getText().toString().trim();
        message ="ok";/*"Dear "+_name+",\n" +
                "\n" +
                "I am delighted to inform you that you have been selected as the President of the Coding Crafters club. \n"+"Here are your login credentials to access the club's resources:\n" +
                "\n" +
                "Username: "+_email +
                "Password: "+_password+"\n\n         -PRESIDENT";*/
        Toast.makeText(getBaseContext(), "working", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}