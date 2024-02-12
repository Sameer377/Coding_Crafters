package com.agpitcodeclub.app.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommunityProfileAdapter extends RecyclerView.Adapter< CommunityProfileAdapter.CommunityProfileViewholder>{
Context context;
ArrayList<CommunityModel> list;
    public CommunityProfileAdapter(Context context, ArrayList<CommunityModel> list) {
   this.context =context;
   this.list=list;
    }



    @NotNull
    @Override
    public CommunityProfileViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_profile_card,parent,false);
        return  new CommunityProfileViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull CommunityProfileViewholder holder, int position)
    {

        CommunityModel model = list.get(position);
        holder.name.setText(model.getName());
        System.out.println("Login Address : "+model.getProfile());
        Glide.with(context).load(model.getProfile()).into(holder.profileImg);
        holder.designation.setText(model.getDesignation());

        holder.persuingyr.setText("Persuing : "+model.getPersuing());


        holder.rel_info_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.social_lin.getVisibility()==View.GONE){
                    holder.social_lin.setVisibility(View.VISIBLE);
                    try {
                        ObjectAnimator slideDown = ObjectAnimator.ofFloat(holder.social_lin, "translationY", -40, 0);
                        slideDown.setDuration(500); // Adjust the duration as needed
                        slideDown.start();

                    }catch (Exception e){

                    }
                    // Start the animation

                } else {

                    try {
                        ObjectAnimator slideDown = ObjectAnimator.ofFloat(holder.social_lin, "translationY", 40, 0);
                        slideDown.setDuration(500); // Adjust the duration as needed

                        // Start the animation
                        slideDown.start();
                    }catch(Exception e){

                    }
                    holder.social_lin.setVisibility(View.GONE);
                }
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
        TextView name, designation, persuingyr;
        ImageView profileImg;

        RelativeLayout rel_info_cd,rel_main_comm;
        LinearLayout social_lin;
        public CommunityProfileViewholder(@NotNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.community_profile_name_txt);
            designation= itemView.findViewById(R.id.community_profile_designation_txt);
            rel_info_cd= itemView.findViewById(R.id.rel_info_cd);
            rel_main_comm= itemView.findViewById(R.id.rel_main_comm);
            social_lin= itemView.findViewById(R.id.social_lin);
            persuingyr = itemView.findViewById(R.id.community_profile_year_txt);
            profileImg=itemView.findViewById(R.id.item_card_profile_img);
        }
    }
}
