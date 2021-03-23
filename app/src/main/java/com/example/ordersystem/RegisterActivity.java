package com.example.ordersystem;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText regisEmail, regisPassword, regisPhone, regisAddress, regisFirstName, regisSurName, regisDoubleCheck;
    RadioButton maleRadio, femaleRadio;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        regisDoubleCheck = findViewById(R.id.register_password_doubleCheck);
        regisEmail = findViewById(R.id.register_email);
        regisPassword = findViewById(R.id.register_password);
        regisFirstName = findViewById(R.id.register_firstname);
        regisSurName = findViewById(R.id.register_surname);
        regisPhone = findViewById(R.id.register_phone);
        regisAddress = findViewById(R.id.register_address);

        maleRadio = findViewById(R.id.maleRadio);
        femaleRadio = findViewById(R.id.femaleRadio);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registercancel(View view) {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registerCheck(View view) {
        if (regisEmail.equals("")) {
            Toast.makeText(this, "帳號輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisPassword.equals("")) {
            Toast.makeText(this, "密碼輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisDoubleCheck.equals("")) {
            Toast.makeText(this, "密碼輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!regisDoubleCheck.equals(regisPassword.getText().toString().trim())) {
            Toast.makeText(this, "密碼驗證輸入錯誤", Toast.LENGTH_SHORT).show();
            regisDoubleCheck.setText("");
            regisDoubleCheck.getFocusable();
            return;
        }
        if (regisPassword.length() < 6) {
            Toast.makeText(this, "密碼長度不可低於6字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisFirstName.equals("")) {
            Toast.makeText(this, "姓名輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisSurName.equals("")) {
            Toast.makeText(this, "姓名輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisPhone.equals("")) {
            Toast.makeText(this, "電話輸入錯誤", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisPhone.length() < 10) {
            Toast.makeText(this, "電話長度不可少於10字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regisAddress.equals("")) {
            Toast.makeText(this, "外送地址錯誤", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String email = regisEmail.getText().toString().trim();
            String pass = regisPassword.getText().toString().trim();
            String firstname = regisFirstName.getText().toString().trim();
            String surname = regisSurName.getText().toString().trim();
            String phone = regisPhone.getText().toString().trim();
            String address = regisAddress.getText().toString().trim();
            int gender = 1;
            if (maleRadio.isChecked()) {
                gender = 1;
            } else if (femaleRadio.isChecked()) {
                gender = 0;
            }

            int finalGender = gender;
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        DatabaseReference registerReference = FirebaseDatabase.getInstance().getReference("user");
                        registerReference.child(firebaseAuth.getUid()).child("Address").setValue(address);
                        registerReference.child(firebaseAuth.getUid()).child("Lastname").setValue(firstname);
                        registerReference.child(firebaseAuth.getUid()).child("Phone").setValue(phone);
                        registerReference.child(firebaseAuth.getUid()).child("Sex").setValue(finalGender);
                        registerReference.child(firebaseAuth.getUid()).child("Status").setValue(1);
                        registerReference.child(firebaseAuth.getUid()).child("Surname").setValue(surname);

                        Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}