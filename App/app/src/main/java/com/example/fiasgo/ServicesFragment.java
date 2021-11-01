package com.example.fiasgo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fiasgo.Adapters.ServicesPostAdapter;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.example.fiasgo.utils.location;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends Fragment implements location.location_interface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private JSONArray feed_arr;
    private RecyclerView service_post_rv;
    private RecyclerView.Adapter service_post_adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service_post_rv = view.findViewById(R.id.service_feeds_rv);
        feed_arr = new JSONArray();
        service_post_adapter = new ServicesPostAdapter(getActivity(),feed_arr);
        service_post_rv.setAdapter(service_post_adapter);
        service_post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //service_post_adapter
    }




    @Override
    public void onStart() {
        super.onStart();
        new location(getActivity(), this).getLastLocation();
    }

    public void GetIt(RequestParams params, String relUrl){
        FiasGoApi.get(relUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                }
                else{
                    User.Logout(getActivity());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                feed_arr = response;
                service_post_adapter = new ServicesPostAdapter(getActivity(),feed_arr);
                service_post_rv.setAdapter(service_post_adapter);
                service_post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                new location(getActivity(), this).getLastLocation();
            }
        }
    }


    @Override
    public void getCoords(Location location) {
        System.out.println("GONAAAAAA SEND AAAAAA GETETTTT");
        RequestParams params = new RequestParams();
        params.put("user_lat", location.getLatitude());
        params.put("user_long", location.getLongitude());
        GetIt(params, "getServices");
        System.out.println(location.getLatitude()+" "+location.getLongitude());

    }
}