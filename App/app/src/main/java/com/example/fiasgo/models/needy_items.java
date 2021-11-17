package com.example.fiasgo.models;

import java.util.ArrayList;

public class needy_items {
    public String item_name;
    public String item_desc;
    public String item_quantity;
    public static ArrayList<needy_items> needy_items_list= new ArrayList<>();

    public needy_items(String item_name, String item_desc, String item_quantity) {
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_quantity = item_quantity;
        needy_items_list.add(this);
    }

}
