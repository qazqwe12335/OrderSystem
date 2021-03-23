package com.example.ordersystem;

import java.util.HashMap;

public class OrderInfo {

    HashMap<String, String> Userorder = new HashMap<>();

    public OrderInfo(HashMap<String, String> userorder) {
        Userorder = userorder;
    }

    public HashMap getUserorder() {
        return Userorder;
    }

    public void setUserorder(HashMap userorder) {
        Userorder = userorder;
    }
}
