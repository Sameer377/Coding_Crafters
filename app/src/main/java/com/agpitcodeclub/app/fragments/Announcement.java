package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agpitcodeclub.app.Adapters.MsgAdapter;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Announcement extends Fragment {
    //notification

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAALRvzS04:APA91bH3O9LTYW7FNOWlSF2_4vY3jfUQ0qEVFYDg5kcwwK6CMW6wM6AyxHcu8JDzX1jmkfSIyz635qfjSXgU95KCffBzQCe3-ezeDDDSdzMQNih0CV1WYsyeo3o5ZyTOS8szxnKuswAr";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    private LinearProgressIndicator ann_prg;
    RecyclerView inbox_recycle;
    FloatingActionButton fb;
    public Announcement() {
    }

    public static Announcement newInstance(String param1, String param2) {
        Announcement fragment = new Announcement();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "Demo Titile"
        NOTIFICATION_MESSAGE = "Demo messege";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }



    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                Listener      ){
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue( jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_announcement, container, false);
        fb=view.findViewById(R.id.fab_send_msg);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(),SendMessege.class);
//                startActivity(intent);

                SendMsgBtmSheet addPhotoBottomDialogFragment =
                        SendMsgBtmSheet.newInstance();
                addPhotoBottomDialogFragment.show( getActivity().getSupportFragmentManager(),
                        SendMsgBtmSheet.TAG);
                Toast.makeText(getContext(),"DB Error",Toast.LENGTH_LONG).show();

            }
        });
        initUi(view);
        return view;
    }
    private DatabaseReference databaseReference;

    private void initUi(View view) {
        inbox_recycle=view.findViewById(R.id.inbox_recycle);
        ann_prg=view.findViewById(R.id.ann_prg);
        ann_prg.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.INBOX);
        inbox_recycle.setHasFixedSize(true);
        inbox_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();

        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = prefs.getString(Credentials.USER_DESIGNATION, null);

        if(des==null){
            des="";
        }

        if (des.equals(FirebasePath.PRESIDENT)) {
            inbox_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && fb.getVisibility() == View.VISIBLE) {
                        fb.hide();
                    } else if (dy < 0 && fb.getVisibility() !=View.VISIBLE) {
                        fb.show();
                    }
                }
            });
        }else {
            fb.setVisibility(View.GONE);
        }





        fetchData();
    }
    private ArrayList<MsgModel> list;
    private MsgAdapter adapter;
    void fetchData() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    try {

                        list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MsgModel user = dataSnapshot.getValue(MsgModel.class);
                        list.add(user);
                    }
//                loadingprg.setVisibility(View.GONE);
                        Collections.reverse(list);
                        adapter = new MsgAdapter(getContext(), list);
                    inbox_recycle.setAdapter(adapter);
//                        inbox_recycle.scrollToPosition(list.size()-1);

                        adapter.notifyDataSetChanged();
                        ann_prg.setVisibility(View.GONE);
                }catch(Exception e){
                    Toast.makeText(getContext(),"Error : "+e,Toast.LENGTH_SHORT).show();
                        ann_prg.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    ann_prg.setVisibility(View.GONE);

                }
            });

    }

    public void sendNotification(String messege){

        try{
            JSONObject jsonObject = new JSONObject();
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Announcement");
            notificationObj.put("body","Hello first notification");

            JSONObject dataObj = new JSONObject();
            dataObj.put("UserId","hello1");
            jsonObject.put("notification",notificationObj);
            jsonObject.put("data",dataObj);
//            jsonObject.put("to",otherUser.getFCMToken());
        }catch (Exception e){

        }

    }



    private void callApi(JSONObject jsonObject){
         MediaType JSON
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String url ="https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAALRvzS04:APA91bH3O9LTYW7FNOWlSF2_4vY3jfUQ0qEVFYDg5kcwwK6CMW6wM6AyxHcu8JDzX1jmkfSIyz635qfjSXgU95KCffBzQCe3-ezeDDDSdzMQNih0CV1WYsyeo3o5ZyTOS8szxnKuswAr")
                .build();

        client.newCall(request);

    }

}