package com.example.ordersystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner sugarSpinner, iceSpinner;
    String sugarChooseString = null, iceChooseString = null;
    Button OKbtn, Cancelbtn;
    ImageButton decrease_btn, add_btn;

    TextView title, edit;
    RadioGroup radioGroup;
    RadioButton mpriceRadiobtn, lpriceRadiobtn;

    String commoditytitle, Mprice, Lprice, chooseCup = "L", cupQuantity = "1";

    //從菜單中點擊其中一項後會進入到此頁面，此頁面可以做細項調整(甜度 冰塊 杯數...等)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        init();

        commoditytitle = getIntent().getStringExtra("CommodityName");
        Mprice = getIntent().getStringExtra("CommodityMcup");
        Lprice = getIntent().getStringExtra("CommodityLcup");

        title.setText(commoditytitle);
        mpriceRadiobtn.setText("$ " + Mprice);
        lpriceRadiobtn.setText("$ " + Lprice);

        edit.setText(cupQuantity);

        OKbtn.setOnClickListener(this);
        Cancelbtn.setOnClickListener(this);

        add_btn.setOnClickListener(this);
        decrease_btn.setOnClickListener(this);

        ArrayAdapter<CharSequence> sugar = ArrayAdapter.createFromResource(this, R.array.sugar, android.R.layout.simple_spinner_item);
        sugar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sugarSpinner.setAdapter(sugar);

        ArrayAdapter<CharSequence> ice = ArrayAdapter.createFromResource(this, R.array.ice, android.R.layout.simple_spinner_item);
        ice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iceSpinner.setAdapter(ice);

        sugarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sugarChooseString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iceChooseString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        sugarSpinner = findViewById(R.id.sugar_choose);
        iceSpinner = findViewById(R.id.icechoose);

        title = findViewById(R.id.chooseTitle);
        add_btn = findViewById(R.id.choose_add_btn);
        decrease_btn = findViewById(R.id.choose_decrease_btn);
        edit = findViewById(R.id.choose_edit);

        radioGroup = findViewById(R.id.priceRadioGroup);
        mpriceRadiobtn = findViewById(R.id.mpriceRadiobtn);
        lpriceRadiobtn = findViewById(R.id.lpriceRadiobtn);

        OKbtn = findViewById(R.id.chooseOKbtn);
        Cancelbtn = findViewById(R.id.chooseCancelbtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //取消按鈕
            case R.id.chooseCancelbtn:
                finish();
                break;

            //確認按鈕
            case R.id.chooseOKbtn:
                if (!(sugarSpinner.getSelectedItemPosition() == 0 || iceSpinner.getSelectedItemPosition() == 0)) {
                    if (mpriceRadiobtn.isChecked()) {
                        UserAtChooseCup("M");

                    } else if (lpriceRadiobtn.isChecked()) {
                        UserAtChooseCup("L");

                    }
                } else {
                    Toast.makeText(this, "甜度 / 冰塊 未選取", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("Item", commoditytitle);
                hashMap.put("cupQuantity", cupQuantity);
                hashMap.put("ChooseCup", chooseCup);
                hashMap.put("sugar", sugarChooseString);
                hashMap.put("ice", iceChooseString);
                hashMap.put("mprice", Mprice);
                hashMap.put("lprice", Lprice);

                Log.e("TAGG", String.valueOf(hashMap));

                AuthData authData = (AuthData) getApplication();

                boolean status = true;

                //檢查購物車中是否有同款品項，同款細項的訂單，如果有則加入杯數而不建立相同資料，若沒有則建立這筆資料在購物車中
                for (int i = 0; i < authData.getUserorder().size(); i++) {
                    if (hashMap.get("Item").equals(authData.getUserorder().get(i).get("Item"))) {
                        if (hashMap.get("ChooseCup").equals(authData.getUserorder().get(i).get("ChooseCup"))) {
                            if (hashMap.get("sugar").equals(authData.getUserorder().get(i).get("sugar"))) {
                                if (hashMap.get("ice").equals(authData.getUserorder().get(i).get("ice"))) {
                                    String q = (String) authData.getUserorder().get(i).get("cupQuantity");

                                    String Q = String.valueOf(
                                            Integer.valueOf(hashMap.get("cupQuantity")) + Integer.valueOf(q));
                                    authData.setAddQuan(i, Q);
                                    status = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (status) {
                    authData.setUserorder(hashMap);
                }
                finish();
                break;

            //加號
            case R.id.choose_add_btn:
                edit.setText(cupquan(true));
                break;

            //減號
            case R.id.choose_decrease_btn:
                edit.setText(cupquan(false));
                break;
        }
    }

    private void UserAtChooseCup(String cup) {
        chooseCup = cup;
    }

    //根據status判斷點擊的是加號還是減號 ，加號 status為true 減號為 false
    //減號按鈕最少減到1杯，1杯為底線
    private String cupquan(boolean status) {
        if (status) {
            cupQuantity = String.valueOf(Integer.valueOf(cupQuantity) + 1);
        } else {
            if (Integer.valueOf(edit.getText().toString().trim()) == 1) {
                cupQuantity = "1";
            } else {
                cupQuantity = String.valueOf(Integer.valueOf(cupQuantity) - 1);
            }
        }
        return cupQuantity;
    }
}