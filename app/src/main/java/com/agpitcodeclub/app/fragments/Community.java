package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agpitcodeclub.app.Adapters.CommunityProfileAdapter;
import com.agpitcodeclub.app.FirebasePath;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.AddMember;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Community extends Fragment {

    private FloatingActionButton addmember;
    private RecyclerView recyclerView;
    private CommunityProfileAdapter adapter; // Create Object of the Adapter class
    private DatabaseReference databaseReference;
    private ArrayList<CommunityModel> list;
    public Context context;
    public Community() {
        // Required empty public constructor
    }

    public Community(Context context){
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   = inflater.inflate(R.layout.fragment_community, container, false);
        initui(view);
        loadContentInList();
        return view;
    }

    private void initui(View view) {
        addmember=view.findViewById(R.id.add_member);
        recyclerView=view.findViewById(R.id.community_list_profile);
        recyclerView.setLayoutFrozen(true);
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddMember.class);
                startActivity(intent);
            }
        });

    }

    void loadContentInList(){
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list=new ArrayList<>();
        adapter = new CommunityProfileAdapter(context,list);
        recyclerView.setAdapter(adapter);
        String t="DB status";
        Toast.makeText(context,"Entering",Toast.LENGTH_SHORT).show();
        Log.v(t,"Entering.......");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CommunityModel user = dataSnapshot.getValue(CommunityModel.class);
                    user.setDesignation(dataSnapshot.getKey().substring(2));
                    list.add(user);
                    Log.v(t,"Name : "+user.getName()+"\nDesignation : "+user.getDesignation());
                    Log.v(t,"Name : "+list.get(0).getName()+"\nDesignation : "+list.get(0).getDesignation());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(context,"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}