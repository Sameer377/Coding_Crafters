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

import com.agpitcodeclub.app.Adapters.PostAdapter;
import com.agpitcodeclub.app.FirebasePath;
import com.agpitcodeclub.app.Models.PostModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.credentials.UploadPost;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Post extends Fragment {



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
        adapter = new PostAdapter(context,list);
        recyclerView.setAdapter(adapter);
        String t="DB status";
//        Toast.makeText(context,"Entering",Toast.LENGTH_SHORT).show();
        ArrayList<PostModel> listinner=new ArrayList<>();
        Log.v(t,"Entering.......");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel user = dataSnapshot.getValue(PostModel.class);
                    ArrayList<SlideModel> imgurls=new ArrayList<>();
                    DatabaseReference imgRef =  FirebaseDatabase.getInstance().getReference(FirebasePath.POST).child(dataSnapshot.getKey()).child(FirebasePath.POSTIMAGE);
                    imgRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshotInner) {
                            for(DataSnapshot imgsnap:snapshotInner.getChildren()){
                                imgurls.add(new SlideModel(imgsnap.getValue().toString(), ScaleTypes.CENTER_CROP));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                    user.setImgUri(imgurls);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
                System.out.println("\n.............................................\n");

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(context,"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }

  /*  private FloatingActionButton addpost;

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
    }*/
}