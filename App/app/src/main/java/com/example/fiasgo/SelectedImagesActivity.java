package com.example.fiasgo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.*;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class SelectedImagesActivity extends AppCompatActivity {


    private RecyclerView selected_img_rv;
    private ImagesAdapter selected_img_rv_adapter;
    private AlertDialog.Builder DialogBuilder;
    private AlertDialog dialog;
    ArrayList<String> selected_images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_images);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        selected_images = (ArrayList<String>) args.getSerializable("ARRAYLIST");
        System.out.println(selected_images);

        selected_img_rv = findViewById(R.id.images_cont);
        selected_img_rv_adapter  = new ImagesAdapter(this, selected_images);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        lm.setReverseLayout(false);
        selected_img_rv.setAdapter(selected_img_rv_adapter);
        selected_img_rv.setLayoutManager(lm);

    }

    public void fillDetailsActivity(View v){
        DialogBuilder = new AlertDialog.Builder(this);
        final View prompt = getLayoutInflater().inflate(R.layout.upload_options_prompt, null);
        CardView victims = prompt.findViewById(R.id.cardView_request_for_help);
        CardView services = prompt.findViewById(R.id.card_view_service);
        DialogBuilder.setView(prompt);
        dialog = DialogBuilder.create();
        dialog.show();

        victims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectedImagesActivity.this, "victims", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectedImagesActivity.this, UploadVictimsPostActivity.class);
                intent.putExtra("images", selected_images);
                startActivity(intent);

            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectedImagesActivity.this, "services", Toast.LENGTH_SHORT).show();
                Intent  intent = new Intent(SelectedImagesActivity.this, UploadServicesPostActivity.class);
                intent.putExtra("images", selected_images);
                startActivity(intent);
            }
        });

    }


}