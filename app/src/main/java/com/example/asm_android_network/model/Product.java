package com.example.asm_android_network.model;

import com.android.volley.toolbox.StringRequest;

import java.lang.ref.PhantomReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Product {
    
    private String title, image;
    private double price;
    private float rating;

    public Product() {
    }

    public Product (String title, double price, float rating, String image){

        this.title = title;
        this.image = image;
        this.rating = rating;
        this.price = price;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;

    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
