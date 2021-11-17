package com.example.fiasgo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ItemsToSupportAdapter extends RecyclerView.Adapter<ItemsToSupportAdapter.ItemsToSupportAdapterHolder> {

    private AlertDialog.Builder DialogBuilder;
    private AlertDialog dialog;

    Context context;
    JSONArray jsonArray;

    public ItemsToSupportAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;

    }

    @NonNull
    @Override
    public ItemsToSupportAdapter.ItemsToSupportAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_to_support, parent, false);
        return new ItemsToSupportAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsToSupportAdapter.ItemsToSupportAdapterHolder holder, int position) {
        try {
            String item_name = jsonArray.getJSONObject(position).getString("list_item_name");
            String limit = jsonArray.getJSONObject(position).getString("limit_quantity");
            String received_items = jsonArray.getJSONObject(position).getString("received");
            String description = jsonArray.getJSONObject(position).getString("item_desc");
            holder.support_item_name.setText(item_name);
            holder.support_item_limit.setText(limit);
            holder.support_item_acc.setText(received_items);
            holder.support_desc_name.setText(description);
            holder.support_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    DialogBuilder = new AlertDialog.Builder(context);
                    final View prompt = inflater.inflate(R.layout.qunatity_propmt, null);
                    DialogBuilder.setView(prompt);
                    dialog = DialogBuilder.create();
                    dialog.show();

                    Button donate_btn = prompt.findViewById(R.id.quantity_prompt_btn);
                    donate_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                EditText quantity_to_donate = prompt.findViewById(R.id.quantity_promt_et);
                                EditText desc = prompt.findViewById(R.id.quantity_description_et);
                                String description = desc.getText().toString();
                                int amount = Integer.parseInt(quantity_to_donate.getText().toString());
                                String rec_quant = jsonArray.getJSONObject(position).getString("received");
                                String limit = jsonArray.getJSONObject(position).getString("limit_quantity");
                                int rec_quant_num = Integer.parseInt(rec_quant) + amount;
                                if(rec_quant_num <= Integer.parseInt(limit)){
                                    RequestParams params = new RequestParams();
                                    params.put("user_id", User.getUser().user_id);
                                    params.put("desc", description);
                                    params.put("post_id", jsonArray.getJSONObject(position).getString("v_post_id"));
                                    params.put("list_item_id", jsonArray.getJSONObject(position).getString("list_item_id"));
                                    params.put("quantity", rec_quant_num);
                                    System.out.println(params);
                                    PostIt(params, "supportVictim");



                                }else{
                                    Toast.makeText(context, "maxed out", Toast.LENGTH_SHORT).show();
                                }


                                System.out.println("quantity to donate : "+rec_quant_num);
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

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
                    RequestParams params = new RequestParams();
                    try {
                        params.put("v_post_id", jsonArray.getJSONObject(0).getString("v_post_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    GetIt(params, "getVictimsItems");

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



    public void GetIt(RequestParams params, String relUrl){
        FiasGoApi.get(relUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(!checkAuthError(response)){
                    System.out.println(response);

                }
                else{
                    User.Logout((Activity) context);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                jsonArray = response;
                notifyDataSetChanged();

            }
        });
    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ItemsToSupportAdapterHolder extends RecyclerView.ViewHolder{
        TextView support_item_name, support_desc_name, support_item_limit, support_item_acc, support_item_btn;
        public ItemsToSupportAdapterHolder(@NonNull View itemView) {
            super(itemView);
            support_item_name = itemView.findViewById(R.id.support_item_name);
            support_desc_name = itemView.findViewById(R.id.support_item_desc);
            support_item_limit = itemView.findViewById(R.id.support_item_limit);
            support_item_acc = itemView.findViewById(R.id.support_item_accumulated);
            support_item_btn = itemView.findViewById(R.id.support_item_button);
        }
    }
}
