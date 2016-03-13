package orderit.mainapp.model;

import java.io.Serializable;

/**
 * Created by LanTuan on 3/8/16.
 */
public class MenuItem implements Serializable {

    private int     id;

    private String  name;

    private int     price;
    private String  priceUnit;

    private int     aveMakeTime;
    private String  aveMakeTimeUnit;

    private int     categoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
