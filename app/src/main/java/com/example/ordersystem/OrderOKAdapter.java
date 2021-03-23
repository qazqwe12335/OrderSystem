package com.example.ordersystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

//建立以完成的訂單
//與 StoreOrderAdapter訂單一樣，分開寫比較清楚而已
public class OrderOKAdapter extends FirebaseRecyclerAdapter<OKModel, OrderOKAdapter.OrderOKViewHolder> {

    Context context;
    OrderInfoAdapter orderInfoAdapter;

    public OrderOKAdapter(@NonNull FirebaseRecyclerOptions<OKModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderOKViewHolder holder, int position, @NonNull OKModel model) {
        long time = Long.valueOf(model.getArrivedTime()) * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("a HH:mm");
        //format.setTimeZone(TimeZone.getTimeZone("GMT"));

        if (model.getStatus() == 1) {
            holder.tvName.setText(model.getCustomerName());
            holder.tvPhone.setText("電話 :" + model.getPhone());
            holder.tvAddress.setText(model.getAddress());
            holder.tvTotal.setText(String.valueOf(model.getTotal()));
            holder.tvTime.setText(String.valueOf(format.format(date)));

            if (model.getComment().isEmpty()) {
                holder.tvComment.setVisibility(View.GONE);
            } else {
                holder.tvComment.setVisibility(View.VISIBLE);
                holder.tvComment.setText("備註 : " + model.getComment());
            }


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            holder.Orderrv.setLayoutManager(linearLayoutManager);

            FirebaseRecyclerOptions<StoreOrderInfoModel> options =
                    new FirebaseRecyclerOptions.Builder<StoreOrderInfoModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("order").child("1").child(model.getID()).child("Meun"), StoreOrderInfoModel.class)
                            .build();

            orderInfoAdapter = new OrderInfoAdapter(options);
            holder.Orderrv.setAdapter(orderInfoAdapter);

            orderInfoAdapter.startListening();
        } else {

        }
    }

    @NonNull
    @Override
    public OrderOKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_order_recyclerview, parent, false);
        return new OrderOKViewHolder(v);
    }

    class OrderOKViewHolder extends RecyclerView.ViewHolder {

        TextView tvTotal, tvTime, tvName, tvPhone, tvAddress, tvComment;
        ImageButton checkbtn;
        RecyclerView Orderrv;

        public OrderOKViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            tvTotal = itemView.findViewById(R.id.storeOrderTotal);
            tvTime = itemView.findViewById(R.id.storeOrderTime);
            tvName = itemView.findViewById(R.id.storeOrdername);
            tvPhone = itemView.findViewById(R.id.storeOrderphone);
            tvAddress = itemView.findViewById(R.id.storeOrderaddress);
            tvComment = itemView.findViewById(R.id.store_order_comment);
            checkbtn = itemView.findViewById(R.id.storeOrderCheck);
            Orderrv = itemView.findViewById(R.id.OrderRv);

            checkbtn.setVisibility(View.GONE);
        }
    }
}
