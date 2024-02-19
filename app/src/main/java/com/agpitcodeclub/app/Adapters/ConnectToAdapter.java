package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.fragments.ConnectToMsg;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ConnectToAdapter extends RecyclerView.Adapter< ConnectToAdapter.CommunityProfileViewholder>{
    Context context;
    String Userid;
    ArrayList<CommunityModel> list;
    public ConnectToAdapter(Context context, ArrayList<CommunityModel> list,String Userid) {
        this.context =context;
        this.list=list;
        this.Userid=Userid;
    }



    @NotNull
    @Override
    public ConnectToAdapter.CommunityProfileViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.connect_to_item,parent,false);
        return  new ConnectToAdapter.CommunityProfileViewholder(v);
    }




    @Override
    public void onBindViewHolder(@NotNull ConnectToAdapter.CommunityProfileViewholder holder, int position)
    {
        final DatabaseReference[] mDatabase = new DatabaseReference[1];
        CommunityModel model = list.get(position);
        Glide.with(context).load(model.getProfile()).into(holder.profileImg);
        if(model.getDesignation().length()>15||model.getDesignation().equals(FirebasePath.REVENUE_MANAGER.substring(2))){
            holder.designation.setTextSize(13);
        }

        holder.designation.setText(model.getDesignation().substring(2));


        holder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gt="Hey \uD83D\uDC4B\uD83C\uDFFB this is "+ model.getName().split(" ")[0]+" ask me anything i will guide you :) ";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                HashMap<String,String> intimsg = new HashMap<>();
                intimsg.put("msg",gt);
                intimsg.put("date", timeFormat.format(new Date())+"\t"+dateFormat.format(new Date()));

                Intent intent = new Intent(context, ConnectToMsg.class);
                intent.putExtra("ImageUrl",model.getProfile());
                intent.putExtra("username",model.getName());
                intent.putExtra("User_Des",model.getDesignation());
                String chatpath="";
                if(model.getDesignation()==FirebasePath.DEVELOPER||model.getDesignation().equals(FirebasePath.DEVELOPER)){
                   chatpath =FirebasePath.DEVELOPER+"/"+model.getUserid()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;


                }else{
                    mDatabase[0] = FirebaseDatabase.getInstance().getReference().child(FirebasePath.COMMUNITY).child(model.getDesignation()).child(FirebasePath.CONNECT_TO_CHATS);
                    chatpath =model.getDesignation()+"/"+FirebasePath.CONNECT_TO_CHATS+"/"+Userid;
                }
                intent.putExtra("chatpath",chatpath);
                context.startActivity(intent);





                Toast.makeText(context,Userid,Toast.LENGTH_LONG).show();

            }
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "CommunityModel.xml")
    public static class CommunityProfileViewholder extends RecyclerView.ViewHolder {
        TextView  designation;
        ImageView profileImg;

        public CommunityProfileViewholder(@NotNull View itemView)
        {
            super(itemView);
            designation= itemView.findViewById(R.id.connect_to_txt);
            profileImg=itemView.findViewById(R.id.connect_to_img);
        }
    }
}
