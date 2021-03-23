package com.example.ordersystem;

public class StoreOrderInfoModel {

    String ChooseCup, Item, cupQuantity, ice, sugar, lprice, mprice;

    StoreOrderInfoModel(){

    }

    public StoreOrderInfoModel(String ChooseCup, String Item, String cupQuantity, String ice, String sugar, String lprice, String mprice) {
        this.ChooseCup = ChooseCup;
        this.Item = Item;
        this.cupQuantity = cupQuantity;
        this.ice = ice;
        this.sugar = sugar;
        this.lprice = lprice;
        this.mprice = mprice;
    }

    public String getChooseCup() {
        return ChooseCup;
    }

    public void setChooseCup(String chooseCup) {
        ChooseCup = chooseCup;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getCupQuantity() {
        return cupQuantity;
    }

    public void setCupQuantity(String cupQuantity) {
        this.cupQuantity = cupQuantity;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }
}
