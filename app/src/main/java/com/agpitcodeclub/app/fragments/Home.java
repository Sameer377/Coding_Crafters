package com.agpitcodeclub.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.agpitcodeclub.app.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    private ImageSlider imageSlider;

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
}