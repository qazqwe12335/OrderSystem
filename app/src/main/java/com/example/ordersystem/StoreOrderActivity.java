package com.example.ordersystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoreOrderActivity extends AppCompatActivity implements View.OnClickListener, OnRemoveCallback {

    //店家端
    private FloatingActionButton check, newOrderfab, checkOKfab;
    RecyclerView customerOrderrecyclerview;

    OrderOKAdapter okAdapter;
    StoreOrderAdapter storeOrderAdapter;
    CoordinatorLayout snackBarActivity;

    //動畫
    OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    Boolean Menustate = false;
    int translationy = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        check = findViewById(R.id.check);
        newOrderfab = findViewById(R.id.newOrder_fab);
        checkOKfab = findViewById(R.id.checkOK_fab);
        snackBarActivity = findViewById(R.id.snachbarlayout);

        customerOrderrecyclerview = findViewById(R.id.storeGetrv);

        newOrderfab.setTranslationY(translationy);
        newOrderfab.animate().alpha(0f).setDuration(100).start();
        checkOKfab.setTranslationY(translationy);
        checkOKfab.animate().alpha(0f).setDuration(100).start();

        check.setOnClickListener(this);
        newOrderfab.setOnClickListener(this);
        checkOKfab.setOnClickListener(this);

        //建立店家端 查看的訂餐者清單
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        customerOrderrecyclerview.setLayoutManager(linearLayoutManager);

        //目前訂單
        now_order();

    }

    private void openMenu() {
        Menustate = !Menustate;
        check.animate().translationY(0f).setInterpolator(overshootInterpolator).rotation(45).setDuration(300).start();
        newOrderfab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(300).start();
        newOrderfab.setVisibility(View.VISIBLE);
        checkOKfab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(300).start();
        checkOKfab.setVisibility(View.VISIBLE);

    }

    private void closeMenu() {
        Menustate = !Menustate;
        check.animate().translationY(0f).setInterpolator(overshootInterpolator).rotation(0).setDuration(300).start();
        newOrderfab.animate().translationY(translationy).alpha(0f).setInterpolator(overshootInterpolator).setDuration(300).start();
        newOrderfab.setVisibility(View.VISIBLE);
        checkOKfab.animate().translationY(translationy).alpha(0f).setInterpolator(overshootInterpolator).setDuration(300).start();
        checkOKfab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.check:
                if (Menustate) {
                    closeMenu();
                } else {
                    openMenu();
                    showSnachBar();
                }
                break;


            case R.id.newOrder_fab:
                Intent StoreChooseOrderIntent = new Intent(StoreOrderActivity.this, StoreChooseAddOrCancelActivity.class);
                startActivity(StoreChooseOrderIntent);
                break;

            //點擊進入 查看已完成的訂單
            case R.id.checkOK_fab:
                Intent check_intent = new Intent(this, CheckOkOrderActivity.class);
                startActivity(check_intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //不斷檢查新訂單是否加入
        storeOrderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止檢查新訂單是否加入
        storeOrderAdapter.stopListening();
    }

    @Override
    public void onRemoveListener(String id) {
        DatabaseReference fromreference = FirebaseDatabase.getInstance().getReference("order").child("0");
        fromreference.child(id).setValue(null);
        recreate();
    }

    private void now_order() {
        //目前訂單
        FirebaseRecyclerOptions<StoreOrderModel> options =
                new FirebaseRecyclerOptions.Builder<StoreOrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("order").child("0").orderByChild("ArrivedTime"), StoreOrderModel.class)
                        .build();

        storeOrderAdapter = new StoreOrderAdapter(options, this);
        customerOrderrecyclerview.setAdapter(storeOrderAdapter);
    }

    private void showSnachBar() {
        Snackbar snackbar = Snackbar.make(snackBarActivity, "收到新訂單", Snackbar.LENGTH_INDEFINITE)
                .setAction("點擊查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent StoreChooseOrderIntent = new Intent(StoreOrderActivity.this, StoreChooseAddOrCancelActivity.class);
                        startActivity(StoreChooseOrderIntent);
                    }
                })
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}