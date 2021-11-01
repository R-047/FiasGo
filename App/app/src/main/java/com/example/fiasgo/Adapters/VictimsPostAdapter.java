package com.example.fiasgo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.fiasgo.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VictimsPostAdapter extends RecyclerView.Adapter<VictimsPostAdapter.VictimsPostAdapterHolder> {

    private Context context;
    private JSONArray feeds;


    public VictimsPostAdapter(Context context, JSONArray feeds) {
        this.context = context;
        this.feeds = feeds;
    }

    @NonNull
    @Override
    public VictimsPostAdapter.VictimsPostAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.victims_post, parent, false);
        return new VictimsPostAdapter.VictimsPostAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VictimsPostAdapter.VictimsPostAdapterHolder holder, int position) {
        String user_name = null, loation="bangalore",  upvotes=null, supports_count=null, post_desc = null;
        JSONArray pics = null;

        try {
            JSONObject post = feeds.getJSONObject(position);
            user_name = post.getString("user_name");
            upvotes = post.getString("v_upvotes");
            supports_count = post.getString("v_count_supports");
            post_desc = post.getString("v_post_desc");
            pics = post.getJSONArray("images");

            holder.user_name.setText(user_name);
            holder.post_title.setText("details");
            holder.location.setText(loation);
            holder.description.setText(post_desc);
//            holder.description.setShowingChar(post_desc.length());
//            number of line you want to short
//            holder.description.setShowingLine(post_desc.length() - (post_desc.length() - 2));
//            holder.description.addShowMoreText("more");
//            holder.description.addShowLessText("less");


            holder.imageSlider.setAdapter(new image_slider_class(context, pics, image_slider_class.VICTIM));
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

    public class VictimsPostAdapterHolder extends RecyclerView.ViewHolder {
        ImageView dp;
        RecyclerView imageSlider;
        TextView user_name, location, post_title;
        TextView description;
        public VictimsPostAdapterHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.profile_pic_image_view);
            imageSlider = itemView.findViewById(R.id.victims_post_view_adapter);
            user_name = itemView.findViewById(R.id.user_name);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);

            post_title = itemView.findViewById(R.id.details_header);
        }
    }
}
