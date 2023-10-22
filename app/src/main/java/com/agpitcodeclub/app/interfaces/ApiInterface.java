package com.agpitcodeclub.app.interfaces;

import static com.agpitcodeclub.app.utils.FirebasePath.FCM_CONTENT_TYPE;
import static com.agpitcodeclub.app.utils.FirebasePath.FCM_SERVER_KEY;

import com.agpitcodeclub.app.utils.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key="+FCM_SERVER_KEY,"Content-Type:"+FCM_CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);
}
