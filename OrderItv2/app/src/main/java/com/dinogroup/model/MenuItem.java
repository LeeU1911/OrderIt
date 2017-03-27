package com.dinogroup.model;

import java.io.Serializable;

/**
 * Created by LanTuan on 3/8/16.
 */
public class MenuItem implements Serializable {

    private String      id;
    private String      name;
    private int         price;
    private String      priceUnit;
    private int         aveMakeTime;
    private String      aveMakeTimeUnit;
    private String      categoryId;

    public MenuItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getAveMakeTime() {
        return aveMakeTime;
    }

    public void setAveMakeTime(int aveMakeTime) {
        this.aveMakeTime = aveMakeTime;
    }

    public String getAveMakeTimeUnit() {
        return aveMakeTimeUnit;
    }

    public void setAveMakeTimeUnit(String aveMakeTimeUnit) {
        this.aveMakeTimeUnit = aveMakeTimeUnit;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
