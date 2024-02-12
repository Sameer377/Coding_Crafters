package com.agpitcodeclub.app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agpitcodeclub.app.Adapters.ConnectToAdapter;
import com.agpitcodeclub.app.Adapters.MsgAdapter;
import com.agpitcodeclub.app.Models.CommunityModel;
import com.agpitcodeclub.app.Models.MsgModel;
import com.agpitcodeclub.app.R;
import com.agpitcodeclub.app.utils.Credentials;
import com.agpitcodeclub.app.utils.FirebasePath;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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

    String Userid=null;

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
/*
        TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "Demo Titile";
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
        sendNotification(notification);*/
    }

/*


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
*/

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_announcement, container, false);
        fb=view.findViewById(R.id.fab_send_msg);
        recyclerView=view.findViewById(R.id.recycle_connect_to);

        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, getContext().MODE_PRIVATE);
        String des = null;
        des = prefs.getString(Credentials.USER_DESIGNATION, null);

          if(des==null || des.equals("h_Member") ){
            des="";
            fb.hide();
        }else{
              fb.show();
          }


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(),SendMessege.class);
//                startActivity(intent);

                Intent intent = new Intent(getContext(), SendMsgActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation); // Apply the animation


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



       /* if(des==null){
            des="";
        }*/

//        if (des.equals(FirebasePath.PRESIDENT)) {
//            inbox_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (dy > 0 && fb.getVisibility() == View.VISIBLE) {
//                        fb.hide();
//                    } else if (dy < 0 && fb.getVisibility() !=View.VISIBLE) {
//                        fb.show();
//                    }
//                }
//            });
       /* }else {
            fb.setVisibility(View.GONE);
        }*/
        SharedPreferences prefs = getContext().getSharedPreferences(Credentials.USER_DATA, MODE_PRIVATE);
        Userid = prefs.getString(Credentials.USER_ID, null);
        fetchData();
        loadContentInList();



         Toast.makeText(getContext(),Userid,Toast.LENGTH_LONG).show();

    }
    private ArrayList<MsgModel> list;
    private MsgAdapter adapter;
    private ConnectToAdapter adapter1;
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


//    ConnectTo

    private ArrayList<CommunityModel> list1;

    private RecyclerView recyclerView;
    void loadContentInList(){

        databaseReference = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list1=new ArrayList<>();
        adapter1 = new ConnectToAdapter(getContext(),list1,Userid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CommunityModel user = dataSnapshot.getValue(CommunityModel.class);
                    user.setDesignation(dataSnapshot.getKey().toString());
                    System.out.println("Node1 : "+dataSnapshot.getKey().toString()+"\n"+dataSnapshot.getKey().equals(FirebasePath.DEVELOPER));
                    Log.v("tag","Name : "+user.getName()+"\nDesignation : "+user.getDesignation());
                    if (dataSnapshot.getKey().equals(FirebasePath.DEVELOPER)){
                        DatabaseReference innerChild = FirebaseDatabase.getInstance().getReference(FirebasePath.COMMUNITY).child(dataSnapshot.getKey());
                        innerChild.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot1) {

                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                    CommunityModel user1 = dataSnapshot1.getValue(CommunityModel.class);
                                    user1.setDesignation(dataSnapshot.getKey().toString());
//                                    System.out.println("Name : "+user1.getName());
//                                    System.out.println("des2 : "+user1.getDesignation());
                                        System.out.println("1 Names : "+user1.getName()+"\n");
                                        list1.add(user1);

                                }
                                adapter1.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });


                    }else if(!dataSnapshot.getKey().equals(FirebasePath.MEMBER)){

                            list1.add(user);

                    }

                }

                try {
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                }
                System.out.println("\n.............................................\n");
                for (CommunityModel l:list1) {
                    System.out.println("Names : "+l.getName()+"\n");
                }
            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(getContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }




}