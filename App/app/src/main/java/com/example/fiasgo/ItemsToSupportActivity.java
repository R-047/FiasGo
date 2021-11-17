package com.example.fiasgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.fiasgo.Adapters.ItemsToSupportAdapter;
import com.example.fiasgo.Adapters.VictimsPostAdapter;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ItemsToSupportActivity extends AppCompatActivity {

    RecyclerView rv;
    ItemsToSupportAdapter ia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_to_support);
        rv = findViewById(R.id.items_to_support_cont);
        Intent intent = getIntent();
        String post_id=intent.getStringExtra("post_id");
        RequestParams params = new RequestParams();
        params.put("v_post_id", post_id);
        GetIt(params, "getVictimsItems");
    }


    public void GetIt(RequestParams params, String relUrl){
        FiasGoApi.get(relUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);

                }
                else{
                    User.Logout(ItemsToSupportActivity.this);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                ia = new ItemsToSupportAdapter(ItemsToSupportActivity.this, response);
                rv.setAdapter(ia);
               rv.setLayoutManager(new LinearLayoutManager(ItemsToSupportActivity.this));
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