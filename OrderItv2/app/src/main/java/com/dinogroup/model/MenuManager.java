package com.dinogroup.model;

import java.util.Map;

/**
 * Created by LanTuan on 25/3/2017.
 */
public class MenuManager {
    private Map<Integer, MenuCategory> categoryList;

    public MenuManager(Map<Integer, MenuCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public Map<Integer, MenuCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Map<Integer, MenuCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
