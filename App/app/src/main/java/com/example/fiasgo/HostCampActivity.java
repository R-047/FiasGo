package com.example.fiasgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fiasgo.Adapters.CampImageSliderAdapter;
import com.example.fiasgo.Adapters.image_slider_class;

import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.RealPathUtil;
import com.example.fiasgo.utils.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class HostCampActivity extends AppCompatActivity {

    RecyclerView images_cont;
    CampImageSliderAdapter adapter;
    EditText camp_name, id, camp_desc, contact_number;
    TextView date, time, location;
    ArrayList<Uri> img_list;
    String CampClosingDate, CampOpeningDate, CampOpeningTime, CampClosingTime;
    TimePickerDialog opening_time_picker, closing_time_picker;
    double latitude, longitude;
    private static Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_camp);
        User.ACTIVITY = this;
        images_cont = findViewById(R.id.host_camp_img_cont_rv);
        camp_name = findViewById(R.id.host_camp_camp_name_et);
        id = findViewById(R.id.host_camp_id_et);
        camp_desc = findViewById(R.id.host_camp_camp_desc_tv);
        contact_number = findViewById(R.id.host_camp_ph_num_et);
        date = findViewById(R.id.host_camp_display_date_tv);
        time = findViewById(R.id.host_camp_select_time_tv);
        location = findViewById(R.id.host_camp_select_location_tv);
        img_list = new ArrayList();
        adapter = new CampImageSliderAdapter(this, img_list);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        lm.setReverseLayout(false);
        images_cont.setAdapter(adapter);
        images_cont.setLayoutManager(lm);
    }

    public void selectImageFromGallery(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        img_list.add(imageUri);
                        adapter.notifyDataSetChanged();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                }
            }
        }else if(requestCode == 2 && resultCode == RESULT_OK) {
            place = PlacePicker.getPlace(this, data);
            String placeName = String.format("Place: %s", place.getName());
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
            location.setText("location selected successfully, "+placeName);
            System.out.println(placeName+" "+latitude+" "+longitude+" "+place.getName());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void pick_date(View v){
        DatePickerDialog opening_date_picker = new DatePickerDialog(this);
        opening_date_picker.setTitle("camp opening date");
        opening_date_picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.println(year+" " +month+" " +dayOfMonth);
                CampOpeningDate = year+"-"+month+"-"+dayOfMonth;
                opening_date_picker.dismiss();
                DatePickerDialog closing_date_picker = new DatePickerDialog(HostCampActivity.this);
                closing_date_picker.setTitle("camp closing date");
                closing_date_picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        System.out.println(year+" " +month+" " +dayOfMonth);
                        CampClosingDate = year+"-"+month+"-"+dayOfMonth;
                        date.setText("starts from "+CampOpeningDate+"\ntill "+CampClosingDate);

                    }
                });
                closing_date_picker.show();
            }
        });
        opening_date_picker.show();
    }





    TimePickerDialog.OnTimeSetListener time_set_listener = new TimePickerDialog.OnTimeSetListener() {

        TimePickerDialog.OnTimeSetListener time_set_listener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                System.out.println(hourOfDay+" "+minute);
                CampClosingTime = hourOfDay+":"+minute;
                time.setText("from "+CampOpeningTime+"\ntill "+CampClosingTime);

            }
        };
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            System.out.println(hourOfDay+" "+minute);
            CampOpeningTime = hourOfDay+":"+minute;
            opening_time_picker.dismiss();
            closing_time_picker = new TimePickerDialog(HostCampActivity.this, time_set_listener2, 12, 0, true);
            closing_time_picker.setTitle("closing time picker");
            closing_time_picker.show();

        }
    };

    public void pick_time(View v){
        opening_time_picker = new TimePickerDialog(this, time_set_listener, 12, 0, true);
        opening_time_picker.setTitle("camp opening time");
        opening_time_picker.show();


    }


    public void pick_location(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), 2);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void host(View v) throws IOException, URISyntaxException {
        ArrayList<File> myFilesList = new ArrayList<>();
        ArrayList<String> images_refs = new ArrayList<>();
        File[] myFiles = null;
        Bitmap bm = null;
        String destFolder = this.getCacheDir().getAbsolutePath();
        FileOutputStream out = null;
        for(Uri filename : img_list){
            bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filename);
            String file_name = UUID.randomUUID().toString()+".png";
            String ImgReference = destFolder+file_name;
            try {
                out = new FileOutputStream(ImgReference);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                images_refs.add(ImgReference);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        for(String filename : images_refs){
            myFilesList.add(new File(filename));
        }

        myFiles = new File[myFilesList.size()];
        myFilesList.toArray(myFiles);
        System.out.println(myFiles+" "+myFilesList);
        String camp_name_value = camp_name.getText().toString();
        String unique_id = id.getText().toString();
        String desc = camp_desc.getText().toString();
        String ph_num = contact_number.getText().toString();
        String opening_date = CampOpeningDate;
        String closing_date = CampClosingDate;
        String opening_time = CampOpeningTime;
        String closing_time = CampClosingTime;
        Double camp_lat = latitude;
        Double camp_long = longitude;

        RequestParams params = new RequestParams();
        params.put("user_id", User.getUser().user_id);
        params.put("camp_name", camp_name_value);
        params.put("id", unique_id);
        params.put("opening_date", opening_date);
        params.put("closing_date", closing_date);
        params.put("time_from", opening_time);
        params.put("time_till", closing_time);
        params.put("camp_desc", desc);
        params.put("contact", ph_num);
        params.put("camp_lat", camp_lat);
        params.put("camp_long", camp_long);
        params.put("camp_images", myFiles);
        System.out.println(params);
        PostIt(params, "setCamps");
    }


    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                }
                else{
                    User.Logout(HostCampActivity.this);
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