package com.example.fiasgo.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fiasgo.ImagesAdapter;
import com.example.fiasgo.R;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ServicesPostAdapter extends RecyclerView.Adapter<ServicesPostAdapter.ServicePostAdapterHolder> {

    private Context context;
    private JSONArray feeds;

    private AlertDialog.Builder DialogBuilder;
    private AlertDialog dialog;

    public ServicesPostAdapter(Context context, JSONArray feeds) {
        this.context = context;
        this.feeds = feeds;
    }

    @NonNull
    @Override
    public ServicesPostAdapter.ServicePostAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.services_post, parent, false);
        return new ServicesPostAdapter.ServicePostAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesPostAdapter.ServicePostAdapterHolder holder, int position) {
        String user_name = null, location=null, contact_info=null, upvotes=null, comment_count=null, post_desc, post_id;
        JSONArray pics = null;

        try {
            JSONObject post = feeds.getJSONObject(position);
            user_name = post.getString("user_name");
            contact_info = post.getString("contact_info");
            upvotes = post.getString("s_upvotes");
            comment_count = post.getString("comments_count");
            post_desc = post.getString("s_post_desc");
            location = post.getString("service_location_name");
            pics = post.getJSONArray("images");
            post_id = post.getString("s_post_id");
            holder.user_name.setText(user_name);
            holder.post_title.setText("details");
            holder.location.setText(location);
            holder.description.setText(post_desc);
            holder.upvotes_count.setText(upvotes);
            holder.comments_count.setText(comment_count);
            String finalContact_info = contact_info;
            holder.contact_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(finalContact_info);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+finalContact_info));

                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(callIntent);
                }
            });

            holder.report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    DialogBuilder = new AlertDialog.Builder(context);
                    final View prompt = inflater.inflate(R.layout.report_dialog, null);
                    DialogBuilder.setView(prompt);
                    dialog = DialogBuilder.create();
                    dialog.show();

                    prompt.findViewById(R.id.report_post_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String report_desc = ((EditText) prompt.findViewById(R.id.report_post_et)).getText().toString();
                            RequestParams params = new RequestParams();
                            params.put("post_type", "SERVICE");
                            params.put("s_post_id", post_id);
                            params.put("user_id", User.getUser().user_id);
                            params.put("report_desc", report_desc);
                            PostIt(params, "reportPost");
                        }
                    });
                }
            });

            holder.imageSlider.setAdapter(new image_slider_class(context, pics, image_slider_class.SERVICE));
            LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
            lm.setReverseLayout(false);
            holder.imageSlider.setLayoutManager(lm);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PostIt(RequestParams params, String relUrl){
        FiasGoApi.post(relUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);
                    Toast.makeText(context, "reported successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    User.Logout((Activity) context);
                }
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
    public int getItemCount() {
        return feeds.length();
    }

    public class ServicePostAdapterHolder extends RecyclerView.ViewHolder {
        ImageView dp;
        RecyclerView imageSlider;
        TextView user_name, location, description, post_title, upvotes_count, comments_count;
        Button contact_btn;
        ImageButton report_btn;
        public ServicePostAdapterHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.profile_pic_image_view);
            imageSlider = itemView.findViewById(R.id.image_container_view_pager);
            user_name = itemView.findViewById(R.id.user_name);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            post_title = itemView.findViewById(R.id.details_header);
            upvotes_count = itemView.findViewById(R.id.service_post_s_upvotes_tv);
            comments_count = itemView.findViewById(R.id.service_post_s_comm_count_tv);
            contact_btn = itemView.findViewById(R.id.contact_button);
            report_btn = itemView.findViewById(R.id.report_service_post_btn);
        }
    }
}
