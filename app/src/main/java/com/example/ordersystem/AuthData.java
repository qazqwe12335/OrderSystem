package com.example.ordersystem;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthData extends Application {
    //全域變數的資料可以供所有頁面使用

    //資料1.UID 使用者識別的ID
    String StorageAuthUID;

    //2.購物車清單
    ArrayList<HashMap> Userorder = new ArrayList<>();

    //3.總金額
    // 4.總杯數
    int total, totalQuan;

    // 5.訂餐姓名
    // 6.訂餐地址
    // 7.訂餐電話
    String UserName, Address;
    String phone;

    //取得使用者識別碼
    public String getStorageAuthUID() {
        return StorageAuthUID;
    }

    //設定使用者識別碼
    public void setStorageAuthUID(String storageAuthUID) {
        StorageAuthUID = storageAuthUID;
    }

    //取得購物車裡所有資料
    public ArrayList<HashMap> getUserorder() {
        return Userorder;
    }

    //加入新品項至購物車中
    public void setUserorder(HashMap hashMap) {
        Userorder.add(hashMap);
    }

    //編輯購物車的某一筆資料，根據index的數 設定改變第幾筆資料
    public void modityUserOrder(int index, HashMap hashMap) {
        Userorder.set(index, hashMap);
    }

    //清除購物車的"某一筆"資料
    public void removeUserOrder(int index) {
        Userorder.remove(index);
    }

    //清除購物車的所有資料
    public void clearAllOrder() {
        total = 0;
        totalQuan = 0;
        Userorder.clear();
    }

    //取得總金額
    public int getTotal() {
        total = 0;
        for (int position = 0; position < Userorder.size(); position++) {
            HashMap<String, String> map = new HashMap<>();
            map = Userorder.get(position);
            if (map.get("ChooseCup").equals("L")) {
                total += Integer.valueOf(map.get("lprice")) * Integer.valueOf(map.get("cupQuantity"));

            } else if (map.get("ChooseCup").equals("M")) {
                total += Integer.valueOf(map.get("mprice")) * Integer.valueOf(map.get("cupQuantity"));

            }
        }
        return total;
    }

    //取得目前總杯數
    public int getTotalQuan() {
        totalQuan = 0;
        for (int position = 0; position < Userorder.size(); position++) {
            HashMap<String, String> map = new HashMap<>();
            map = Userorder.get(position);
            totalQuan += Integer.valueOf(map.get("cupQuantity"));
        }
        return totalQuan;
    }

    //設定增加總杯數
    public void setAddQuan(int position, String Quan) {
        Userorder.get(position).put("cupQuantity", Quan);
    }

    //取得送餐姓名
    public String getUserName() {
        return UserName;
    }

    //設定送餐姓名
    public void setUserName(String userName) {
        UserName = userName;
    }

    //取得送餐地址
    public String getAddress() {
        return Address;
    }

    //設定送餐地址
    public void setAddress(String address) {
        Address = address;
    }

    //取得送餐電話
    public String getPhone() {
        return phone;
    }

    //設定送餐電話
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
