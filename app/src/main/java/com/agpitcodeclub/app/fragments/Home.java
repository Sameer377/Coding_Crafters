package com.agpitcodeclub.app.fragments;

import static android.app.Activity.RESULT_OK;
import static com.agpitcodeclub.app.utils.FirebasePath.FCM_TOPIC;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.agpitcodeclub.app.Models.UpCommingModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.ApiUtilities;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.utils.NotificationData;
import com.agpitcodeclub.app.utils.PushNotification;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    private ImageSlider imageSlider;
    private TextView txt_intro;
    private TextView txt_logo;
    private ImageView btn_pickimg;
    private AppCompatButton btn_upload_up_img;
    private GifImageView up_img;
    private EditText edt_des_up_img;
    private Uri fileUrl;
    private ProgressBar upcomming_btn_prg;
    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        return fragment;
    }
    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState) {
        initUi(view);
        sliderImage();
    }

    private void initUi(View view) {
        txt_logo=view.findViewById(R.id.txt_logo);
        imageSlider=view.findViewById(R.id.intro_img_slider);
        txt_intro=view.findViewById(R.id.txt_intro);
        up_img=view.findViewById(R.id.up_img);
        btn_upload_up_img=view.findViewById(R.id.btn_upload_up_img);
        btn_pickimg=view.findViewById(R.id.btn_pickimg);
        edt_des_up_img=view.findViewById(R.id.edt_des_up_img);
        btn_pickimg.setOnClickListener(this);
        btn_upload_up_img.setOnClickListener(this);
        upcomming_btn_prg=view.findViewById(R.id.upcomming_btn_prg);
        setImageDetails();

        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = prefs.getString(Credentials.USER_DESIGNATION, null);

        if(des==null){
            des="";
        }

        if (des.equals(FirebasePath.PRESIDENT)) {
            btn_upload_up_img.setVisibility(View.VISIBLE);
            btn_pickimg.setVisibility(View.VISIBLE);
        }else {
            btn_upload_up_img.setVisibility(View.GONE);
            btn_pickimg.setVisibility(View.GONE);
            edt_des_up_img.setEnabled(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC);
    }

    private void sliderImage() {
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/codingcrafters-fac21.appspot.com/o/dash%2Finstitute.jpg?alt=media&token=09b12f94-6f3a-4198-9af4-36cf96995aaf", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/codingcrafters-fac21.appspot.com/o/dash%2FSecretaryDesk.png?alt=media&token=c77b089c-224e-4baf-8a20-b128ef7ff118", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/codingcrafters-fac21.appspot.com/o/dash%2Fcampus_director_chougule_sir.jpg?alt=media&token=5c54276f-2c93-4542-93da-da1105ae5f20", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/codingcrafters-fac21.appspot.com/o/dash%2Fvice-principal.jfif?alt=media&token=2bc47b5c-c250-4afe-aa37-4886bb7e1388", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/codingcrafters-fac21.appspot.com/o/dash%2FSAVE_20230905_122924%20(1).jpg?alt=media&token=c075a5ac-fca1-40d7-b1f4-56bed5c95259", ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
       
        try {
            view  =inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {

        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        
        // Inflate the layout for this fragment
        displayTag();

        return view;
    }

    boolean pause=true;

    private void displayTag() {
        String tag = "Coding Crafters";
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        for (int i = 0, delay = 300; i < 15 && pause; i++, delay += 100) {
            char c = tag.charAt(i);


            final int finalI = i; // Need to use final variable inside the inner class
          new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (finalI >= 6) {
                        spannableStringBuilder.append(c);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.soft_red)),
                                spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.NORMAL),
                                spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);

                    } else {

                        spannableStringBuilder.append(c);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                                spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);

                    }

                    txt_logo.setText(spannableStringBuilder); // Update the TextView with colored text
                }
            }, delay);




            if (i == 14) {
            }
        }



    }


    public String intentType;

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);

    }

    public String tempUri="";

    public String getTempUri() {
        return tempUri;
    }

    public void setTempUri(String tempUri) {
        this.tempUri = tempUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data.getData()!=null){
            Uri img=data.getData();
            fileUrl=data.getData();
            if(img!=null){
                up_img.setImageURI(img);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pickimg:
                pickImage();
                break;

            case R.id.btn_upload_up_img:
                if(!edt_des_up_img.getText().toString().trim().equals(null)){
                    uploadUpComming();
                }else {
                    edt_des_up_img.setError("Cannot be Empty");
                }
                break;

        }
    }

    private void uploadUpComming() {
        upcomming_btn_prg.setVisibility(View.VISIBLE);
        btn_upload_up_img.setVisibility(View.GONE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.DASHBOARD).child("upcommingimg");
        if (fileUrl!=null) {
            storageReference.putFile(fileUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgUri = uri.toString();
                                    UpCommingModel upcommingModel = new UpCommingModel(imgUri, edt_des_up_img.getText().toString().trim());
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.UPCOMMING);
                                    databaseReference.setValue(upcommingModel);

                                    //Push nnotification
                                    PushNotification notification = new PushNotification(new NotificationData("UpcommingEvent",edt_des_up_img.getText().toString().trim()),FCM_TOPIC);
                                    sendNotification(notification);

                                    upcomming_btn_prg.setVisibility(View.GONE);
                                    btn_upload_up_img.setVisibility(View.VISIBLE);

                                    Toast.makeText(getContext(), " Uploaded" , Toast.LENGTH_LONG).show();

                                }
                            });
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(), " Error" + e, Toast.LENGTH_LONG).show();
                    upcomming_btn_prg.setVisibility(View.GONE);
                    btn_upload_up_img.setVisibility(View.VISIBLE);

                }
            });
        }else {
            btn_upload_up_img.setVisibility(View.VISIBLE);

            upcomming_btn_prg.setVisibility(View.GONE);
            Toast.makeText(getContext(),"Select image",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification (notification).enqueue (new Callback<PushNotification>() {
            @Override
            public void onResponse (Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(  getContext(),  "success", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText( getContext(),  "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure (Call<PushNotification> call, Throwable t) {
                Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }


    public void setImageDetails(){
        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference(FirebasePath.UPCOMMING);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                UpCommingModel upcommingModel = snapshot.getValue(UpCommingModel.class);
                if (upcommingModel != null) {
                    Glide.with(getContext()).load(upcommingModel.getImage()).into(up_img);
                    edt_des_up_img.setText(upcommingModel.getDescription());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        pause=false;
    }
}