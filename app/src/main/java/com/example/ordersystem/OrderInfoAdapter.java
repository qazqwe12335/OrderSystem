package com.example.ordersystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderInfoAdapter extends FirebaseRecyclerAdapter<StoreOrderInfoModel, OrderInfoAdapter.OrderInfoViewHolder> {

    public OrderInfoAdapter(@NonNull FirebaseRecyclerOptions<StoreOrderInfoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderInfoViewHolder holder, int position, @NonNull StoreOrderInfoModel model) {
        holder.orderName.setText(model.getItem());
        holder.orderQuan.setText(model.getCupQuantity() + "杯");
        holder.orderCup.setText("(" + model.getChooseCup() + ")");
        holder.orderIce.setText(model.getIce());
        holder.orderSugar.setText(model.getSugar());

    }

    @NonNull
    @Override
    public OrderInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.storeorderitemrecyclerview, parent, false);
        return new OrderInfoViewHolder(v);
    }

    class OrderInfoViewHolder extends RecyclerView.ViewHolder {

        TextView orderName, orderCup, orderQuan, orderIce, orderSugar;

        public OrderInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.storeOrderCommodityName);
            orderCup = itemView.findViewById(R.id.storeOrderCup);
            orderIce = itemView.findViewById(R.id.storeOrderIce);
            orderSugar = itemView.findViewById(R.id.storeOrdersugar);
            orderQuan = itemView.findViewById(R.id.storeOrderCupQuan);
        }
    }
}
