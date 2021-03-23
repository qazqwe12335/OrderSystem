package com.example.ordersystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

//設定購物車資料清單
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.MyHolder> {

    private ArrayList<OrderInfo> orderInfoArrayList;
    Context c;
    private OnDelCallback onDelCallback;

    public CartRecyclerViewAdapter(Context context, ArrayList<OrderInfo> orderInfoArrayList, OnDelCallback onDelCallback) {
        this.orderInfoArrayList = orderInfoArrayList;
        this.c = context;
        this.onDelCallback = onDelCallback;
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_listview, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.MyHolder holder, int position) {
        HashMap<String, String> info = orderInfoArrayList.get(position).getUserorder();
        holder.rv_title.setText(info.get("Item"));
        holder.rv_quan.setText("x" + info.get("cupQuantity"));
        holder.rv_sugar.setText(info.get("sugar"));
        holder.rv_ice.setText(info.get("ice"));
        holder.rv_position.setText(String.valueOf(position));

        String getPrice = null;
        if (info.get("ChooseCup").equals("M")) {
            getPrice = info.get("mprice");
        } else if (info.get("ChooseCup").equals("L")) {
            getPrice = info.get("lprice");
        }

        holder.rv_price.setText("$" + getPrice);
    }

    @Override
    public int getItemCount() {
        return orderInfoArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView rv_title, rv_sugar, rv_ice, rv_position, rv_price, rv_quan;
        ImageButton rv_delbtn, rv_moditybtn;

        OnClickListener onClickListener;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            rv_title = itemView.findViewById(R.id.rvCommodityName);
            rv_sugar = itemView.findViewById(R.id.rvSugar);
            rv_ice = itemView.findViewById(R.id.rvIce);
            rv_position = itemView.findViewById(R.id.rvPosition);
            rv_price = itemView.findViewById(R.id.rvPrice);
            rv_delbtn = itemView.findViewById(R.id.rvDelBtn);
            rv_moditybtn = itemView.findViewById(R.id.rvModityBtn);
            rv_quan = itemView.findViewById(R.id.rvQuan);

            rv_delbtn.setOnClickListener(this);
            rv_moditybtn.setOnClickListener(this);
        }

        //購物車上的兩個按鈕 刪除、修改
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //點擊刪除呼叫callback 立即修改總杯數 總金額
                case R.id.rvDelBtn:
                    AuthData authData = (AuthData) c.getApplicationContext();
                    authData.removeUserOrder(getAdapterPosition());

                    orderInfoArrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    onDelCallback.onDelListener();

                    break;

                case R.id.rvModityBtn:

                    Intent gotoModityIntent = new Intent(c, ModifyActivity.class);
                    gotoModityIntent.putExtra("listPosition", getAdapterPosition());
                    c.startActivity(gotoModityIntent);
                    break;
            }
        }

        public void setUserOrderClickListsnse(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }

    public interface OnClickListener {
        void OnDelClick(View v, int position);

        void OnModityClick(View v, int position);
    }
}
