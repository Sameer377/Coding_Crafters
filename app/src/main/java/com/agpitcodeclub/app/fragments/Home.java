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
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/page1-f073e.appspot.com/o/demo%2FBest_Principal.jpg?alt=media&token=dd7ea6a2-ea1a-4028-b3f2-93be886c3d43", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/page1-f073e.appspot.com/o/demo%2Fprin.jpg?alt=media&token=2bd88912-c544-4829-a179-6a03b5ed4ac2", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/page1-f073e.appspot.com/o/demo%2Finfra.jpg?alt=media&token=e4a1cbe6-a15e-44f1-a5fa-5a1d225604b9", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/page1-f073e.appspot.com/o/demo%2Flab.jpg?alt=media&token=7dce08e1-aa67-4295-8a9b-fb7ab26f5ae3", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/page1-f073e.appspot.com/o/demo%2Fmec.jpg?alt=media&token=0cc17984-17d4-46b3-a347-a80e752186f7", ScaleTypes.FIT));
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