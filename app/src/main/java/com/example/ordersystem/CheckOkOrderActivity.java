package com.example.ordersystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckOkOrderActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView OKrecyclerview;
    OrderOKAdapter okAdapter;

    TextView earnTV, dateTV;
    FloatingActionButton dateChoose;
    Calendar calendar;
    int Y, M, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ok_order);

        OKrecyclerview = findViewById(R.id.OKrv);

        earnTV = findViewById(R.id.dateearn);
        dateTV = findViewById(R.id.dateTv);
        dateChoose = findViewById(R.id.datechoosefab);

        //建立以完成 訂單
        LinearLayoutManager oklinearLayoutManager = new LinearLayoutManager(this);
        oklinearLayoutManager.setStackFromEnd(true);
        oklinearLayoutManager.setReverseLayout(true);
        OKrecyclerview.setLayoutManager(oklinearLayoutManager);
        //設定今日時間
        calendar = Calendar.getInstance();
        dateTV.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
        //選擇日期
        dateChoose.setOnClickListener(this);

        Y = calendar.get(Calendar.YEAR);
        M = calendar.get(Calendar.MONTH);
        d = calendar.get(Calendar.DAY_OF_MONTH);

        Intent i = this.getIntent();
        Y = i.getIntExtra("Y", Y);
        M = i.getIntExtra("M", M);
        d = i.getIntExtra("d", d);
        dateTV.setText(Y + "年" + String.valueOf(Integer.valueOf(M) + 1) + "月" + d + "日");

        ok_order(UseDateGetOrder(Y, M, d, true), UseDateGetOrder(Y, M, d, false));
    }

    private void ok_order(double startTime, double endTime) {
        //完成訂單
        FirebaseRecyclerOptions<OKModel> options =
                new FirebaseRecyclerOptions.Builder<OKModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("order").child("1").orderByChild("ArrivedTime").startAt(startTime).endAt(endTime), OKModel.class)
                        .build();

        okAdapter = new OrderOKAdapter(options);
        OKrecyclerview.setAdapter(okAdapter);
        okAdapter.notifyDataSetChanged();

        //計算營業額
        getDateTotal(startTime, endTime);
    }

    //不斷檢查是否有新的 以完成訂單加入
    @Override
    protected void onStart() {
        super.onStart();
        okAdapter.startListening();
        Log.e("A", "A");
    }

    //停止檢查是否有新的 以完成訂單加入
    @Override
    protected void onStop() {
        super.onStop();
        okAdapter.stopListening();
    }

    //選擇查看訂單的日期
    @Override
    public void onClick(View v) {

        //取得目前日期 年/月/日
        int YY = calendar.get(Calendar.YEAR);
        int MM = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CheckOkOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Log.e("TAG", String.valueOf(UseDateGetOrder(year, month, dayOfMonth, false)));

                    //因為firebase 每次搜尋只能有一種排序，因此這邊每次選完日期便重新開啟頁面來重製firebase的清單排序
                    Intent restartIntent = new Intent(CheckOkOrderActivity.this, CheckOkOrderActivity.class);
                    restartIntent.putExtra("Y", year);
                    restartIntent.putExtra("M", month);
                    restartIntent.putExtra("d", dayOfMonth);
                    startActivity(restartIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }, YY, MM, dd);
            datePickerDialog.show();
        }
    }

    //將時間轉換為UNIX
    private long UseDateGetOrder(int DateYear, int DateMonth, int DateDay, boolean status) {
        Calendar cal = Calendar.getInstance();
        if (status) {
            cal.set(DateYear, DateMonth, DateDay, 0, 0, 0);
        } else {
            cal.set(DateYear, DateMonth, DateDay, 23, 59, 59);
        }
        cal.getTimeInMillis();
        return cal.getTimeInMillis() / 1000L;
    }

    //總營業額計算
    private void getDateTotal(double startTime, double endTime) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order");
        Query query = reference.child("1").orderByChild("ArrivedTime").startAt(startTime).endAt(endTime);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int dateTotal = 0;

                for (DataSnapshot total : dataSnapshot.getChildren()) {
                    dateTotal += total.child("Total").getValue(int.class);
                }
                earnTV.setText("今日營業額 : " + dateTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}