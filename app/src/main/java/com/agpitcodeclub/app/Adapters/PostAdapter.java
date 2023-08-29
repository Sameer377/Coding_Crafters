package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.PostModel;
import com.agpitcodeclub.app.R;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter< PostAdapter.PostViewholder>{
    Context context;
    ArrayList<PostModel> list;
    public PostAdapter(Context context, ArrayList<PostModel> list) {
        this.context =context;
        this.list=list;
    }



    @NotNull
    @Override
    public PostViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_card,parent,false);
        return  new PostViewholder(v);
    }
    boolean show = true;
    @Override
    public void onBindViewHolder(@NotNull PostViewholder holder, int position)
    {

        PostModel model = list.get(position);
       holder.txt_post_title.setText(model.getTitle());
       holder.post_description_txt.setText(model.getDescription());
       holder.post_uploaded_date.setText(model.getPostuploadedon());
       if(model.getDescription().length()>100){

           holder.txt_read_more.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                    if (show){
                        ViewGroup.LayoutParams params = holder.post_description_txt.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        holder.post_description_txt.setLayoutParams(params);
                        show=false;
                    }else {
                        ViewGroup.LayoutParams params = holder.post_description_txt.getLayoutParams();
                        params.height = 200;
                        holder.post_description_txt.setLayoutParams(params);
                        show=true;
                    }
                       }
           });
       }

       if(model.getImglist()!=null){
           ArrayList<SlideModel> imgurls=new ArrayList<>();
           for (String ur: model.getImglist().split(">>>")) {
               imgurls.add(new SlideModel(ur, ScaleTypes.CENTER_CROP));
           }
           holder.post_imgslider.setImageList(imgurls);
       }


    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "CommunityModel.xml")
    public static class PostViewholder extends RecyclerView.ViewHolder {
        TextView txt_post_title, post_description_txt,txt_read_more,btn_feedback_txt,post_uploaded_date;
        ImageSlider post_imgslider;

        public PostViewholder(@NotNull View itemView)
        {
            super(itemView);
            post_uploaded_date=itemView.findViewById(R.id.post_uploaded_date);
            txt_post_title = itemView.findViewById(R.id.txt_post_title);
            post_imgslider= itemView.findViewById(R.id.post_imgslider);
            post_description_txt = itemView.findViewById(R.id.post_description_txt);
            txt_read_more=itemView.findViewById(R.id.txt_read_more);
            btn_feedback_txt=itemView.findViewById(R.id.btn_add_addmember);
        }
    }
}

