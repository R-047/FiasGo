package com.example.fiasgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User.ACTIVITY=this;
        if(User.userAuthenticate(this)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    public void login(View v){
        RequestParams params = new RequestParams();
        params.put("email", "abhay@gmail.com");
        params.put("password", "1234");
        FiasGoApi.post("login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(response.getString("error").equals("null")){
                        System.out.println("LOGIN SUCCCESSSSSS: "+response);
                        User.setUser(LoginActivity.this, FiasGoApi.getSesssionFromResponseHeaders(headers), response.getJSONObject("user_details").getString("user_id"), response.getJSONObject("user_details").getString("user_name"), response.getJSONObject("user_details").getString("user_email"), response.getJSONObject("user_details").getString("ph_num"), response.getJSONObject("user_details").getString("profile_pic"));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        System.out.println("LOGIN FAILUREEEEEE: "+response);
                        Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray arr) {
                System.out.println(arr);
            }
        });

    }

    public void onSignUp(View v){
        startActivity(new Intent(this, SignUpActivity.class));
    }

}