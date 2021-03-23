package com.example.ordersystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout us_acc_layout, us_pass_layout;
    TextInputEditText us_account, us_password;

    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    Button loginbtn, registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();
    }

    private void init() {
        us_acc_layout = findViewById(R.id.user_account_layout);
        us_pass_layout = findViewById(R.id.user_password_layout);
        us_account = findViewById(R.id.user_account);
        us_password = findViewById(R.id.user_password);
        loginbtn = findViewById(R.id.login_btn);
        registerbtn = findViewById(R.id.registerBtn);

        progressBar = findViewById(R.id.login_load_progress);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
    }

    //帳號或密碼的錯誤訊息函式
    private void showToast(int error_position) {
        String error = getResources().getString(R.string.login_edit_error);
        String login_errorcallback = getResources().getString(R.string.login_error);
        switch (error_position) {
            case 1:
                error = "帳號" + error;
                break;
            case 2:
                error = "密碼" + error;
                break;
            case 3:
                error = login_errorcallback;
                break;
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    //登入檢查
    public void login_onclick(View view) {
        String useraccount = us_account.getText().toString().trim();
        String userpassword = us_password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        //檢查帳號是否為空
        if (useraccount.isEmpty()) {
            showToast(1);
            progressBar.setVisibility(View.INVISIBLE);
            return;

            //檢查密碼是否為空
        } else if (userpassword.isEmpty()) {
            showToast(2);
            progressBar.setVisibility(View.INVISIBLE);
            //us_acc_layout.setError("");
            //us_password.setText("");
            //us_pass_layout.setError("密碼未輸入");
            return;

            //都不為空則進入firebase檢查 是否有此筆使用者資料(是否有在firebase註冊)
        } else {
            mAuth.signInWithEmailAndPassword(useraccount, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        getUserPermission();
                    } else {
                        showToast(3);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    //資料庫比對，比對登入權限
    private void getUserPermission() {

        /*
        Status 0 = 店家 , 1 = 客人
        */
        FirebaseUser currentFirebaseUser = mAuth.getCurrentUser();

        AuthData authData = (AuthData) getApplication();
        //儲存使用者識別ID
        authData.setStorageAuthUID(currentFirebaseUser.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkquery = reference.child(currentFirebaseUser.getUid());

        //成功取得使用者資料並儲存在全域變數中，根據權限進入對應頁面，若沒有取得成功則顯示"登入錯誤"
        checkquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int statusFromDB = snapshot.child("Status").getValue(int.class);
                    String Name = snapshot.child("Lastname").getValue(String.class) + snapshot.child("Surname").getValue(String.class);
                    String Address = snapshot.child("Address").getValue(String.class);
                    String Phone = snapshot.child("Phone").getValue(String.class);

                    authData.setUserName(Name);
                    authData.setAddress(Address);
                    authData.setPhone(Phone);

                    //根據權限進入不同頁面
                    Intent order_intent = null;
                    if (statusFromDB == 1) {
                        order_intent = new Intent(LoginActivity.this, ClientOrderActivity.class);
                    } else if (statusFromDB == 0) {
                        order_intent = new Intent(LoginActivity.this, StoreOrderActivity.class);
                    }
                    startActivity(order_intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登入錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return;
    }

    //註冊
    public void register(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    //忘記密碼
    public void forgetPassword(View view) {
        Intent resetPasswordIntent = new Intent(this, ResetPasswordActivity.class);
        startActivity(resetPasswordIntent);
    }
}