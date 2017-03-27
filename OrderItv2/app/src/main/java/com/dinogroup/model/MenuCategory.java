package com.dinogroup.model;

import java.util.Map;

/**
 * Created by LanTuan on 25/3/2017.
 */
public class MenuCategory {
    private String id;
    private String name;
    private Map<Integer, MenuItem> itemList;

    public MenuCategory() {
        this.id = "";
        this.name = "";
    }

    public MenuCategory(String id, String name, Map<Integer, MenuItem> itemList) {
        this.id = id;
        this.name = name;
        this.itemList = itemList;
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

    public Map<Integer, MenuItem> getItemList() {
        return itemList;
    }

    public void setItemList(Map<Integer, MenuItem> itemList) {
        this.itemList = itemList;
    }
}
