package com.dinogroup.model;

import java.util.List;

/**
 * Created by LanTuan on 25/3/2017.
 */
public class Business {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int type;       /* coffee shop, restaurant, ... */

    private List<TableItem> tableList;
    private MenuManager menuManager;

    public Business() {
    }

    public Business(String id, String name, String email, String phone, String address, int type, List<TableItem> tableList, MenuManager menuManager) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.type = type;
        this.tableList = tableList;
        this.menuManager = menuManager;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TableItem> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableItem> tableList) {
        this.tableList = tableList;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }
}
