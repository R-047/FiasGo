package com.example.fiasgo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fiasgo.Adapters.ServicesPostAdapter;
import com.example.fiasgo.Adapters.VictimsPostAdapter;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VicimsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VicimsFragment extends Fragment {
    private JSONArray feed_arr;
    private RecyclerView victims_post_rv;
    private RecyclerView.Adapter victims_post_adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VicimsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VicimsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VicimsFragment newInstance(String param1, String param2) {
        VicimsFragment fragment = new VicimsFragment();
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
        return inflater.inflate(R.layout.fragment_vicims, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        victims_post_rv = view.findViewById(R.id.victims_feeds_rv);
        feed_arr = new JSONArray();
        victims_post_adapter = new VictimsPostAdapter(getActivity(),feed_arr);
        victims_post_rv.setAdapter(victims_post_adapter);
        victims_post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        GetIt(null, "getVictims");
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
                victims_post_adapter = new VictimsPostAdapter(getActivity(),feed_arr);
                victims_post_rv.setAdapter(victims_post_adapter);
                victims_post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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