package com.example.fiasgo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.ImagesAdapter;
import com.example.fiasgo.R;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class image_slider_class extends RecyclerView.Adapter<image_slider_class.ImageSliderAdapterHolder> {

    private Context context;
    private JSONArray post_images;
    String Type;
    public static final String VICTIM="victim", SERVICE="service", CAMP="camp";
    //private int[] img_Arr = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3};

    public image_slider_class(Context context, JSONArray post_images, String Type) {
        this.context = context;
        this.post_images = post_images;
        this.Type = Type;
        System.out.println("PASSED POST IMAGES ARRAY.......................... ");
        System.out.println(post_images);
    }

    @NonNull
    @Override
    public ImageSliderAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        if(this.Type.equals(CAMP)){
            view = inflater.inflate(R.layout.camp_marker_image, parent, false);
            return new ImageSliderAdapterHolder(view);
        }
        view = inflater.inflate(R.layout.post_image, parent, false);
        return new ImageSliderAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapterHolder holder, int position) {

        String file_name = null;
        RequestParams params = new RequestParams();
        try {
            JSONObject post = post_images.getJSONObject(position);
            switch(this.Type){
                case SERVICE:
                    file_name = post.getString("imagename");
                    params.add("imageName", file_name);
                    setImage(params, holder.img,"getPostImage");
                    break;
                case VICTIM:
                    file_name = post.getString("victims_post_image_name");
                    params.add("imageName", file_name);
                    setImage(params, holder.img,"getPostImage");
                    break;
                case CAMP:
                    file_name = post.getString("camp_image_name");
                    params.add("imageName", file_name);
                    setImage(params, holder.img,"getCampsImages");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("FILENAME OF POST INDEX "+position+" "+file_name);
        //holder.img.setImageDrawable(context.getResources().getDrawable(img_Arr[position]));
    }

    public void setImage(RequestParams params, View view, String relUrl){
        ImageView img = (ImageView) view;
        FiasGoApi.get(relUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    new JSONObject(new String(responseBody));
                    User.Logout((Activity) context);
                } catch (JSONException e) {
                    Bitmap image = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                    img.setImageBitmap(image);
                    System.out.println("SETTING IMAGE....."+image);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("FAILLLUREEEEE IMAAGEEEEEeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                System.out.println(new String(responseBody));
            }
        });

    }

    @Override
    public int getItemCount() {
        return post_images.length();
    }

    public class ImageSliderAdapterHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ImageSliderAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.post_image_imageview);
        }
    }


}