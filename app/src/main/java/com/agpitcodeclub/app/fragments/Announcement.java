package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agpitcodeclub.app.Adapters.MsgAdapter;
import com.agpitcodeclub.app.Adapters.PostAdapter;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class Announcement extends Fragment {

    RecyclerView inbox_recycle;
    FloatingActionButton fb;
    public Announcement() {
    }

    public static Announcement newInstance(String param1, String param2) {
        Announcement fragment = new Announcement();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(),"Currently not available !",Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_announcement, container, false);
        fb=view.findViewById(R.id.fab_send_msg);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(),SendMessege.class);
//                startActivity(intent);

                SendMsgBtmSheet addPhotoBottomDialogFragment =
                        SendMsgBtmSheet.newInstance();
                addPhotoBottomDialogFragment.show( getActivity().getSupportFragmentManager(),
                        SendMsgBtmSheet.TAG);
                Toast.makeText(getContext(),"DB Error",Toast.LENGTH_LONG).show();

            }
        });
        initUi(view);
        return view;
    }
    private DatabaseReference databaseReference;

    private void initUi(View view) {
        inbox_recycle=view.findViewById(R.id.inbox_recycle);
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        inbox_recycle.setHasFixedSize(true);
        inbox_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();

        inbox_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fb.getVisibility() == View.VISIBLE) {
                    fb.hide();
                } else if (dy < 0 && fb.getVisibility() !=View.VISIBLE) {
                    fb.show();
                }
            }
        });

        fetchData();
    }
    private ArrayList<MsgModel> list;
    private MsgAdapter adapter;
    void fetchData() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    try {

                        list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MsgModel user = dataSnapshot.getValue(MsgModel.class);
                        list.add(user);
                    }
//                loadingprg.setVisibility(View.GONE);
                        Collections.reverse(list);
                        adapter = new MsgAdapter(getContext(), list);
                    inbox_recycle.setAdapter(adapter);
//                        inbox_recycle.scrollToPosition(list.size()-1);

                        adapter.notifyDataSetChanged();
                }catch(Exception e){
                    Toast.makeText(getContext(),"Error : "+e,Toast.LENGTH_SHORT).show();
                }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

    }

}