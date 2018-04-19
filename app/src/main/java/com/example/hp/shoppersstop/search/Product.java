package com.example.hp.shoppersstop.search;

import com.example.hp.shoppersstop.Constants;

import java.util.HashMap;
import java.util.Map;

public class Product {
   private String name, brand;
   private Double price, weight;

    public Product() {
    }

    public Product(String name, String brand, double price, double weight){
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.weight = weight;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.NAME_KEY, name);
        result.put(Constants.BRAND_KEY, brand);
        result.put(Constants.PRICE_KEY, price);
        result.put(Constants.WEIGHT_KEY, weight);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
