package com.example.ordersystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ClientOrderActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView client_recyclerView;
    CommodityClassRVAdapter commodityClassRVAdapter;
    FloatingActionButton cartfab;

    Button totalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
    }

    private void init() {
        client_recyclerView = findViewById(R.id.client_order_recyclerview);
        cartfab = findViewById(R.id.cart_fab);
        totalbtn = findViewById(R.id.totalbtn);

        cartfab.setOnClickListener(this);

        //設定商品類別顯示的清單 (紅茶 咖啡 冰沙...等)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        client_recyclerView.setLayoutManager(linearLayoutManager);

        //firebase路徑，用於取得商品類別資料，清單內容的設定在CommodityClassRVAdaoter.java頁面
        FirebaseRecyclerOptions<CommodityClassModel> options =
                new FirebaseRecyclerOptions.Builder<CommodityClassModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("store").child("c1hi07bBstZixffgryillFxgd8t2").child("Class"), CommodityClassModel.class)
                        .build();

        commodityClassRVAdapter = new CommodityClassRVAdapter(options);
        client_recyclerView.setAdapter(commodityClassRVAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //不斷檢查資料庫是否更新
        commodityClassRVAdapter.startListening();
        AuthData authData = (AuthData) getApplication();
        totalbtn.setText("總共 " + String.valueOf(authData.getTotal()) + " 元");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止檢查資料庫是否更新
        commodityClassRVAdapter.stopListening();
    }

    //關閉頁面(登出)，清空購物車所有資料
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AuthData authData = (AuthData) getApplication();
        authData.clearAllOrder();
    }

    //點擊進入購物車
    @Override
    public void onClick(View v) {
        Intent viewCart_intent = new Intent(this, ClientCartActivity.class);
        startActivity(viewCart_intent);
    }
}