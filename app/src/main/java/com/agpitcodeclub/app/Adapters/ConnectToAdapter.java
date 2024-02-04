package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConnectToAdapter extends RecyclerView.Adapter< ConnectToAdapter.CommunityProfileViewholder>{
    Context context;
    ArrayList<CommunityModel> list;
    public ConnectToAdapter(Context context, ArrayList<CommunityModel> list) {
        this.context =context;
        this.list=list;
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

        CommunityModel model = list.get(position);
        Glide.with(context).load(model.getProfile()).into(holder.profileImg);
        if(model.getDesignation().length()>15||model.getDesignation().equals(FirebasePath.REVENUE_MANAGER.substring(2))){
            holder.designation.setTextSize(13);
        }

        holder.designation.setText(model.getDesignation());
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
