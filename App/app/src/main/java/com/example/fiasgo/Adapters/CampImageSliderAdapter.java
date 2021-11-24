package com.example.fiasgo.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;

import java.io.IOException;
import java.util.ArrayList;

public class CampImageSliderAdapter extends RecyclerView.Adapter<CampImageSliderAdapter.CampImageSliderAdapterHolder> {

    private Context context;
    private ArrayList<Uri> imgs_Arr;

    public CampImageSliderAdapter(Context context, ArrayList<Uri> imgs_Arr) {
        this.context = context;
        this.imgs_Arr = imgs_Arr;
    }

    @NonNull
    @Override
    public CampImageSliderAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_image, parent, false);
        return new CampImageSliderAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampImageSliderAdapter.CampImageSliderAdapterHolder holder, int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri img_path = imgs_Arr.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), img_path);
            holder.img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        holder.img.setImageURI(img_path);
    }

    @Override
    public int getItemCount() {
        return imgs_Arr.size();
    }

    public class CampImageSliderAdapterHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public CampImageSliderAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.post_image_imageview);
        }
    }
}
