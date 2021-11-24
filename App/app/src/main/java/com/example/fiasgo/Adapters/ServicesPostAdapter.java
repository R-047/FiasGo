package com.example.fiasgo.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fiasgo.ImagesAdapter;
import com.example.fiasgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesPostAdapter extends RecyclerView.Adapter<ServicesPostAdapter.ServicePostAdapterHolder> {

    private Context context;
    private JSONArray feeds;


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
        String user_name = null, loation="bangalore", contact_info=null, upvotes=null, comment_count=null, post_desc;
        JSONArray pics = null;

        try {
            JSONObject post = feeds.getJSONObject(position);
            user_name = post.getString("user_name");
            contact_info = post.getString("contact_info");
            upvotes = post.getString("s_upvotes");
            comment_count = post.getString("comments_count");
            post_desc = post.getString("s_post_desc");
            pics = post.getJSONArray("images");
            holder.user_name.setText(user_name);
            holder.post_title.setText("details");
            holder.location.setText(loation);
            holder.description.setText(post_desc);

            holder.imageSlider.setAdapter(new image_slider_class(context, pics, image_slider_class.SERVICE));
            LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
            lm.setReverseLayout(false);
            holder.imageSlider.setLayoutManager(lm);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            contact_btn = itemView.findViewById()
        }
    }
}
