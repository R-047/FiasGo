package com.example.fiasgo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;
import com.example.fiasgo.utils.FiasGoApi;
import com.example.fiasgo.utils.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context context;
    private static CardView image;
    private static CardView camp_image;
    public CustomInfoWindowAdapter(Context context) {
        mWindow = LayoutInflater.from(context).inflate(R.layout.map_info_layout, null);
        this.context = context;
    }

    private void renderWindow(Marker marker, View view) throws JSONException {
        image = view.findViewById(R.id.marker_camp_image);
        if(camp_image == null){
            camp_image = image;
        }
        TextView name = view.findViewById(R.id.marker_camp_name);
        TextView desc = view.findViewById(R.id.marker_camp_desc);
        String payload_rec = marker.getSnippet();
        if(payload_rec != null) {
            System.out.println(payload_rec);
            JSONObject location_data = new JSONObject(payload_rec);
            String type = location_data.getString("Type");
            switch (type) {
                case "hospital":
                    ((ViewGroup) view).removeView(image);
                    name.setText(location_data.getString("title"));
                    desc.setText(location_data.getString("address"));
                    break;


                case "camp":
                    if (image == null) {
                        ((ViewGroup) view).removeView(image);
                        ((ViewGroup) view).addView(camp_image);
                    }
                    RequestParams params = new RequestParams();
                    String camp_img_name = location_data.getJSONArray("images").getJSONObject(0).getString("camp_image_name");
                    if (camp_img_name != "") {
                        params.put("imageName", camp_img_name);
                        setImage(params, camp_image.findViewById(R.id.camp_imageview), "getCampsImages");
                    }
                    name.setText(location_data.getString("camp_name"));
                    desc.setText(location_data.getString("description"));

                    break;


                default:
                    break;
            }
        }else{
            ((ViewGroup) view).removeView(image);
            name.setText("your location");
            desc.setText("you are here");
        }



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

    @Nullable
    @Override
    public View getInfoWindow(@NonNull  Marker marker) {
        try {
            renderWindow(marker, mWindow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull  Marker marker) {
        try {
            renderWindow(marker, mWindow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mWindow;
    }
}
