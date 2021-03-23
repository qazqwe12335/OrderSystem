package com.example.ordersystem;

public class OKModel {
    String Address, CustomerName, Phone, Comment, ID;
    int Total, Status;
    long ArrivedTime;

    OKModel() {

    }

    public OKModel(String Address, String CustomerName, String Phone, String Comment, String ID, int Total, int Status, long ArrivedTime) {
        this.Address = Address;
        this.CustomerName = CustomerName;
        this.Phone = Phone;
        this.Comment = Comment;
        this.ID = ID;
        this.Total = Total;
        this.Status = Status;
        this.ArrivedTime = ArrivedTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
        Phone = phone;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public long getArrivedTime() {
        return ArrivedTime;
    }

    public void setArrivedTime(long arrivedTime) {
        ArrivedTime = arrivedTime;
    }
}
