package com.example.ordersystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//關於清單類別內容設定
public class CommodityClassRVAdapter extends FirebaseRecyclerAdapter<CommodityClassModel, CommodityClassRVAdapter.commodityViewholder> {

    int nowclicked = -2;
    List<MenuListModel> menuListModels;
    Context context;

    public CommodityClassRVAdapter(@NonNull FirebaseRecyclerOptions<CommodityClassModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull commodityViewholder holder, int position, @NonNull CommodityClassModel model) {
        holder.CommodityClassText.setText(model.getClassName());
        Glide.with(holder.CommodityClassRecyclerviewImage.getContext()).load(model.getClassImage()).into(holder.CommodityClassRecyclerviewImage);

        //到firebase取得清單中的"商品資料" ，商品清單會建立在 MenuListAdapter
        menuListModels = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("store").child("c1hi07bBstZixffgryillFxgd8t2").child("Class").child(model.getCommodityClass()).child("Menu");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Toast.makeText(context, String.valueOf(snapshot), Toast.LENGTH_SHORT).show();

                menuListModels.clear();

                for (DataSnapshot menu : snapshot.getChildren()) {
                    MenuListModel menulist = menu.getValue(MenuListModel.class);
                    menuListModels.add(menulist);
                }

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, menuListModels);
                holder.ShowMenuListview.setAdapter(menuListAdapter);
                //Toast.makeText(context, String.valueOf(snapshot), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if (nowclicked == -2) {
//            holder.ShowMenuLayout.setVisibility(View.GONE);
//            holder.ShowMenuListBtn.setImageResource(R.drawable.ic_open_menu);
//        } else if (nowclicked == position) {
//            holder.ShowMenuLayout.setVisibility(View.VISIBLE);
//            holder.ShowMenuListBtn.setImageResource(R.drawable.ic_close_menu);
//        } else {
//            holder.ShowMenuLayout.setVisibility(View.GONE);
//            holder.ShowMenuListBtn.setImageResource(R.drawable.ic_open_menu);
//        }
    }

    @NonNull
    @Override
    public commodityViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_commodity_listview_in_recyclerview, parent, false);
        return new commodityViewholder(view);
    }

    class commodityViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //清單中需要的資料
        //1.類別名
        //2.類別圖片

        ImageView CommodityClassRecyclerviewImage;
        TextView CommodityClassText;
        ImageButton ShowMenuListBtn;
        ListView ShowMenuListview;
        LinearLayout ShowMenuLayout, recyclerviewOutermostLayout;

        public commodityViewholder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            CommodityClassRecyclerviewImage = itemView.findViewById(R.id.review_layout_image);
            CommodityClassText = itemView.findViewById(R.id.commodity_Type);
            ShowMenuListBtn = itemView.findViewById(R.id.show_menu_imagebtn);
            ShowMenuListview = itemView.findViewById(R.id.show_menu_listview);
            ShowMenuLayout = itemView.findViewById(R.id.show_menu_layout);
            recyclerviewOutermostLayout = itemView.findViewById(R.id.recyclerviewOutermostLayout);

            ShowMenuListBtn.setOnClickListener(this);
        }

        //菜單開關功能
        @Override
        public void onClick(View v) {
//            if (nowclicked == getAdapterPosition()) {
//                ShowMenuLayout.setVisibility(View.GONE);
//                ShowMenuListBtn.setImageResource(R.drawable.ic_open_menu);
//                nowclicked = -1;
//
//                ViewGroup.LayoutParams cdparams = (ViewGroup.LayoutParams) recyclerviewOutermostLayout.getLayoutParams();
//                cdparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                recyclerviewOutermostLayout.setLayoutParams(cdparams);
//            } else {
//                nowclicked = getAdapterPosition();
//                notifyDataSetChanged();
//            }
        }
    }
}
