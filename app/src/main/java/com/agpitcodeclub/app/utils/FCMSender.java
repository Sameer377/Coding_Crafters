package com.agpitcodeclub.app.utils;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class FCMSender {
    /*
    String userFcmToken;
    String title;
    String body;
    Activity mActivity;
    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAALRvzS04:APA91bH3O9LTYW7FNOWlSF2_4vY3jfUQ0qEVFYDg5kcwwK6CMW6wM6AyxHcu8JDzX1jmkfSIyz635qfjSXgU95KCffBzQCe3-ezeDDDSdzMQNih0CV1WYsyeo3o5ZyTOS8szxnKuswAr";

    public FCMSender(String userFcmToken, String title, String body, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {
        requestQueue = Volley.newRequestQueue (mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put( "to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put(  "title", title);
            notiObject.put(  "body", body);
            notiObject.put(  "icon",  "ic_launcher_background"); //enter icon that exists in drawable only
            JSONObject extraData = new JSONObject();
            extraData.put( "activity", toGo);
            mainObj.put( "notification", notiObject);
            mainObj.put( "data", extData);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response. Listener<JSONO
            @Override
            public void on Response (JSONObject response) {
// code run is got response
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     */
}
