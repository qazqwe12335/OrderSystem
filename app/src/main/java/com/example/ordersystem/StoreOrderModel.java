package com.example.ordersystem;

public class StoreOrderModel {
    String Address, CustomerName, Phone, Comment, ID;
    int Total, Status;
    long ArrivedTime;

    StoreOrderModel() {

    }

    public StoreOrderModel(String Address, long ArrivedTime, String CustomerName, String Phone, String Comment, int Total, int Status, String ID) {
        this.Address = Address;
        this.ArrivedTime = ArrivedTime;
        this.CustomerName = CustomerName;
        this.Phone = Phone;
        this.Comment = Comment;
        this.Total = Total;
        this.Status = Status;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public long getArrivedTime() {
        return ArrivedTime;
    }

    public void setArrivedTime(long arrivedTime) {
        ArrivedTime = arrivedTime;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
