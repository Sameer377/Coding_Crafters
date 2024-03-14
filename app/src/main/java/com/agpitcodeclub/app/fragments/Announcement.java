package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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

import com.agpitcodeclub.app.Adapters.ConnectToAdapter;
import com.agpitcodeclub.app.Adapters.MsgAdapter;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
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

public class Announcement extends Fragment {
    //notification

  
    String Userid=null;



    private LinearProgressIndicator ann_prg;
    RecyclerView inbox_recycle;
    FloatingActionButton fb;
    public Announcement() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_announcement, container, false);
        fb=view.findViewById(R.id.fab_send_msg);
        recyclerView=view.findViewById(R.id.recycle_connect_to);

        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = null;
        des = prefs.getString(Credentials.USER_DESIGNATION, null);

          if(des==null || des.equals("h_Member") ){
            des="";
            fb.hide();
        }else{
              fb.show();
          }


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), SendMsgActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // Apply the animation


            }
        });
        initUi(view);
        return view;
    }
    private DatabaseReference databaseReference;
    String designation="";
    private void initUi(View view) {
        inbox_recycle=view.findViewById(R.id.inbox_recycle);
        ann_prg=view.findViewById(R.id.ann_prg);
        ann_prg.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        inbox_recycle.setHasFixedSize(true);
        inbox_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();


        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        Userid = prefs.getString(Credentials.USER_ID, null);
        designation = prefs.getString(Credentials.USER_DESIGNATION, null);
        fetchData();
        loadConnectToInList ();




    }
    private ArrayList<MsgModel> list;
    private MsgAdapter adapter;
    private ConnectToAdapter adapter1;
    void fetchData() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"NotifyDataSetChanged"})
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    try {

                        list.clear();
                        adapter = new MsgAdapter(getContext(), list);
                        inbox_recycle.setAdapter(adapter);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {

                                MsgModel user = dataSnapshot.getValue(MsgModel.class);
                                list.add(0,user);
                        }catch (Exception e){
                        }

                    }
                    adapter.notifyAll();
                        ann_prg.setVisibility(View.GONE);
                }catch(Exception e){
                        ann_prg.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    ann_prg.setVisibility(View.GONE);

                }
            });

    }


    private ArrayList<CommunityModel> list1;

    private RecyclerView recyclerView;
    void loadConnectToInList (){

        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list1=new ArrayList<>();
        adapter1 = new ConnectToAdapter(getContext(),list1,Userid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CommunityModel user = dataSnapshot.getValue(CommunityModel.class);
                    user.setDesignation(dataSnapshot.getKey().toString());
                    System.out.println("Node1 : "+dataSnapshot.getKey().toString()+"\n"+dataSnapshot.getKey().equals(FirebasePath.DEVELOPER));
                    Log.v("tag","Name : "+user.getName()+"\nDesignation : "+user.getDesignation());

                    if (dataSnapshot.getKey().equals(FirebasePath.DEVELOPER)){
                        DatabaseReference innerChild = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(dataSnapshot.getKey());
                        innerChild.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot1) {

                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                    CommunityModel user1 = dataSnapshot1.getValue(CommunityModel.class);
                                    user1.setUserid(dataSnapshot1.getKey().toString());
                                    user1.setDesignation(dataSnapshot.getKey().toString());
                                        System.out.println("1 Names : "+user1.getName()+"\n");
                                            list1.add(user1);


                                }
                                adapter1.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });


                    }else if(!dataSnapshot.getKey().equals(FirebasePath.MEMBER)){
                                list1.add(user);


                    }

                }

                try {
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                }
                System.out.println("\n.............................................\n");
                for (CommunityModel l:list1) {
                    System.out.println("Names : "+l.getName()+"\n");
                }
            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(getContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }




}