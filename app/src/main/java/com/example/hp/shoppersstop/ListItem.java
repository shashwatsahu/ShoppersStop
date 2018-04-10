package com.example.hp.shoppersstop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 17-02-2018.
 */

public class ListItem implements Parcelable {

    public static int nextId = 0;

    private String name, brand;
    private int quantity;
    private double price, weight;
    public int id = ++nextId;

    public ListItem() {

    }

    private ListItem(Parcel in) {
        this.name = in.readString();
        this.weight = in.readDouble();
        this.brand = in.readString();
        this.price = in.readDouble();
        this.quantity = in.readInt();
    }

    public ListItem (String name, String brand, int quant, double price, double weight) {
        this.name = name;
        this.brand = brand;
        this.quantity = quant;
        this.price = price;
        this.weight = weight;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeDouble(this.weight);
        dest.writeString(this.brand);
        dest.writeDouble(this.price);
        dest.writeInt(this.quantity);
    }


    public String getName(){
        return name;
    }

    public Double getWeight(){
        return weight;
    }

    public Double getPrice(){
        return price;
    }

    public Integer getQuant(){
        return quantity;
    }

    public String getBrand(){
        return brand;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.NAME_KEY, name);
        result.put(Constants.BRAND_KEY, brand);
        result.put(Constants.QUANTITY_KEY, quantity);
        result.put(Constants.PRICE_KEY, price);
        result.put(Constants.WEIGHT_KEY, weight);

        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ListItem> CREATOR
            = new Parcelable.Creator<ListItem>() {
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };


}
