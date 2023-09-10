package com.agpitcodeclub.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agpitcodeclub.app.R;

public class CheckInternet extends Fragment {


    public CheckInternet() {
        // Required empty public constructor
    }

    public static CheckInternet newInstance(String param1, String param2) {
        CheckInternet fragment = new CheckInternet();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_internet, container, false);
    }
}