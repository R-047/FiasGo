package com.example.fiasgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.example.fiasgo.utils.location;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
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

public class UploadServicesPostActivity extends AppCompatActivity {

    ArrayList<String> images;
    Button location_pick_btn;
    private static Place place;
    EditText description, ph_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_services_post);
        description = ((EditText) findViewById(R.id.service_upload_desc));
        ph_num = ((EditText) findViewById(R.id.service_upload_ph_num));
        location_pick_btn = findViewById(R.id.services_upload_location_pick_btn);
        User.ACTIVITY = this;
        Intent intent = getIntent();
        images = (ArrayList<String>)intent.getSerializableExtra("images");
        System.out.println(images);

    }


    public void pickLocation(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), 1);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    place = PlacePicker.getPlace(this, data);
                    String placeName = String.format("Place: %s", place.getName());
                    double latitude = place.getLatLng().latitude;
                    double longitude = place.getLatLng().longitude;
                    System.out.println(placeName+" "+latitude+" "+longitude);




            }
        }
    }

    public void onUploadServicesPost(View v) {

        ArrayList<File> myFilesList = new ArrayList<>();
        File[] myFiles = null;
        for(String filename : images){
            myFilesList.add(new File(filename));
        }
        myFiles = new File[myFilesList.size()];
        myFilesList.toArray(myFiles);

        RequestParams params = new RequestParams();
        params.put("user_id", User.getUser().user_id);
        params.put("contact_info",ph_num.getText().toString());
        params.put("post_desc", description.getText().toString());
        params.put("image_count", myFiles.length);
        params.put("post_lat", place.getLatLng().latitude);
        params.put("post_long",place.getLatLng().longitude);
        try{
            params.put("post_images", myFiles);
        }catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        PostIt(params, "postServices");
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

}