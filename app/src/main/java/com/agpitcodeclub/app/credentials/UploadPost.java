package com.agpitcodeclub.app.credentials;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agpitcodeclub.app.Adapters.FileTime;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.Models.PostModel;
import com.agpitcodeclub.app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class UploadPost extends AppCompatActivity  implements View.OnClickListener, EasyPermissions.PermissionCallbacks{
    private RelativeLayout rel_upload_post_btn;
    private ArrayList<String> imgList;
    private MaterialButton upload_btn_post;
    private EditText edt_post_title,edt_post_desc;
    private String uplodingdate;
    ArrayList arrayListFileObjects=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        initUi();
        initListener();
    }

    private void initListener() {
        rel_upload_post_btn.setOnClickListener(this);
        upload_btn_post.setOnClickListener(this);
    }

    private void initUi() {
        rel_upload_post_btn=findViewById(R.id.rel_upload_post_btn);
        upload_btn_post=findViewById(R.id.upload_btn_post);
        edt_post_desc=findViewById(R.id.edt_post_desc);
        edt_post_title=findViewById(R.id.edt_post_title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rel_upload_post_btn:

                uploadPost();
                break;
            case R.id.upload_btn_post:
                pushContent(getArrayListFileObjects());
                break;
        }
    }

    void toast(String s){
        Toast.makeText(UploadPost.this,s,Toast.LENGTH_SHORT).show();
    }

    private void uploadPost() {
        pickImg();

    }
    DatabaseReference databaseReference;
    private  StorageReference storageReference;
    private String fileKey=null;


    public StorageReference getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    PostModel postModel;

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String imgUrls="";
    private void pushContent(ArrayList arrayList) {
        fileKey= new FileTime().getFileTime();
        databaseReference= FirebaseDatabase.getInstance().getReference(FirebasePath.POST).child(fileKey);

        LocalDate currentDate = LocalDate.now();

        // Format the date using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        // Extract day, month, and year
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        uplodingdate=day+" / "+month+" / "+year;
        postModel=new PostModel(edt_post_title.getText().toString().trim(),edt_post_desc.getText().toString().trim(),uplodingdate);
//        databaseReference.setValue(postModel);

        toast("entered in push : "+arrayList.size());
        imgUrls="";
        for(int i=0; i<arrayList.size();i++)
        {

            Uri fileuri=(Uri)arrayList.get(i);
            String filename=getfilenamefromuri(fileuri);



            storageReference = FirebaseStorage.getInstance().getReference(FirebasePath.POST+"/"+fileKey+"/"+filename);
            setStorageReference(storageReference);
            StorageReference st=storageReference;
            toast("Executing");
            storageReference.putFile(fileuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            toast("Entered in the success");
                            st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    toast("Uploaded");
                                    Log.i("TAG","File path ..............:"+getStorageReference());
                                    String tempFile=filename.replaceAll("[+-_,.*/%$#@!&^]","");
                                    toast(filename);
                                    if(imgUrls.equals("")){
                                        imgUrls=uri.toString();
                                        setImgUrls(uri.toString());
                                    }else{
                                        setImgUrls(getImgUrls()+">>>"+uri.toString());

                                    }
                                    postModel.setImglist(imgUrls);
                                    databaseReference.child("imglist").setValue(getImgUrls());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure( Exception e) {
                                    Log.i("TAG","File path ..............:"+e);

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( Exception e) {


                }
            });


        }



        databaseReference.setValue(postModel);

    }


    public ArrayList getArrayListFileObjects() {
        return arrayListFileObjects;
    }

    public void setArrayListFileObjects(ArrayList arrayListFileObjects) {
        this.arrayListFileObjects = arrayListFileObjects;
    }

    private void pickImg() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();

                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Please Select Multiple Files"), 101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        arrayListFileObjects=new ArrayList();
        if(requestCode==101 && resultCode==RESULT_OK)
        {
            toast(String.valueOf(data.getClipData()));

            if(data.getClipData()!=null)
            {
                for(int i=0; i<data.getClipData().getItemCount();i++)
                {
                    arrayListFileObjects.add(i,data.getClipData().getItemAt(i).getUri());
                    Uri fileuri=data.getClipData().getItemAt(i).getUri();
                    String filename=getfilenamefromuri(fileuri);


                }
                String fff1="",filename="l";
                setArrayListFileObjects(arrayListFileObjects);
                for(int i=0; i<arrayListFileObjects.size();i++) {
                    Uri fileuri = (Uri) arrayListFileObjects.get(i);
                    filename = getfilenamefromuri(fileuri);
                    fff1=fff1+filename+"\n";
                }
//                tv_filename1.setText(fff1);
//                tv_filename1.setMovementMethod(new ScrollingMovementMethod());
            }
            toast("Arrobj : "+arrayListFileObjects.size());

        }


    }

    public String getfilenamefromuri(Uri filepath)
    {
        String result = null;
        if (filepath.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = filepath.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



}