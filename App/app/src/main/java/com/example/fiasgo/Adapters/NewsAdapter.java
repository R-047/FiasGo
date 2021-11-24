package com.example.fiasgo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;

import org.json.JSONArray;
import org.json.JSONException;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder> {
    JSONArray arr;
    Context context;

    public NewsAdapter(JSONArray arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_card_layout, parent, false);
        return new NewsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsAdapterHolder holder, int position) {
        try {
            holder.heading.setText(arr.getJSONObject(position).getJSONObject("fields").getString("name"));
            holder.content.setText(arr.getJSONObject(position).getJSONObject("fields").getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arr.length();
    }

    public class NewsAdapterHolder extends RecyclerView.ViewHolder {
        TextView heading, content;
        public NewsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.news_heading_tv);
            content = itemView.findViewById(R.id.news_desc_tv);
        }
    }
}
