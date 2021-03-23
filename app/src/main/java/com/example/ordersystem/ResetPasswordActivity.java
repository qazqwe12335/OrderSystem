package com.example.ordersystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText changepasswordfromEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        changepasswordfromEmail = findViewById(R.id.changePasswordfromEmail);
    }

    public void ChangeCannel(View view) {
        finish();
    }

    //double check
    public void ChangePassword(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("確認")
                .setMessage("點擊確認後，系統將會寄送修改密碼郵件至輸入信箱，確認是否寄送？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //檢查是否輸入
                        if (changepasswordfromEmail.getText().toString().isEmpty()) {
                            Toast.makeText(ResetPasswordActivity.this, "信箱輸入錯誤", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //firebase 透過信箱寄送修改密碼連結
                        firebaseAuth.sendPasswordResetEmail(changepasswordfromEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ResetPasswordActivity.this, "系統已寄送修改密碼通知，請至信箱確認", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ResetPasswordActivity.this, "修改失敗", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).create()
                .show();

    }
}