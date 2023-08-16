package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.R;

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

        holder.designation.setText(model.getDesignation());

        holder.persuingyr.setText("Persuing : "+model.getPersuing());
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "CommunityModel.xml")
    public static class CommunityProfileViewholder extends RecyclerView.ViewHolder {
        TextView name, designation, persuingyr;
        public CommunityProfileViewholder(@NotNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.community_profile_name_txt);
            designation= itemView.findViewById(R.id.community_profile_designation_txt);
            persuingyr = itemView.findViewById(R.id.community_profile_year_txt);
        }
    }
}
