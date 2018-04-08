package com.example.hp.shoppersstop;

/**
 * Created by hp on 01-03-2018.
 */

public class ShopListItem {

    private String name, address, email, shopName;
    private double rating;
    private boolean status;
    private String mob;
    private String displayPhotoUrl;
    private String openTime, closeTime;

    public ShopListItem(){

    }

    public ShopListItem(String shopName, String address, Double rating, Boolean status){
        this.shopName = shopName;
        this.address = address;
        this.rating = rating;
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public String getDisplayPhotoUrl() {
        return displayPhotoUrl;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
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

}
