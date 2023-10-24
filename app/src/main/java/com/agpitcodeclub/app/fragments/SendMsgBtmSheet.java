package com.agpitcodeclub.app.fragments;

import static com.agpitcodeclub.app.utils.FirebasePath.FCM_TOPIC;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.agpitcodeclub.app.Adapters.FileTime;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.ApiUtilities;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.agpitcodeclub.app.utils.NotificationData;
import com.agpitcodeclub.app.utils.PushNotification;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMsgBtmSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    TextInputEditText tf_title_msg,tf_msg,tf_msg_loc;
    AppCompatButton btn_sendmsg;
    ImageView close_btm_nav;
    public static SendMsgBtmSheet newInstance() {
        return new SendMsgBtmSheet();
    }
    @Nullable @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC);
        return inflater.inflate(R.layout.activity_send_messege, container, false);
    }
    @Override public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tf_title_msg=view.findViewById(R.id.tf_title_msg);
        tf_msg=view.findViewById(R.id.tf_msg);
        btn_sendmsg=view.findViewById(R.id.btn_sendmsg);
        tf_msg_loc=view.findViewById(R.id.tf_msg_loc);
        close_btm_nav=view.findViewById(R.id.close_btm_nav);
        close_btm_nav.setOnClickListener(this);
        btn_sendmsg.setOnClickListener(this);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sendmsg:
                sendMsg();
                break;
            case R.id.close_btm_nav:
                dismiss();
                break;
        }
    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }
    private void sendMsg() {
        MsgModel model = new MsgModel(tf_title_msg.getText().toString().trim(),tf_msg.getText().toString().trim(),tf_msg_loc.getText().toString().trim(),"");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        databaseReference.child(new FileTime().getFileTime()).setValue(model);
        PushNotification notification = new PushNotification(new NotificationData(tf_title_msg.getText().toString().trim(),tf_msg.getText().toString().trim()),FCM_TOPIC);
        sendNotification(notification);
        sendSMS("8999596143","hello");
    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification (notification).enqueue (new Callback<PushNotification>() {
            @Override
            public void onResponse (Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(  getContext(),  "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),  "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure (Call<PushNotification> call, Throwable t) {
                Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

}