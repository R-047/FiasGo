package com.example.fiasgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fiasgo.Adapters.NeedyItemsAdapter;
import com.example.fiasgo.Adapters.VictimsPostAdapter;
import com.example.fiasgo.models.needy_items;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

public class AttachListActivity extends AppCompatActivity {

    RecyclerView items_rv;
    Button save_list_btn, add_item_btn;
    EditText item_name, item_quant, item_desc;
    NeedyItemsAdapter needy_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_list);
        items_rv = findViewById(R.id.needy_items_cont);
        save_list_btn = findViewById(R.id.save_lst_btn);
        item_name = findViewById(R.id.enter_needy_item_name);
        item_desc = findViewById(R.id.enter_needy_item_desc);
        item_quant = findViewById(R.id.enter_needy_item_quant);
        add_item_btn = findViewById(R.id.add_item_btn);

        needy_adapter = new NeedyItemsAdapter(this,needy_items.needy_items_list);
        items_rv.setAdapter(needy_adapter);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        lm.setReverseLayout(false);
        items_rv.setLayoutManager(lm);



    }

    public void add_item(View v){
        String item_name_entered = item_name.getText().toString(), item_quant_entered = item_quant.getText().toString(), item_desc_entered = item_desc.getText().toString();
        new needy_items(item_name_entered,item_desc_entered, item_quant_entered);
        needy_adapter.notifyDataSetChanged();
    }


    public void saveList(View v){
        finish();
    }

}