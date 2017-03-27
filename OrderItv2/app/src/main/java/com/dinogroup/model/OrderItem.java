package com.dinogroup.model;

/**
 * Created by LanTuan on 3/8/16.
 */
public class OrderItem {

    private int     id;
    private int     orderId;
    private int     menuItemId;
    private int     menuItemQuantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getMenuItemQuantity() {
        return menuItemQuantity;
    }

    public void setMenuItemQuantity(int menuItemQuantity) {
        this.menuItemQuantity = menuItemQuantity;
    }
}
