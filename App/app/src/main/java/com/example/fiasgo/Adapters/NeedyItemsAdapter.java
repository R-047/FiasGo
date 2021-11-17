package com.example.fiasgo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiasgo.R;
import com.example.fiasgo.models.needy_items;

import java.util.ArrayList;

public class NeedyItemsAdapter extends RecyclerView.Adapter<NeedyItemsAdapter.NeedyItemsAdapterViewHolder> {
    Context context;
    ArrayList<needy_items> needy_items_Arr;

    public NeedyItemsAdapter(Context context, ArrayList<needy_items> arr) {
        this.context = context;
        this.needy_items_Arr = arr;
    }

    @NonNull
    @Override
    public NeedyItemsAdapter.NeedyItemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_add_widget, parent, false);
        return new NeedyItemsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeedyItemsAdapter.NeedyItemsAdapterViewHolder holder, int position) {
        holder.item_name_et.setText(needy_items_Arr.get(position).item_name);
        holder.item_quant_et.setText("quantity: "+needy_items_Arr.get(position).item_quantity);
        holder.item_dec_et.setText(needy_items_Arr.get(position).item_desc);
    }

    @Override
    public int getItemCount() {
        return needy_items_Arr.size();
    }

    class NeedyItemsAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView item_name_et, item_dec_et, item_quant_et;
        public NeedyItemsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name_et = itemView.findViewById(R.id.needy_item_name);
            item_dec_et = itemView.findViewById(R.id.needy_item_desc);
            item_quant_et = itemView.findViewById(R.id.needy_item_quant);
        }
    }
}
