package com.example.fiasgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Fragment mapFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.feeds_itm:
                    Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_itm:
                    Toast.makeText(MainActivity.this, "coming back later", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };
}