package com.example.ordersystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

//建立店家端點餐資料(姓名 電話 地址)的 清單，餐點的內容在第74行
public class StoreOrderAdapter extends FirebaseRecyclerAdapter<StoreOrderModel, StoreOrderAdapter.storeOrderViewHolder> {

    OnRemoveCallback onRemoveCallback;

    @NonNull
    @Override
    public StoreOrderModel getItem(int position) {
        return super.getItem(getItemCount() - 1 - position);
    }

    Context context;
    StoreOrderInfoModel storeOrderInfoModel;
    OrderInfoAdapter orderInfoAdapter;

    public StoreOrderAdapter(@NonNull FirebaseRecyclerOptions<StoreOrderModel> options, OnRemoveCallback onRemoveCallback) {
        super(options);
        this.onRemoveCallback = onRemoveCallback;
    }

    @Override
    protected void onBindViewHolder(@NonNull storeOrderViewHolder holder, int position, @NonNull StoreOrderModel model) {

        long time = model.getArrivedTime() * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("a HH:mm");
        //format.setTimeZone(TimeZone.getTimeZone("GMT"));

        if (model.getStatus() == 0) {
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


            //建立餐點清單
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            holder.Orderrv.setLayoutManager(linearLayoutManager);

            //到firebase 取得餐點清單
            FirebaseRecyclerOptions<StoreOrderInfoModel> options =
                    new FirebaseRecyclerOptions.Builder<StoreOrderInfoModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("order").child("0").child(model.getID()).child("Meun"), StoreOrderInfoModel.class)
                            .build();

            orderInfoAdapter = new OrderInfoAdapter(options);
            holder.Orderrv.setAdapter(orderInfoAdapter);

            orderInfoAdapter.startListening();
        } else {

        }
        holder.orderStatus = new OrderStatus() {
            @Override
            public void statuschange(View v, int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("完成訂單")
                        .setMessage("點擊確認移除訂單")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order");
//                                reference.child(model.getID()).child("Status").setValue(1);
                                remove(model.getID());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        };
    }

    @NonNull
    @Override
    public storeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_order_recyclerview, parent, false);
        return new storeOrderViewHolder(v);
    }

    class storeOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTotal, tvTime, tvName, tvPhone, tvAddress, tvComment;
        ImageButton checkbtn;
        RecyclerView Orderrv;
        CardView cardView;
        OrderStatus orderStatus;

        public storeOrderViewHolder(@NonNull View itemView) {
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

            checkbtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //onOrderStatusChange.onOrderStatusChange();
            this.orderStatus.statuschange(v, getLayoutPosition());
        }

        public void setDataClickListener(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

    }

    public interface OrderStatus {
        void statuschange(View v, int position);
    }

    public void remove(String ID) {
        DatabaseReference fromreference = FirebaseDatabase.getInstance().getReference("order");
        Query query = fromreference.child("0").child(ID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order").child("1");
                Log.e("snap", String.valueOf(snapshot));
                reference.child(ID).setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        onRemoveCallback.onRemoveListener(ID);
                        reference.child(ID).child("Status").setValue(1);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
