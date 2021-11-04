package com.example.fiasgo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fiasgo.utils.User;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    Fragment mapFragment, feedsFragment;
    BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LocationRequest locationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);





        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navDrawerListener);


        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = bottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        feedsFragment = new FeedsFragment();
        mapFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.options_holder, feedsFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }

        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
            Log.d("ITEM STATS: ", "ITEM IS SELECTED");
            System.out.println("item stats: " + item.isChecked());
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.feeds_itm:
                    getSupportFragmentManager().beginTransaction().replace(R.id.options_holder, feedsFragment).commit();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Toast.makeText(MainActivity.this, "open feeds", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_itm:
                    //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Fragment uploadPost = new UploadPostFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.options_holder, uploadPost).commit();
                    Toast.makeText(MainActivity.this, "open post", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.timeline_itm:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Fragment timeLine = new TimelineFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.options_holder, timeLine).commit();
                    Toast.makeText(MainActivity.this, "open timeline", Toast.LENGTH_SHORT).show();

            }
            return true;
        }


    };


    private NavigationView.OnNavigationItemSelectedListener navDrawerListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.logout:
                    Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                    User.Logout(MainActivity.this);
                    break;
                case R.id.exit:
                    Toast.makeText(MainActivity.this, "exit", Toast.LENGTH_SHORT).show();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    public void openNavDrawer(View v){
        drawerLayout.openDrawer(navigationView);
    }


}