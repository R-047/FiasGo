package com.example.fiasgo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;

import org.json.JSONArray;
import org.json.JSONException;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.userActivityAdapterHolder> {
    Context context;
    JSONArray arr;

    public UserActivityAdapter(Context context, JSONArray arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public userActivityAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch(viewType){
            case 1:
                view = inflater.inflate(R.layout.users_rfh_activity, parent, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.users_service_activity, parent, false);
                break;
            case 3:
//                view = inflater.inflate(R.layout.users_support_activity, parent, false);
                break;
            case 4:
//                view = inflater.inflate(R.layout.users_camp_activity, parent, false);
                break;
        }
        return new userActivityAdapterHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserActivityAdapter.userActivityAdapterHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {

        try{
            switch (arr.getJSONObject(position).getString("type")) {
                case "RFH":
                    return 1;
                case "SERVICE":
                    return 2;
                case "CAMP":
                    return 3;
                case "SUPPORT":
                    return 4;
                default:
                    return -1;
            }
        }catch(JSONException e){System.out.println("json exception");}
        return  -1;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class userActivityAdapterHolder extends RecyclerView.ViewHolder {
        public userActivityAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
