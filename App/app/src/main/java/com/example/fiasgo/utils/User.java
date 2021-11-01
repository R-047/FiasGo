package com.example.fiasgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.fiasgo.LoginActivity;

public class User {
    public static Activity ACTIVITY = null;
    public String user_id, user_name, user_email, ph_num, profile_pic;
    public String session_id;


    public static void setUser(Activity activity, String session_id, String user_id, String user_name, String user_email, String ph_num, String profile_pic){
        SharedPreferences sharedPref = activity.getSharedPreferences("FiasGo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("session_id", session_id);
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("user_email", user_email);
        editor.putString("ph_num", ph_num);
        editor.putString("profile_pic", profile_pic);
        editor.apply();
    }

    public static User getUser(){
        User user = new User();
        SharedPreferences sharedPref = ACTIVITY.getSharedPreferences("FiasGo",Context.MODE_PRIVATE);
        user.session_id = sharedPref.getString("session_id", "");
        user.user_id = sharedPref.getString("user_id", "");
        return  user;
    }

    public static void userSignOut(Activity activity){
        SharedPreferences sharedPref = activity.getSharedPreferences("FiasGo",Context.MODE_PRIVATE);
        sharedPref.edit().remove("session_id").apply();
        System.out.println("user logged out");
    }

    public static boolean userAuthenticate(Activity activity){
        if(activity.getSharedPreferences("FiasGo",Context.MODE_PRIVATE).getString("session_id", "") != ""){
            return true;
        }
        return false;
    }

    public static void Logout(Activity currContext){
        currContext.startActivity(new Intent(currContext, LoginActivity.class));
        User.userSignOut(currContext);
        currContext.finish();
    }

}