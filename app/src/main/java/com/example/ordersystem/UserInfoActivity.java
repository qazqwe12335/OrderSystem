package com.example.ordersystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    EditText address, name, phone;
    Button cannel, okbtn;

    //變更送餐資訊 (訂餐者姓名 電話 地址)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        init();
        AuthData authData = (AuthData) getApplication();

        address.setText(authData.getAddress());
        name.setText(authData.getUserName());
        phone.setText(authData.getPhone());
    }

    private void init() {
        address = findViewById(R.id.userInfo_address);
        name = findViewById(R.id.userInfo_name);
        phone = findViewById(R.id.userInfo_phone);

        cannel = findViewById(R.id.cannel);
        okbtn = findViewById(R.id.ok);

        cannel.setOnClickListener(this);
        okbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cannel:
                finish();
                break;

                //點擊確認後將資料存回 全域變數中
            case R.id.ok:
                String Useraddress = address.getText().toString().trim();
                String Username = name.getText().toString().trim();
                String Userphone = phone.getText().toString().trim();

                AuthData authData = (AuthData) getApplication();
                authData.setAddress(Useraddress);
                authData.setUserName(Username);
                authData.setPhone(Userphone);

                finish();
                break;
        }
    }
}