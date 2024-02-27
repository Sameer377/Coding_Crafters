package com.agpitcodeclub.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Models.ConnectToMsgModel;
import com.agpitcodeclub.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConnectToChatsAdapter extends RecyclerView.Adapter< ConnectToChatsAdapter.ConnectToChatsViewholder>{
    Context context;

    public int getINTENT_ID() {
        return INTENT_ID;
    }

    public void setINTENT_ID(int INTENT_ID) {
        this.INTENT_ID = INTENT_ID;
    }

    int INTENT_ID;
    public void setList(ArrayList<ConnectToMsgModel> list) {
        this.list = list;
    }

    ArrayList<ConnectToMsgModel> list;
    public ConnectToChatsAdapter(Context context, ArrayList<ConnectToMsgModel> list) {
        this.context =context;
        this.list=list;
    }

    public ConnectToChatsAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public ConnectToChatsAdapter.ConnectToChatsViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case 0 : v = LayoutInflater.from(context).inflate(R.layout.connect_to_msg_receiver_layout,parent,false); break;
            default: v = LayoutInflater.from(context).inflate(R.layout.connect_to_msg_sender_layout,parent,false);
        }

        return  new ConnectToChatsAdapter.ConnectToChatsViewholder(v);
    }




    @Override
    public void onBindViewHolder(@NotNull ConnectToChatsAdapter.ConnectToChatsViewholder holder, int position)
    {
        ConnectToMsgModel connectToMsgModel = list.get(position);
        holder.message.setText(connectToMsgModel.getMsg());
        holder.timesstamp.setText(connectToMsgModel.getDate());
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if(list.get(position).getUser()=="sender" || list.get(position).getUser().equals("sender")){
            if(INTENT_ID==0){
                viewType = 1;

            }else{
                viewType = 0;
            }
        }else{
            if(INTENT_ID==0){
                viewType = 0;

            }else{
                viewType = 1;
            }
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Sub Class to create references of the views in Crad
    // view (here "ConnectToMsgModel.xml")
    public static class ConnectToChatsViewholder extends RecyclerView.ViewHolder {
        TextView message,timesstamp;

        public ConnectToChatsViewholder(@NotNull View itemView)
        {
            super(itemView);
            message= itemView.findViewById(R.id.msg_txt);
            timesstamp=itemView.findViewById(R.id.msg_timestamp);
        }
    }
}
