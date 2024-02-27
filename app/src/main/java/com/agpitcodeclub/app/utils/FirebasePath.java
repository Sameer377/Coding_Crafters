package com.agpitcodeclub.app.utils;

public class FirebasePath {

    /* Realtime database  */
    public static final String FIREBASE_ROOT="https://codingcrafters-fac21-default-rtdb.firebaseio.com/";
    public static final String USERS="users";
    public static final String PRESIDENT="a_President";
    public static final String VPRESIDENT="b_Vice President";
    public static final String SECRETARY="c_Secretary";
    public static final String VSECRETARY="d_Vice Secretary";
    public static final String REVENUE_MANAGER="e_Revenue Manger";
    public static final String VREVENUE_MANAGER="f_Assistant Revenue Manger";
    public static final String DEVELOPER="g_Developer";
    public static final String COMMUNITY="community";
    public static final String CONNECT_TO_CHATS="chats";
    public static final String MEMBER = "h_Member";
    public static final String MEMBER_USERTOKENS = "communitytokens";
    public static final String POST = "post";
    public static final String INBOX = "inbox";
    public static final String DASHBOARD="dash";
    public static final String UPCOMMING=DASHBOARD+"upcommingevent";

    public static final String CONTENT = "content";
    public static final String POSTIMAGE="imglist";
    /* Firebase Storage */
    public static final String STORAGE_ROOT="/MemberFiles";
//    public static final String STORAGE_MEMBER_FILES=STORAGE_ROOT+"/MemberFiles";
    public static final String STORAGE_MEMBER_PROFILE=STORAGE_ROOT+"/ProfileImage";

    //FCM Constants
    public static final String FCM_BASE_URL="https://fcm.googleapis.com";
    public static final String FCM_SERVER_KEY="AAAALRvzS04:APA91bH3O9LTYW7FNOWlSF2_4vY3jfUQ0qEVFYDg5kcwwK6CMW6wM6AyxHcu8JDzX1jmkfSIyz635qfjSXgU95KCffBzQCe3-ezeDDDSdzMQNih0CV1WYsyeo3o5ZyTOS8szxnKuswAr";
    public static final String FCM_CONTENT_TYPE="application/json";
    public static final String FCM_TOPIC="/topics/Announcement";

    //Social Networks
    public static final String SOCIAL = "socials";
    public static final String USER_INSTAGRAM="instagram";
    public static final String USER_TWITTER="twitter";
    public static final String USER_GITHUB="github";
    public static final String USER_LINKEDIN="linkedin";

}
