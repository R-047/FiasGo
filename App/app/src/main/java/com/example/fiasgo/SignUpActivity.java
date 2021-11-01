package com.example.fiasgo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    String server_url = "http://172.19.96.1:3000/register";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        User.ACTIVITY = this;
        sign_up();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sign_up() {
        RequestParams params = new RequestParams();
        params.put("name", "Jonathan");
        params.put("email", "jonathan@gmail.com");
        params.put("password", "1234");
        params.put("ph_number", "9090909090");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String ts = dtf.format(now);
        params.put("join_date_time", ts);
        PostIt(params, "register");

    }

    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getString("error").equals("null")){
                        System.out.println(response);
                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        System.out.println(response);
                        Toast.makeText(SignUpActivity.this, "Sign Up Failed, User Already exists", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}