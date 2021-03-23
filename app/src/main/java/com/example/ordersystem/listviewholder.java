package com.example.ordersystem;

import android.view.View;
import android.widget.TextView;

public class listviewholder {
    TextView name, large_cup, med_cup;

    listviewholder(View v) {
        name = v.findViewById(R.id.commodity_name);
        large_cup = v.findViewById(R.id.commodity_big_cup_price);
        med_cup = v.findViewById(R.id.commodity_med_cup_price);
    }
}
