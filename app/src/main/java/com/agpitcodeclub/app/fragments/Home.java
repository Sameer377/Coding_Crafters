package com.agpitcodeclub.app.fragments;

import static android.app.Activity.RESULT_OK;
import static com.agpitcodeclub.app.utils.FirebasePath.FCM_TOPIC;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.net.MalformedURLException;
import java.net.URL;
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
    public static final int UPI_PAYMENT_REQUEST_CODE = 101;
    private ImageSlider imageSlider;
    private AppCompatButton paymentbtn;
    private TextView txt_intro;
    private TextView txt_logo,gformtxt;
    private ImageView btn_pickimg;
    private AppCompatButton btn_upload_up_img,gfrom_url_upload,gfrom_url_delete;
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

    private View fview;
    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState) {
        fview=view;
        initUi(view);
        sliderImage();
    }

    private void initUi(View view) {
        txt_logo=view.findViewById(R.id.txt_logo);
        imageSlider=view.findViewById(R.id.intro_img_slider);
        txt_intro=view.findViewById(R.id.txt_intro);
        up_img=view.findViewById(R.id.up_img);
        paymentbtn=view.findViewById(R.id.paymentbtn);
        btn_upload_up_img=view.findViewById(R.id.btn_upload_up_img);
        btn_pickimg=view.findViewById(R.id.btn_pickimg);
        edt_des_up_img=view.findViewById(R.id.edt_des_up_img);
        btn_pickimg.setOnClickListener(this);
        btn_upload_up_img.setOnClickListener(this);
        upcomming_btn_prg=view.findViewById(R.id.upcomming_btn_prg);
        gfrom_url_upload=view.findViewById(R.id.gfrom_url_upload);
        gformtxt=view.findViewById(R.id.gformtxt);
        gfrom_url_delete=view.findViewById(R.id.gfrom_url_delete);
        paymentbtn.setOnClickListener(this);
        setImageDetails();

        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = prefs.getString(Credentials.USER_DESIGNATION, null);

        if(des==null){
            des="";
        }

        if (des.equals(FirebasePath.PRESIDENT)) {
            btn_upload_up_img.setVisibility(View.VISIBLE);
            btn_pickimg.setVisibility(View.VISIBLE);
            gform(true);
        }else {
            btn_upload_up_img.setVisibility(View.GONE);
            btn_pickimg.setVisibility(View.GONE);
            edt_des_up_img.setEnabled(false);
            gform(false);
        }

        fview.findViewById(R.id.main_about).setOnClickListener(view1 -> {
            about();
        });


    }

    private void about() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_about);

        ImageView btnClose = dialog.findViewById(R.id.dialocback);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        dialog.show();
    }

    @SuppressLint("RestrictedApi")
    void gform(boolean ispresident){

        gfrom_url_upload.setOnClickListener((view -> {
            showCustomDialog();
        }));

        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference().child("gform");
        if(ispresident){
            fview.findViewById(R.id.gformcontrols).setVisibility(View.VISIBLE);
        }else {
            fview.findViewById(R.id.gformcontrols).setVisibility(View.GONE);
        }
        database1.child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    fview.findViewById(R.id.lin_gform).setVisibility(View.VISIBLE);
                    // Do something with the retrieved value
                    gfrom_url_delete.setVisibility(View.VISIBLE);
                    fview.findViewById(R.id.open_web).setOnClickListener(view -> {
                        Intent intent = new Intent(getContext(),GForm.class);
                        intent.putExtra("url",value);
                        startActivity(intent);
                    });

                }else{
                    gfrom_url_delete.setVisibility(View.GONE);
                    if(!ispresident){
                        fview.findViewById(R.id.lin_gform).setVisibility(View.GONE);
                    }else{
                        fview.findViewById(R.id.lin_gform).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database1.child("des").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    // Do something with the retrieved value
                    gformtxt.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        gfrom_url_delete.setOnClickListener((view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Warning !")
                    .setMessage("Are you sure you want to delete ?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked Yes, perform your action here
                            // For example, you can delete a record, submit a form, etc.
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("gform");
                            database.setValue(null);

                            dialog.dismiss(); // Close the dialog
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked No, do nothing or handle accordingly
                            dialog.dismiss(); // Close the dialog
                        }
                    })
                    .show();

        }));

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

    private void showCustomDialog() {




        // Inflate the custom layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View customLayout = inflater.inflate(R.layout.gform_dialog, null);

        // Find the EditText views in the custom layout
        EditText editText1 = customLayout.findViewById(R.id.editText1);
        EditText editText2 = customLayout.findViewById(R.id.editText2);



        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(customLayout)
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle OK button click
                        String des = editText1.getText().toString().trim();
                        String url = editText2.getText().toString().trim();

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("gform");

                        if(!des.equals("") && !url.equals("")){
                            database.child("des").setValue(des);
                            database.child("url").setValue(url);
                            Toast.makeText(getContext(),"Uploaded",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getContext(),"Empty Values",Toast.LENGTH_SHORT).show();
                        }


                        // Process the entered text
                        // ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Cancel button click
                        dialog.dismiss();
                    }
                });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    Handler handler;
    private void displayTag() {
        String tag = "Coding Crafters";
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();



            for (int i = 0, delay = 300; i < 15; i++, delay += 100) {
                char c = tag.charAt(i);


                final int finalI = i; // Need to use final variable inside the inner class

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (finalI >= 6) {
                            try {
                            spannableStringBuilder.append(c);
                            spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.soft_red)),
                                    spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.NORMAL),
                                    spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                            }catch (Exception e){
                                Log.v("anim",""+e);
                            }
                        } else {
                            try {
                            spannableStringBuilder.append(c);
                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                                    spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                            }catch (Exception e){
                                Log.v("anim",""+e);
                            }


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

            case R.id.paymentbtn:
                makePayment();
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
                                 /*   PushNotification notification = new PushNotification(new NotificationData("UpcommingEvent",edt_des_up_img.getText().toString().trim()),FCM_TOPIC);
                                    sendNotification(notification);*/
                                    sendNotification( "UpcommingEvent",edt_des_up_img.getText().toString().trim(),imgUri);
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

    public void makePayment() {
        // Your existing code for initiating the UPI payment
        String upiId = "solapurevivek2003@okhdfcbank";
        String amount = "1.00";
        String transactionNote = "Payment for XYZ";
        String currencyCode = "INR";

        // Create UPI payment URI
     /*   EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(getActivity())
                .with(PaymentApp.ALL)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setPayeeMerchantCode(payeeMerchantCode)
                .setDescription(description)
                .setAmount(amount);
        // END INITIALIZATION

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }*/

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

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Payment successful
                String response = data.getStringExtra("response");
                // Handle the payment response as needed
                Toast.makeText(getContext(), "Payment successful", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Payment was canceled by the user
                Toast.makeText(getContext(), "Payment canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Payment failed
                String response = data.getStringExtra("response");
                // Handle the payment failure
                Toast.makeText(getContext(), "Payment failed", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void sendNotification(String title, String des,String imageUri) {

        PushNotification notification = null;
        try {
            notification = new PushNotification(new NotificationData("Upcomming Even",title,new URL(imageUri)),FCM_TOPIC);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ApiUtilities.getClient().sendNotification (notification).enqueue (new Callback<PushNotification>() {
            @Override
            public void onResponse (Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(getContext(),  "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),  "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure (Call<PushNotification> call, Throwable t) {
                Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }
/*
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
    }*/


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
    }
}