package com.dinogroup.model;

import java.util.Map;

/**
 * Created by LanTuan on 12/22/15.
 */

public class TableItem {

    public static final int TABLE_STATUS_AVAILABLE = 0;
    public static final int TABLE_STATUS_NOT_AVAILABLE = 1;
    public static final int TABLE_STATUS_STARTER_ORDER = 2;
    public static final int TABLE_STATUS_MAIN_ORDER = 3;
    public static final int TABLE_STATUS_DESSERT_ORDER = 4;
    public static final int TABLE_STATUS_DRINK_ORDER = 5;

    private String  businessId;
    private String  id;
    private String  name;
    private int     status;

    private Map<Integer, OrderItem> orderList;

    public TableItem() {
        this.businessId = "";
        this.id = "";
        this.name = "Table 0";
        this.status = TABLE_STATUS_AVAILABLE;
    }

    public TableItem(String businessId, String id, String name, int status) {
        this.businessId = businessId;
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
