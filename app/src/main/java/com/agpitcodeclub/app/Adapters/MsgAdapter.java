package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

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
        View v = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false);
        return  new MsgAdapterViewholder(v);
    }

    boolean show = true;
    @Override
    public void onBindViewHolder(@NotNull MsgAdapterViewholder holder, int position)
    {
        MsgModel model = list.get(position);
        String extractedWords = extractWords(model.getMsg(), 12);
        Glide.with(context).load(model.getProfileimg()).into(holder.msg_profile_img);
        holder.msg_date.setText(model.getDate());
        holder.msg_time.setText(model.getTime());
        holder.designation.setText(model.getDesignation());

            if( model.getMsg_imguri()!=null ){
                Glide.with(context).load(model.getMsg_imguri()).into(holder.msg_attached_img);
                holder.img_cd.setVisibility(View.VISIBLE);
            }else{
                holder.img_cd.setVisibility(View.GONE);
            }



        if(model.getMsg().length()>100){
            holder.msg_content.setText(extractedWords+" ...");
            holder.msg_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (show){
                        holder.msg_content.setText(model.getMsg());
                        holder.txt_readmore.setText("show less");
                        show=false;
                    }else {
                        holder.msg_content.setText(extractedWords+" ...");
                        holder.txt_readmore.setText("show more");
                        show=true;
                    }
                }
            });
        }else{
            holder.msg_content.setText(model.getMsg());
            holder.txt_readmore.setVisibility(View.GONE);
        }


    }

    public static String extractWords(String text, int wordCount) {
        String[] words = text.split(" "); // Split the string into an array of words
        StringBuilder result = new StringBuilder(); // Create a StringBuilder to hold the desired words

        for (int i = 0; i < wordCount && i < words.length; i++) {
            result.append(words[i]).append(" "); // Add the word to the result
        }

        return result.toString().trim(); // Trim any extra whitespace and return the result
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "MsgModel.xml")
    public static class MsgAdapterViewholder extends RecyclerView.ViewHolder {
        TextView txt_readmore,msg_content,msg_date,msg_time,designation;
        private GifImageView msg_attached_img,msg_profile_img;
        CardView img_cd;
        ImageView profileImg;
        public MsgAdapterViewholder(@NotNull View itemView)
        {
            super(itemView);
//            msg_title = itemView.findViewById(R.id.title_msg);
//            msg_location = itemView.findViewById(R.id.msg_location);
            txt_readmore=itemView.findViewById(R.id.txt_readmore);
            msg_date=itemView.findViewById(R.id.msg_date);
            msg_time=itemView.findViewById(R.id.msg_time);
            img_cd=itemView.findViewById(R.id.img_cd);
            designation=itemView.findViewById(R.id.designation);
            msg_attached_img=itemView.findViewById(R.id.msg_attached_img);
            msg_profile_img=itemView.findViewById(R.id.msg_profile_img);
            msg_content = itemView.findViewById(R.id.txt_msg);
        }
    }
}