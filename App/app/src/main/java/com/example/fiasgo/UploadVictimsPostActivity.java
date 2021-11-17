package com.example.fiasgo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.example.fiasgo.models.needy_items;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.example.fiasgo.utils.location;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UploadVictimsPostActivity extends AppCompatActivity implements location.location_interface {

    ArrayList<String> images;
    EditText desc;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_victims_post);
        desc = findViewById(R.id.desc_et);
        aSwitch = findViewById(R.id.switch2);
        Intent intent = getIntent();
        images = (ArrayList<String>)intent.getSerializableExtra("images");
        System.out.println(images);
    }

    public void onUploadVicitimsPost(View v) throws FileNotFoundException, JSONException {
        new location(this).getLastLocation();


    }


    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                    finish();
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


    public void onAttachListClick(View v){
        startActivity(new Intent(this, AttachListActivity.class));
    }


    @Override
    public void getCoords(Location location) throws JSONException, FileNotFoundException {
        ArrayList<File> myFilesList = new ArrayList<>();
        File[] myFiles = null;
        for(String filename : images){
            myFilesList.add(new File(filename));
        }
        myFiles = new File[myFilesList.size()];
        myFilesList.toArray(myFiles);

        JSONArray needy_items_arr = new JSONArray();

        for(needy_items itm : needy_items.needy_items_list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item_name", itm.item_name);
            jsonObject.put("item_desc", itm.item_desc);
            jsonObject.put("limit", itm.item_quantity);
            needy_items_arr.put(jsonObject);
        }
        System.out.println(needy_items_arr);



        RequestParams params = new RequestParams();
        params.put("user_id", User.getUser().user_id);
        params.put("post_desc", desc.getText().toString());
        params.put("image_count", myFiles.length);
        params.put("post_images", myFiles);
        if(aSwitch.isEnabled()){
            params.put("post_lat", location.getLatitude());
            params.put("post_long", location.getLongitude());
        }
        params.put("needys_list", needy_items_arr);
        PostIt(params, "postVictims");
    }
}