package com.example.fiasgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fiasgo.Adapters.image_slider_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;

public class CampInfoActivity extends AppCompatActivity {

    RecyclerView campimgs_cont;
    TextView camp_name, camp_details, camp_desc;
    image_slider_class adapter;
    Button contact_btn, maps_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_info);
        campimgs_cont = findViewById(R.id.camp_images_cont_rv);
        camp_name = findViewById(R.id.camp_info_camp_name_tv);
        camp_details = findViewById(R.id.camp_info_camp_details_tv);
        camp_desc = findViewById(R.id.camp_info_camp_desc_tv);
        contact_btn = findViewById(R.id.camp_info_contact_btn);
        maps_btn= findViewById(R.id.camp_info_mao_btn);



        Intent intent = getIntent();
        String camp_obj = intent.getStringExtra("camp_obj");
//        the camp opens from opening_date till closing_date and the camp is functional from opening_time to closing_time
        String name_value, details_value, desc_value, contact = null;
        double lat = 0, longit = 0;
        try {
            JSONObject json_camp_obj = new JSONObject(camp_obj);
            JSONArray imgs_arr = json_camp_obj.getJSONArray("images");
            adapter = new image_slider_class(this, imgs_arr, image_slider_class.CAMP);
            name_value = json_camp_obj.getString("camp_name");

            String opening_date = json_camp_obj.getString("opening_date");
            opening_date = opening_date.split("T")[0].trim();

            String closing_date = json_camp_obj.getString("closing_date");
            closing_date = closing_date.split("T")[0].trim();

            String opening_time = json_camp_obj.getString("available_from");
            String closing_time = json_camp_obj.getString("available_till");
            desc_value = json_camp_obj.getString("description");

            contact = json_camp_obj.getString("contact");
            lat = json_camp_obj.getDouble("camp_lat");
            longit = json_camp_obj.getDouble("camp_long");

            camp_name.setText(name_value);
            camp_details.setText("the camp opens from "+opening_date+" till "+closing_date+" and the camp is functional from "+opening_time+" to "+closing_time+"");
            camp_desc.setText(desc_value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("passed: "+camp_obj);

        campimgs_cont.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        lm.setReverseLayout(false);
        campimgs_cont.setLayoutManager(lm);


        String finalContact = contact;
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(finalContact);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+finalContact));

                if (ActivityCompat.checkSelfPermission(CampInfoActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });


        double finalLat = lat;
        double finalLongit = longit;
        maps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(finalLat +" "+ finalLongit);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:"+finalLat+","+finalLongit+"?q="+finalLat+","+finalLongit+" (name)"));
// the following line should be used if you want use only Google maps
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                startActivity(intent);
            }
        });



    }
}