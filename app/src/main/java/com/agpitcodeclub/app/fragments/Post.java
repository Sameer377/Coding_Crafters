package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.*;
import com.agpitcodeclub.app.Adapters.PostAdapter;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.Models.PostModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.UploadPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;


public class Post extends Fragment {


    private LinearProgressIndicator loadingprg;
    private FloatingActionButton addpost;
    private FloatingActionButton addmember;
    private RecyclerView recyclerView;
    private PostAdapter adapter; // Create Object of the Adapter class
    private DatabaseReference databaseReference;
    private ArrayList<PostModel> list;
    public Context context;
    public Post() {
        // Required empty public constructor
    }

    public Post(Context context){
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   = inflater.inflate(R.layout.fragment_post, container, false);
        initui(view);
        loadContentInList();
        return view;
    }

    private void initui(View view) {
        recyclerView=view.findViewById(R.id.post_recycler);
//        recyclerView.setLayoutFrozen(true);

        loadingprg=view.findViewById(R.id.post_loading_prg);
        addpost=view.findViewById(R.id.fab_add_post);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadPost.class);
                startActivity(intent);
            }
        });

    }
    void loadContentInList(){
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.POST);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list=new ArrayList<>();

        String t="DB status";
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel user = dataSnapshot.getValue(PostModel.class);
                    list.add(user);
                }
                loadingprg.setVisibility(View.GONE);
                Collections.reverse(list);
                adapter = new PostAdapter(context,list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(context,"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}