package com.example.fiasgo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.fiasgo.ItemsToSupportActivity;
import com.example.fiasgo.R;
import com.example.fiasgo.UploadVictimsPostActivity;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class VictimsPostAdapter extends RecyclerView.Adapter<VictimsPostAdapter.VictimsPostAdapterHolder> {

    private Context context;
    private JSONArray feeds;

    private AlertDialog.Builder DialogBuilder;
    private AlertDialog dialog;

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
        String user_name = null, location=null,  upvotes=null, supports_count=null, post_desc = null, post_id;
        JSONArray pics = null;

        try {
            JSONObject post = feeds.getJSONObject(position);
            user_name = post.getString("user_name");
            upvotes = post.getString("v_upvotes");
            supports_count = post.getString("v_count_supports");
            post_desc = post.getString("v_post_desc");
            pics = post.getJSONArray("images");
            location = post.getString("victims_location_name");
            post_id = post.getString("v_post_id");

            holder.user_name.setText(user_name);
            holder.post_title.setText("details");
            holder.location.setText(location);
            holder.description.setText(post_desc);
            holder.support_Text.setText(supports_count);
            holder.upvotes_Text.setText(upvotes);
            holder.upvote_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        buttonView.setButtonDrawable(R.drawable.ic_bx_bxs_upvote_dark);
                        int upvotes_int = Integer.parseInt(holder.upvotes_Text.getText().toString()) + 1;
                        holder.upvotes_Text.setText(Integer.toString(upvotes_int));
                    }else{
                        int upvotes_int = Integer.parseInt(holder.upvotes_Text.getText().toString()) - 1;
                        holder.upvotes_Text.setText(Integer.toString(upvotes_int));
                        buttonView.setButtonDrawable(R.drawable.upvote_icon);
                    }
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
                            params.put("post_type", "RFH");
                            params.put("v_post_id", post_id);
                            params.put("user_id", User.getUser().user_id);
                            params.put("report_desc", report_desc);
                            PostIt(params, "reportPost");
                        }
                    });
                }
            });

            holder.imageSlider.setAdapter(new image_slider_class(context, pics, image_slider_class.VICTIM));
            LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
            lm.setReverseLayout(false);
            holder.imageSlider.setLayoutManager(lm);

            if(User.getUser().user_id.equals(post.getString("user_id"))){
                holder.support_items_btn.setVisibility(View.INVISIBLE);
            }
            holder.support_items_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemsToSupportActivity.class);
                    try {
                        intent.putExtra("post_id", post.getString("v_post_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);

                }
            });

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

    public class VictimsPostAdapterHolder extends RecyclerView.ViewHolder {
        ImageView dp;
        RecyclerView imageSlider;
        TextView user_name, location, post_title, upvotes_Text, support_Text;
        TextView description;
        ToggleButton upvote_btn;
        ImageButton support_btn, report_btn;
        Button support_items_btn;

        public VictimsPostAdapterHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.profile_pic_image_view);
            imageSlider = itemView.findViewById(R.id.victims_post_view_adapter);
            user_name = itemView.findViewById(R.id.user_name);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            post_title = itemView.findViewById(R.id.details_header);
            upvote_btn = itemView.findViewById(R.id.upvote_victims_post_tb);
            support_btn = itemView.findViewById(R.id.supports_btn);
            upvotes_Text = itemView.findViewById(R.id.upvotes_victims_post_tv);
            support_Text = itemView.findViewById(R.id.supports_count_tv);
            report_btn = itemView.findViewById(R.id.report_btn);
            support_items_btn = itemView.findViewById(R.id.support_button);
        }
    }
}
