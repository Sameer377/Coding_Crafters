package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Adapters.CommunityProfileAdapter;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.AddMember;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Community extends Fragment {

    private FloatingActionButton addmember;
    private RecyclerView recyclerView;
    private CommunityProfileAdapter adapter; // Create Object of the Adapter class
    private DatabaseReference databaseReference;
    private ArrayList<CommunityModel> list;
    public Context context;
    private LinearProgressIndicator community_prg;

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
        community_prg=view.findViewById(R.id.community_prg);
        recyclerView.setLayoutFrozen(true);
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddMember.class);
                startActivity(intent);
            }
        });


        
        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = prefs.getString(Credentials.USER_DESIGNATION, null);

        if(des==null){
            des="";
        }

        if (des.equals(FirebasePath.PRESIDENT)) {
            addmember.setVisibility(View.VISIBLE);
        }else {
            addmember.setVisibility(View.GONE);

        }

    }

    void loadContentInList(){
        community_prg.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list=new ArrayList<>();
        adapter = new CommunityProfileAdapter(context,list);
        recyclerView.setAdapter(adapter);
        
        String t="DB status";
        ArrayList<CommunityModel> listinner=new ArrayList<>();
        Log.v(t,"Entering.......");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CommunityModel user = dataSnapshot.getValue(CommunityModel.class);
                    user.setDesignation(dataSnapshot.getKey().substring(2));
                    Log.v(t,"Name : "+user.getName()+"\nDesignation : "+user.getDesignation());
                    System.out.println("Node1 : "+dataSnapshot.getKey().toString()+"\n"+dataSnapshot.getKey().equals(FirebasePath.DEVELOPER));
                    if (dataSnapshot.getKey().equals(FirebasePath.DEVELOPER)||dataSnapshot.getKey().equals(FirebasePath.MEMBER)){
                        DatabaseReference innerChild = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(dataSnapshot.getKey());
                        innerChild.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot1) {

                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                    CommunityModel user1 = dataSnapshot1.getValue(CommunityModel.class);
                                    user1.setDesignation(dataSnapshot.getKey().substring(2));
//                                    System.out.println("Name : "+user1.getName());
//                                    System.out.println("des2 : "+user1.getDesignation());

                                    HashMap<String,String> url = new HashMap<>();
                                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.child(FirebasePath.SOCIAL).getChildren()){
                                        url.put(dataSnapshot2.getKey().toString(),dataSnapshot2.getValue().toString());
                                    }
                                    user1.setSocialurls(url);
                                    list.add(user1);

                                    System.out.println("list status : "+list.contains(user1));

                                }
                                adapter.notifyDataSetChanged();
                                community_prg.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                community_prg.setVisibility(View.GONE);

                            }
                        });


                    }else {
                        HashMap<String,String> url = new HashMap<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.child(FirebasePath.SOCIAL).getChildren()){
                            url.put(dataSnapshot1.getKey().toString(),dataSnapshot1.getValue().toString());
                        }
                        user.setSocialurls(url);
                        list.add(user);
                        community_prg.setVisibility(View.GONE);

                    }

                }
                adapter.notifyDataSetChanged();
                System.out.println("\n.............................................\n");
                for (CommunityModel l:list) {
                    System.out.println("Names : "+l.getName()+"\n");
                }

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(context,"Database error",Toast.LENGTH_SHORT).show();
                community_prg.setVisibility(View.GONE);
            }
        });
    }

}