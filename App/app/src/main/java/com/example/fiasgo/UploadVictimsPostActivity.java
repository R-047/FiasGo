package com.example.fiasgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UploadVictimsPostActivity extends AppCompatActivity {

    ArrayList<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_victims_post);
        Intent intent = getIntent();
        images = (ArrayList<String>)intent.getSerializableExtra("images");
        System.out.println(images);
    }

    public void onUploadVicitimsPost(View v) throws FileNotFoundException {
        ArrayList<File> myFilesList = new ArrayList<>();
        File[] myFiles = null;
        for(String filename : images){
            myFilesList.add(new File(filename));
        }
        myFiles = new File[myFilesList.size()];
        myFilesList.toArray(myFiles);



        RequestParams params = new RequestParams();
        params.put("user_id", User.getUser().user_id);
        params.put("post_desc", "test_post");
        params.put("image_count", myFiles.length);
        params.put("post_images", myFiles);

        PostIt(params, "postVictims");

    }


    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                }
                else{
                    User.Logout(UploadVictimsPostActivity.this);
                }
            }
        });
    }

    public boolean checkAuthError(JSONObject jsonObject){
        try {
            if(jsonObject.getString("error").equals("null")){
                return false;
            }else{
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}