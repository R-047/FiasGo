package com.example.fiasgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.fiasgo.Adapters.CustomInfoWindowAdapter;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class  MapsFragment extends Fragment {

    private MapStyleOptions mapStyleOptions;
    private GoogleMap gmap;
    private FusedLocationProviderClient mFusedLocationClient;
    private int PERMISSION_ID = 44;
    ImageView host_camp_btn;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gmap = googleMap;
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style);
            //method to get users location
            gmap.setMapStyle(mapStyleOptions);
            gmap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
            getLastLocation();
        }

    };

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            setCordsMap(location);
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            String lat = Double.toString(mLastLocation.getLatitude());
            String longit = Double.toString(mLastLocation.getLongitude());
            Log.d("LAT", lat);
            Log.d("LONG", longit);
            setCordsMap(mLastLocation);
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    public void setCordsMap(Location location){
        String lat = Double.toString(location.getLatitude());
        String longit = Double.toString(location.getLongitude());
        Log.d("LAT", lat);
        Log.d("LONG", longit);

        gmap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        LatLng users_loc = new LatLng(location.getLatitude(), location.getLongitude());
        gmap.addMarker(new MarkerOptions().position(users_loc).title("your are here")).setIcon(BitmapFromVector(getActivity(), R.drawable.user_location));
        //gmap.moveCamera(CameraUpdateFactory.newLatLng(users_loc));
        //gmap.animateCamera(CameraUpdateFactory.zoomIn());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(users_loc)      // Sets the center of the map to Mountain View
                .zoom(14)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        RequestParams params = new RequestParams();
        params.put("lat", location.getLatitude());
        params.put("long", location.getLongitude());
        System.out.println("SENDING GET REQ TO GET COORDINATES");
        GetIt(params, "getCamps");
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }





    public void GetIt(RequestParams params, String relUrl){
        FiasGoApi.get(relUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                    JSONArray HospitalList = null;
                    JSONArray CampsList = null;
                    try {
                        HospitalList = response.getJSONArray("HospitalList");
                        CampsList = response.getJSONArray("CampsList");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0 ; i<HospitalList.length(); i++){
                        Double lat = 0.0, longit = 0.0;
                        try {
                            JSONObject hospital = HospitalList.getJSONObject(i);
                            lat = hospital.getDouble("lat");
                            longit = hospital.getDouble("long");
                            LatLng hospital_nearby = new LatLng(lat, longit);
                            gmap.addMarker(new MarkerOptions()
                                    .snippet(hospital.put("Type","hospital").toString())
                                    .position(hospital_nearby)
                                    .title(hospital.getString("title"))).setIcon(BitmapFromVector(getActivity(), R.drawable.ic_group_7hospital_marker));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Double finalLat = lat;
                        Double finalLongit = longit;

//                        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                            @Override
//                            public void onInfoWindowClick(@NonNull Marker marker) {
//                                System.out.println(finalLat +" "+ finalLongit);
//                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+finalLat+","+finalLongit+"?q="+finalLat+","+finalLongit+" (name)"));
//                                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
//                                startActivity(intent);
//                            }
//                        });

                    }
                    for(int i = 0 ; i<CampsList.length(); i++){
                        try {
                            JSONObject camps = CampsList.getJSONObject(i);
                            Double lat = camps.getDouble("camp_lat");
                            Double longit =camps.getDouble("camp_long");
                            String camps_images = camps.getString("images");
                            String camp_desc = camps.getString("description");
                            String payload = camps_images+";"+camp_desc;
                            LatLng camps_nearby = new LatLng(lat, longit);
                            gmap.addMarker(new MarkerOptions()
                                    .snippet(camps.put("Type", "camp").toString())
                                    .position(camps_nearby)
                                    .title(camps.getString("camp_name")))
                                    .setIcon(BitmapFromVector(getActivity(), R.drawable.ic_group_7camps));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(@NonNull Marker marker) {
                                System.out.println("openeing activity: "+marker.getSnippet());
                                Intent intent = new Intent(getContext(), CampInfoActivity.class);
                                intent.putExtra("camp_obj", marker.getSnippet());
                                startActivity(intent);
                                //TODO: if marker.getSnippet is null dont do anything, if marker.getSnippet is hospital details then open the google maps by passing specific coordinates from get snippets, else if if marker.getSnippet is camps then open another activity to display info about the camp
                            }
                        });

                    }
                }
                else{
                    User.Logout(getActivity());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        host_camp_btn = view.findViewById(R.id.host_camp_btn);
        host_camp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostCamp();
            }
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        User.ACTIVITY = getActivity();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }


    public void hostCamp(){
        startActivity(new Intent(getContext(), HostCampActivity.class));
    }


}