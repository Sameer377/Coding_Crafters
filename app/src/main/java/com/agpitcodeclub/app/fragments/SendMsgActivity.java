package com.agpitcodeclub.app.fragments;

import static com.agpitcodeclub.app.utils.FirebasePath.FCM_TOPIC;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.agpitcodeclub.app.Adapters.FileTime;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.ApiUtilities;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.utils.NotificationData;
import com.agpitcodeclub.app.utils.PushNotification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMsgActivity extends AppCompatActivity implements View.OnClickListener {

    EditText tf_msg;
    AppCompatButton btn_sendmsg;
    ImageView close_btm_nav,btn_pickimg_sendmsg;
    private GifImageView up_img_sendmsg;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    String currentDate = dateFormat.format(new Date());
    String currentTime = timeFormat.format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messege);

        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC);

        tf_msg = findViewById(R.id.tf_msg);
        btn_sendmsg = findViewById(R.id.btn_sendmsg);
        close_btm_nav = findViewById(R.id.close_btm_nav);
        btn_pickimg_sendmsg=findViewById(R.id.btn_pickimg_sendmsg);
        up_img_sendmsg=findViewById(R.id.up_img_sendmsg);
        btn_pickimg_sendmsg.setOnClickListener(this);
        close_btm_nav.setOnClickListener(this);
        btn_sendmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sendmsg:
                sendMsg();
                break;
            case R.id.close_btm_nav:
                finish();
                overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);// Close the activity
                break;
            case R.id.btn_pickimg_sendmsg:
                pickImage();
                Toast.makeText(SendMsgActivity.this,"hello",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private Uri fileUrl;

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data.getData()!=null){
            Uri img=data.getData();
            fileUrl=data.getData();
            if(img!=null){
                up_img_sendmsg.setImageURI(img);
            }
        }
    }

    String imgURI=null;
    private void uploadUpComming() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.INBOX).child("upcommingimg");
        if (fileUrl!=null) {
            storageReference.putFile(fileUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgUri = uri.toString();
                                    imgURI=imgUri;
                                    Toast.makeText(getApplicationContext(), " Uploaded" , Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplicationContext(), " Error" + e, Toast.LENGTH_LONG).show();

                        }
                    });
        }else {

            Toast.makeText(getApplicationContext(),"Select image",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMsg() {

        if(tf_msg.getText().toString().trim().isEmpty()){
            tf_msg.setError("empty msg cannot be send !");
            tf_msg.requestFocus();
            return;
        }else {

            String uploadtime = new FileTime().getFileTime().toString();
            SharedPreferences prefs = getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
            String designation = prefs.getString(Credentials.USER_DESIGNATION, null).substring(2);
            String image = prefs.getString(Credentials.USER_PROFILE_IMG, null);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.INBOX).child(uploadtime);
            if (fileUrl != null) {
                storageReference.putFile(fileUrl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imgUri = uri.toString();
                                        MsgModel model = new MsgModel(designation, tf_msg.getText().toString().trim(), image, imgUri, currentDate, currentTime);

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
                                        databaseReference.child(uploadtime).setValue(model);
                                        sendNotification(designation, tf_msg.getText().toString().trim(), imgUri);
                                        Toast.makeText(getApplicationContext(), " Uploaded", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), " Error" + e, Toast.LENGTH_LONG).show();

                            }
                        });
            } else {
                MsgModel model = new MsgModel(designation, tf_msg.getText().toString().trim(), image, null, currentDate, currentTime);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
                databaseReference.child(uploadtime).setValue(model);
                PushNotification notification = new PushNotification(new NotificationData(designation, tf_msg.getText().toString().trim(), null), FCM_TOPIC);
                sendNotification(notification);
            }
        }
    }

    private void sendNotification(String title, String des,String imageUri) {

        PushNotification notification = null;
        try {
            notification = new PushNotification(new NotificationData(title,des,new URL(imageUri)),FCM_TOPIC);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ApiUtilities.getClient().sendNotification (notification).enqueue (new Callback<PushNotification>() {
            @Override
            public void onResponse (Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(getApplicationContext(),  "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),  "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure (Call<PushNotification> call, Throwable t) {
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }
    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(SendMsgActivity.this, "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SendMsgActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(SendMsgActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
