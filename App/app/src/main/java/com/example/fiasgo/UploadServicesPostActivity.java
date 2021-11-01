package com.example.fiasgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.example.fiasgo.utils.location;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.msebera.android.httpclient.Header;

public class UploadServicesPostActivity extends AppCompatActivity implements location.location_interface {

    ArrayList<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_services_post);
        User.ACTIVITY = this;
        Intent intent = getIntent();
        images = (ArrayList<String>)intent.getSerializableExtra("images");
        System.out.println(images);
    }

    public void onUploadServicesPost(View v) {
        new location(this).getLastLocation();

    }



    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                }
                else{
                    User.Logout(UploadServicesPostActivity.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == location.PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new location(this).getLastLocation();
            }
        }
    }


    @Override
    public void getCoords(Location location) {
        ArrayList<File> myFilesList = new ArrayList<>();
        File[] myFiles = null;
        for(String filename : images){
            myFilesList.add(new File(filename));
        }
        myFiles = new File[myFilesList.size()];
        myFilesList.toArray(myFiles);

        RequestParams params = new RequestParams();
        params.put("user_id", User.getUser().user_id);
        params.put("contact_info","00000000");
        params.put("post_desc", "test_post");
        params.put("image_count", myFiles.length);
        params.put("post_lat", location.getLatitude());
        params.put("post_long", location.getLongitude());
        try{
            params.put("post_images", myFiles);
        }catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        PostIt(params, "postServices");

    }
}