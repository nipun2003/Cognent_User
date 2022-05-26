package com.nipunapps.cognent_user.utils;

import androidx.annotation.NonNull;

public class Product {

    private String pid;
    private String Name;
    private int discount_price;
    private int original_price;
    private String image1;

    public Product(String pid, String name, int discount_price, int original_price, String image1) {
        this.pid = pid;
        Name = name;
        this.discount_price = discount_price;
        this.original_price = original_price;
        this.image1 = image1;
    }

    public Product(String pid) {
        this.pid = pid;
    }

    public Product() {
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "pid='" + pid + '\'' +
                ", Name='" + Name + '\'' +
                ", discount_price=" + discount_price +
                ", original_price=" + original_price +
                ", image1='" + image1 + '\'' +
                '}';
    }
}
