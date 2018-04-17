
package com.example.hp.shoppersstop;
public class ShopModel {

    private String shopName;
    private String displayPhotoUrl;
    private String shopContactNumber;
    private String openTime;
    private String closeTime;
    private String address;
    private int ratings;
    private boolean isAuthenticated;
    private boolean isDeliveryAvailable;
    private long online;


    public ShopModel(String shopName, String displayPhotoUrl, String shopContactNumber, String openTime, String closeTime, String address, int ratings, boolean isAuthenticated, boolean isDeliveryAvailable,Long online) {
        this.shopName = shopName;
        this.displayPhotoUrl = displayPhotoUrl;
        this.shopContactNumber = shopContactNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.ratings = ratings;
        this.isAuthenticated = isAuthenticated;
        this.isDeliveryAvailable = isDeliveryAvailable;
        this.online = online;
    }


    public ShopModel() {
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


    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isDeliveryAvailable() {
        return isDeliveryAvailable;
    }

    public void setDeliveryAvailable(boolean deliveryAvailable) {
        isDeliveryAvailable = deliveryAvailable;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }
}
