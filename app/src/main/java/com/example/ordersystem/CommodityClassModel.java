package com.example.ordersystem;

public class CommodityClassModel {
    String CommodityClass, ClassName, ClassImage;

    CommodityClassModel(){

    }

    public CommodityClassModel(String ClassName, String ClassImage, String CommodityClass) {
        this.ClassImage = ClassImage;
        this.ClassName = ClassName;
        this.CommodityClass = CommodityClass;
    }

    public String getCommodityClass() {
        return CommodityClass;
    }

    public void setCommodityClass(String commodityClass) {
        CommodityClass = commodityClass;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassImage() {
        return ClassImage;
    }

    public void setClassImage(String classImage) {
        ClassImage = classImage;
    }
}
