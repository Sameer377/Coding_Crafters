package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgAdapterViewholder>{
    Context context;
    ArrayList<MsgModel> list;
    public MsgAdapter(Context context, ArrayList<MsgModel> list) {
        this.context =context;
        this.list=list;
    }



    @NotNull
    @Override
    public MsgAdapterViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.msg_item,parent,false);
        return  new MsgAdapterViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull MsgAdapterViewholder holder, int position)
    {

        MsgModel model = list.get(position);
        holder.msg_title.setText(model.getTitle());
        holder.msg_location.setText(model.getLocation());
        holder.msg_content.setText(model.getMsg());
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "MsgModel.xml")
    public static class MsgAdapterViewholder extends RecyclerView.ViewHolder {
        TextView msg_title, msg_location, msg_content;
        ImageView profileImg;
        public MsgAdapterViewholder(@NotNull View itemView)
        {
            super(itemView);
            msg_title = itemView.findViewById(R.id.title_msg);
            msg_location = itemView.findViewById(R.id.msg_location);
            msg_content = itemView.findViewById(R.id.msg_content);
        }
    }
}