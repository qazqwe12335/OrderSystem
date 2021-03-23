package com.example.ordersystem;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClientCartActivity extends AppCompatActivity implements View.OnClickListener, OnDelCallback {

    Button send_btn;
    TextView totalText, totalQuanText, timeCheck, userInfo;
    EditText cartComment;

    RecyclerView recyclerView;

    ArrayList<OrderInfo> orderInfoArrayList;
    CartRecyclerViewAdapter adapter;
    AuthData authData;

    int Hour, Minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_cart);

        init();
        send_btn.setOnClickListener(this);
        timeCheck.setOnClickListener(this);
        userInfo.setOnClickListener(this);

        authData = (AuthData) getApplication();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        orderInfoArrayList = new ArrayList<>();
//        getOrderInfo();

        //建立購物車清單
        adapter = new CartRecyclerViewAdapter(this, orderInfoArrayList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init() {
        send_btn = findViewById(R.id.cart_sendOrder_btn);
        totalText = findViewById(R.id.totalText);
        totalQuanText = findViewById(R.id.totalQuanText);
        timeCheck = findViewById(R.id.timeCheck);
        cartComment = findViewById(R.id.cart_comment);

        recyclerView = findViewById(R.id.Cartrecyclerview);
        userInfo = findViewById(R.id.UserIfo);
    }

    @Override
    protected void onStart() {
        super.onStart();


        //顯示總金額、總杯數
        orderInfoArrayList.clear();
        getOrderInfo();
        adapter.notifyDataSetChanged();

        totalText.setText("總金額 : " + String.valueOf(authData.getTotal()) + " 元");
        totalQuanText.setText("總共 " + String.valueOf(authData.getTotalQuan()) + " 杯");
    }

    private void getOrderInfo() {
        //從全域變數中取出每一項顯示在清單上
        for (int i = 0; i < authData.getUserorder().size(); i++) {
            orderInfoArrayList.add(new OrderInfo(authData.getUserorder().get(i)));
            Log.e("a", String.valueOf(authData.getUserorder().get(i)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //送出餐點
            case R.id.cart_sendOrder_btn:
                long unixTime;
                if (timeCheck.getText().toString().trim().equals("選擇取餐時間")) {
                    unixTime = System.currentTimeMillis() / 1000;
                } else {
                    unixTime = getOrderTime(Hour, Minute);
                }
                sendOrderInfo(unixTime);


                break;

            //選擇送餐時間按鈕，送餐時間不可以低於目前時間，只能示將來
            case R.id.timeCheck:
                Hour = Integer.valueOf(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
                Minute = Integer.valueOf(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Hour = Integer.valueOf(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
                                Minute = Integer.valueOf(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));

                                //若低於目前時間則顯示 時間選擇錯誤

                                if (hourOfDay == Hour && minute < Minute) {
                                    Toast.makeText(ClientCartActivity.this, "時間選擇錯誤", Toast.LENGTH_SHORT).show();
                                } else if (hourOfDay < Hour) {
                                    Toast.makeText(ClientCartActivity.this, "時間選擇錯誤", Toast.LENGTH_SHORT).show();
                                } else {
                                    Hour = hourOfDay;
                                    Minute = minute;

                                    String time = Hour + ":" + Minute;

                                    SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date date = f24Hours.parse(time);
                                        SimpleDateFormat f12Hours = new SimpleDateFormat("aa HH:mm");
                                        timeCheck.setText(f12Hours.format(date));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, 24, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(Hour, Minute);
                timePickerDialog.show();
                break;

            case R.id.UserIfo:
                Intent userInfoIntent = new Intent(this, UserInfoActivity.class);
                startActivity(userInfoIntent);
                break;
        }
    }


    //callback 在清單資料設定 的地方點擊刪除按鈕，會在這邊立刻更新 總金額 及 總杯數
    @Override
    public void onDelListener() {
        totalText.setText("總金額 : " + String.valueOf(authData.getTotal()) + " 元");
        totalQuanText.setText("總共 " + String.valueOf(authData.getTotalQuan()) + " 杯");
    }

    private long getOrderTime(int hh, int mm) {
        String Adate = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String Bdate = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String Cdate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.valueOf(Adate), Integer.valueOf(Bdate) - 1, Integer.valueOf(Cdate), hh, mm);
        cal.getTimeInMillis();
        return cal.getTimeInMillis() / 1000L;
    }

    //發送訂單功能，訂單編號為TotalNum
    private void sendOrderInfo(long unix) {

        final long[] count = new long[1];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order").child("0");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count[0] = Long.valueOf(snapshot.getChildrenCount());
                Log.e("AM", String.valueOf(count[0]));

                String stringCountChild = String.valueOf(count[0] + 1);

                Log.e("A", stringCountChild);

                AuthData authData = (AuthData) getApplication();
                String comment = null;
                if (!cartComment.getText().toString().isEmpty()) {
                    comment = cartComment.getText().toString().trim();
                } else {
                    comment = "";
                }
                DatabaseReference Numreference = FirebaseDatabase.getInstance().getReference("order");
                String finalComment = comment;
                Numreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String totalNum = String.valueOf(snapshot.child("TotalNum").getValue(int.class));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order");

                        totalNum = String.valueOf(Integer.valueOf(totalNum) + 1);
                        reference.child("TotalNum").setValue(Integer.valueOf(totalNum));
                        reference.child("0").child(totalNum).child("ID").setValue(totalNum);
                        reference.child("0").child(totalNum).child("Phone").setValue(authData.getPhone());
                        reference.child("0").child(totalNum).child("Comment").setValue(finalComment);
                        reference.child("0").child(totalNum).child("Address").setValue(authData.getAddress());
                        reference.child("0").child(totalNum).child("CustomerName").setValue(authData.getUserName());
                        reference.child("0").child(totalNum).child("Status").setValue(0);
                        reference.child("0").child(totalNum).child("ArrivedTime").setValue(unix);
                        reference.child("0").child(totalNum).child("Total").setValue(authData.getTotal());
                        reference.child("0").child(totalNum).child("customUID").setValue(authData.getStorageAuthUID());
                        reference.child("0").child(totalNum).child("Meun").setValue(authData.getUserorder());

                        orderInfoArrayList.clear();
                        authData.clearAllOrder();
                        getOrderInfo();
                        adapter.notifyDataSetChanged();

                        totalText.setText("總金額 : " + String.valueOf(authData.getTotal()) + " 元");
                        totalQuanText.setText("總共 " + String.valueOf(authData.getTotalQuan()) + " 杯");
                        cartComment.setText("");

                        Toast.makeText(ClientCartActivity.this, "訂單寄送完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order");
//
//                reference.child("0").child(stringCountChild).child("ID").setValue(stringCountChild);
//                reference.child("0").child(stringCountChild).child("Phone").setValue(authData.getPhone());
//                reference.child("0").child(stringCountChild).child("Comment").setValue(comment);
//                reference.child("0").child(stringCountChild).child("Address").setValue(authData.getAddress());
//                reference.child("0").child(stringCountChild).child("CustomerName").setValue(authData.getUserName());
//                reference.child("0").child(stringCountChild).child("Status").setValue(0);
//                reference.child("0").child(stringCountChild).child("ArrivedTime").setValue(unix);
//                reference.child("0").child(stringCountChild).child("Total").setValue(authData.getTotal());
//                reference.child("0").child(stringCountChild).child("customUID").setValue(authData.getStorageAuthUID());
//                reference.child("0").child(stringCountChild).child("Meun").setValue(authData.getUserorder());
//
//                orderInfoArrayList.clear();
//                authData.clearAllOrder();
//                getOrderInfo();
//                adapter.notifyDataSetChanged();
//
//                totalText.setText("總金額 : " + String.valueOf(authData.getTotal()) + " 元");
//                totalQuanText.setText("總共 " + String.valueOf(authData.getTotalQuan()) + " 杯");
//
//                Toast.makeText(ClientCartActivity.this, "訂單寄送完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}