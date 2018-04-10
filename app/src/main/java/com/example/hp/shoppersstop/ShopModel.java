
package com.example.hp.shoppersstop;
public class ShopModel {

    private String shopName;
    private String displayPhotoUrl;
    private String shopContactNumber;
    private String distance;
    private String openTime;
    private String closeTime;
    private String address;
    private int ratings;

    public ShopModel(String shopName, String displayPhotoUrl, String shopContactNumber, String distance, String openTime, String closeTime, String address, int ratings) {
        this.shopName = shopName;
        this.displayPhotoUrl = displayPhotoUrl;
        this.shopContactNumber = shopContactNumber;
        this.distance = distance;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.ratings = ratings;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDisplayPhotoUrl() {
        return displayPhotoUrl;
    }

    public void setDisplayPhotoUrl(String displayPhotoUrl) {
        this.displayPhotoUrl = displayPhotoUrl;
    }

    public String getShopContactNumber() {
        return shopContactNumber;
    }

    public void setShopContactNumber(String shopContactNumber) {
        this.shopContactNumber = shopContactNumber;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
}
