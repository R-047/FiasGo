package com.example.fiasgo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                view = inflater.inflate(R.layout.user_support_activity, parent, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.user_camp_activity, parent, false);
                break;
        }
        return new userActivityAdapterHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserActivityAdapter.userActivityAdapterHolder holder, int position) {
        try {
            JSONObject curr_jsonobj = arr.getJSONObject(position);
            String type = curr_jsonobj.getString("type");
            System.out.println(type);
            switch (type){
                case "RFH":
                    JSONArray images_Arr = curr_jsonobj.getJSONArray("images");
                    holder.v_activity_rv.setAdapter(new image_slider_class(context, images_Arr, image_slider_class.VICTIM));
                    LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                    lm.setReverseLayout(false);
                    holder.v_activity_rv.setLayoutManager(lm);
                    String date = curr_jsonobj.getString("date");
                    date = date.split("GMT")[0].trim();
                    String supports = curr_jsonobj.getString("v_count_supports");
                    String upvotes = curr_jsonobj.getString("v_upvotes");
                    String desc = curr_jsonobj.getString("v_post_desc");
                    holder.v_activity_date.setText(date);
                    holder.v_supports.setText(supports);
                    holder.v_upvotes.setText(upvotes);
                    holder.v_desc.setText(desc);
                   break;
                case "SERVICE":
                    JSONArray s_images_Arr = curr_jsonobj.getJSONArray("images");
                    holder.s_activity_rv.setAdapter(new image_slider_class(context, s_images_Arr, image_slider_class.SERVICE));
                    LinearLayoutManager s_lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                    s_lm.setReverseLayout(false);
                    holder.s_activity_rv.setLayoutManager(s_lm);
                    String s_date = curr_jsonobj.getString("date");
                    s_date = s_date.split("GMT")[0].trim();
                    String s_comments = curr_jsonobj.getString("comments_count");
                    String s_upvotes = curr_jsonobj.getString("s_upvotes");
                    String s_desc = curr_jsonobj.getString("s_post_desc");
                    holder.s_activity_date.setText(s_date);
                    holder.s_comments.setText(s_comments);
                    holder.s_upvotes.setText(s_upvotes);
                    holder.s_desc.setText(s_desc);
                    break;
                case "SUPPORT":
                    break;
                case "CAMP":
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {

        try{
            switch (arr.getJSONObject(position).getString("type")) {
                case "RFH":
                    return 1;
                case "SERVICE":
                    return 2;
                case "SUPPORT":
                    return 3;
                case "CAMP":
                    return 4;
                default:
                    return -1;
            }
        }catch(JSONException e){System.out.println("json exception");}
        return  -1;
    }

    @Override
    public int getItemCount() {
        return arr.length();
    }

    public class userActivityAdapterHolder extends RecyclerView.ViewHolder {
        RecyclerView v_activity_rv, s_activity_rv, camp_activity_rv;
        TextView v_activity_date, s_activity_date, support_date, camp_date;
        TextView v_upvotes, s_upvotes, v_supports, s_comments, camp_name;
        TextView s_desc, v_desc, supports_desc, camp_desc;
        public userActivityAdapterHolder(@NonNull View itemView) {
            super(itemView);
            switch(itemView.getId()){
                case R.id.user_service_Activity_cont:
                    s_activity_rv = itemView.findViewById(R.id.activity_service_image_container);
                    s_activity_date = itemView.findViewById(R.id.user_activity_service_date_tv);
                    s_upvotes = itemView.findViewById(R.id.user_activity_service_upvotes);
                    s_comments = itemView.findViewById(R.id.user_Activity_service_comments_tv);
                    s_desc = itemView.findViewById(R.id.user_activity_service_desc);
                    break;
                case R.id.users_rfh_post_cont:
                    v_activity_rv = itemView.findViewById(R.id.rfh_activity_rfh_image_container);
                    v_activity_date = itemView.findViewById(R.id.user_activity_rfh_date_tv);
                    v_upvotes = itemView.findViewById(R.id.user_activity_rfh_upvotes);
                    v_desc = itemView.findViewById(R.id.user_activity_rfh_desc);
                    v_supports = itemView.findViewById(R.id.user_Activity_rfh_supports_tv);
                    break;
                case R.id.users_support_activity_cont:
                    support_date = itemView.findViewById(R.id.user_activity_support_date_tv);
                    supports_desc = itemView.findViewById(R.id.user_activity_supports_desc);
                    break;
                case R.id.users_camp_activity_cont:
                    camp_activity_rv = itemView.findViewById(R.id.rfh_activity_camp_image_container);
                    camp_date = itemView.findViewById(R.id.user_activity_camp_date_tv);
                    camp_name = itemView.findViewById(R.id.user_Activity_camp_desc_header);
                    camp_desc = itemView.findViewById(R.id.user_activity_camp_desc);
                    break;
            }
        }
    }
}
