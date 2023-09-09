package com.agpitcodeclub.app.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.arch.core.executor.TaskExecutor;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.agpitcodeclub.app.Dashboard;
import com.agpitcodeclub.app.MainActivity;
import com.agpitcodeclub.app.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    private ImageSlider imageSlider;
    private TextView txt_intro;
    private ImageView up_img;
    private Uri fileUrl;

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
        imageSlider=view.findViewById(R.id.intro_img_slider);
        txt_intro=view.findViewById(R.id.txt_intro);
        up_img=view.findViewById(R.id.up_img);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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


        return view;
    }


    public String intentType;

    private void pickImage() {
        Toast.makeText(getContext(),"w",Toast.LENGTH_SHORT).show();
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
            case R.id.up_img:
                pickImage();
                break;
        }
    }
}