package com.agpitcodeclub.app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.AddMember;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Community extends Fragment {

    private FloatingActionButton addmember;


    public Community() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   = inflater.inflate(R.layout.fragment_community, container, false);
        initui(view);
        return view;
    }

    private void initui(View view) {
        addmember=view.findViewById(R.id.add_member);

        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddMember.class);
                startActivity(intent);
            }
        });

    }
}