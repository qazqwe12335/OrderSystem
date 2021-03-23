package com.example.ordersystem;

public class MenuListModel {
    String ItemName;
    int LPrice, MPrice;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getLPrice() {
        return LPrice;
    }

    public void setLPrice(int LPrice) {
        this.LPrice = LPrice;
    }

    public int getMPrice() {
        return MPrice;
    }

    public void setMPrice(int MPrice) {
        this.MPrice = MPrice;
    }
}
