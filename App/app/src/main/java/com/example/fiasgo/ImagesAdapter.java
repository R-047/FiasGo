package com.example.fiasgo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesAdapterHolder> {

    private Context context;
    private ArrayList<String> imgs_Arr;

    public ImagesAdapter(Context context, ArrayList<String> imgs_Arr) {
        this.context = context;
        this.imgs_Arr = imgs_Arr;
    }

    @NonNull
    @Override
    public ImagesAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.selected_image, parent, false);
        return new ImagesAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ImagesAdapterHolder holder, int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String img_path = imgs_Arr.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(img_path, options);
        holder.img.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imgs_Arr.size();
    }

    public class ImagesAdapterHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ImagesAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView2);
        }
    }
}
