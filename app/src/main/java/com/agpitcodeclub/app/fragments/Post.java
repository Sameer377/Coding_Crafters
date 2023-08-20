package com.agpitcodeclub.app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.UploadPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Post extends Fragment {

    private FloatingActionButton addpost;

    public Post() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_post, container, false);

        initUi();
        return mainView;
    }

    private void initUi() {
        addpost=mainView.findViewById(R.id.fab_add_post);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadPost.class);
                startActivity(intent);
            }
        });
    }
}