package com.example.hp.shoppersstop;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 17-02-2018.
 */

public class ListItem {
    private String name, brand;
    private int quantity;
    private double price, weight;

    public ListItem() {

    }

    public ListItem (String name, String brand, int quant, double price, double weight) {
        this.name = name;
        this.brand = brand;
        this.quantity = quant;
        this.price = price;
        this.weight = weight;
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
}