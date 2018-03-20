package com.example.hp.shoppersstop;

/**
 * Created by hp on 01-03-2018.
 */

public class ShopListItem {

    private String name, address, email, shop;
    private double rating;
    private boolean status;
    private int mob;

    public ShopListItem(){

    }

    public ShopListItem(String name, String address, Double rating, Boolean status){
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.status = status;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public boolean getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getShop() {
        return shop;
    }

    public int getMob() {
        return mob;
    }
}
