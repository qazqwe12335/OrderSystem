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

import java.util.ArrayList;
import java.util.HashMap;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView modity_title, modity_edit;
    Button modity_cancel_btn, modity_OK_btn;
    ImageButton modity_decrease_btn, modity_add_btn;

    Spinner modity_sugar_spinner, modity_ice_spinner;

    RadioGroup modity_radio_group;
    RadioButton modity_mprice, modity_lprice;

    String moditytitle, moditychooseCup, moditymprice, moditylprice, moditysugar, modityice, moditycupQuantity;
    int listPosition;

    ArrayList<HashMap> info = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();

    //從購物車中修改某一筆點餐資訊的頁面(甜度 冰塊 杯數...等)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        init();

        modity_OK_btn.setOnClickListener(this);
        modity_cancel_btn.setOnClickListener(this);

        modity_add_btn.setOnClickListener(this);
        modity_decrease_btn.setOnClickListener(this);

        AuthData authData = (AuthData) getApplication();
        info = authData.getUserorder();

        //N = 選中的項
        //將第N筆要修改的資料取出，並顯示出來
        //修改後在放活地N項中
        listPosition = getIntent().getIntExtra("listPosition", 0);

        hashMap = info.get(listPosition);

        moditytitle = hashMap.get("Item");
        moditycupQuantity = hashMap.get("cupQuantity");
        moditychooseCup = hashMap.get("ChooseCup");
        moditysugar = hashMap.get("sugar");
        modityice = hashMap.get("ice");
        moditymprice = hashMap.get("mprice");
        moditylprice = hashMap.get("lprice");

        setInfo();

        modity_edit.setText(moditycupQuantity);

        modity_sugar_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                moditysugar = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        modity_ice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modityice = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void init() {
        modity_title = findViewById(R.id.modityTitle);
        modity_cancel_btn = findViewById(R.id.modityCancelbtn);
        modity_OK_btn = findViewById(R.id.modityOKbtn);

        modity_decrease_btn = findViewById(R.id.modity_decrease_btn);
        modity_add_btn = findViewById(R.id.modity_add_btn);

        modity_edit = findViewById(R.id.modity_edit);

        modity_decrease_btn = findViewById(R.id.modity_decrease_btn);

        modity_sugar_spinner = findViewById(R.id.sugar_modity);
        modity_ice_spinner = findViewById(R.id.ice_modity);

        modity_radio_group = findViewById(R.id.modity_priceRadioGroup);
        modity_mprice = findViewById(R.id.modity_mpriceRadiobtn);
        modity_lprice = findViewById(R.id.modity_lpriceRadiobtn);
    }

    //設定原先的甜訂冰塊及大小杯
    private void setInfo() {
        modity_title.setText(moditytitle);
        modity_mprice.setText("$ " + moditymprice);
        modity_lprice.setText("$ " + moditylprice);

        if (moditychooseCup.equals("M")) {
            modity_mprice.setChecked(true);
            modity_lprice.setChecked(false);
        } else if (moditychooseCup.equals("L")) {
            modity_mprice.setChecked(false);
            modity_lprice.setChecked(true);
        }

        ArrayAdapter<CharSequence> sugar = ArrayAdapter.createFromResource(this, R.array.sugar, android.R.layout.simple_spinner_item);
        sugar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modity_sugar_spinner.setAdapter(sugar);

        ArrayAdapter<CharSequence> ice = ArrayAdapter.createFromResource(this, R.array.ice, android.R.layout.simple_spinner_item);
        ice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modity_ice_spinner.setAdapter(ice);

        modity_sugar_spinner.setSelection(getIndex(modity_sugar_spinner, moditysugar));
        modity_ice_spinner.setSelection(getIndex(modity_ice_spinner, modityice));
    }

    private int getIndex(Spinner spinner, String mystring) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(mystring)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modityOKbtn:

                if (!(modity_sugar_spinner.getSelectedItemPosition() == 0 || modity_ice_spinner.getSelectedItemPosition() == 0)) {
                    if (modity_mprice.isChecked()) {
                        UserAtChooseCup("M");

                    } else if (modity_lprice.isChecked()) {
                        UserAtChooseCup("L");

                    }
                } else {
                    Toast.makeText(this, "甜度 / 冰塊 未選取", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("Item", moditytitle);
                hashMap.put("cupQuantity", moditycupQuantity);
                hashMap.put("ChooseCup", moditychooseCup);
                hashMap.put("sugar", moditysugar);
                hashMap.put("ice", modityice);
                hashMap.put("mprice", moditymprice);
                hashMap.put("lprice", moditylprice);

                Log.e("TAGG", String.valueOf(hashMap));

                AuthData authData = (AuthData) getApplication();
                authData.modityUserOrder(listPosition, hashMap);

                finish();
                break;
            case R.id.modityCancelbtn:
                finish();
                break;

            //加號
            case R.id.modity_decrease_btn:
                modity_edit.setText(CupQuan(false));
                break;

            //減號
            case R.id.modity_add_btn:
                modity_edit.setText(CupQuan(true));
                break;
        }
    }

    private void UserAtChooseCup(String cup) {
        moditychooseCup = cup;
    }

    //根據status判斷點擊的是加號還是減號 ，加號 status為true 減號為 false
    //減號按鈕最少減到1杯，1杯為底線
    private String CupQuan(boolean status) {
        if (status) {
            moditycupQuantity = String.valueOf(Integer.valueOf(moditycupQuantity) + 1);
        } else {
            if (Integer.valueOf(modity_edit.getText().toString().trim()) == 1) {

            } else {
                moditycupQuantity = String.valueOf(Integer.valueOf(moditycupQuantity) - 1);
            }
        }
        return moditycupQuantity;
    }
}