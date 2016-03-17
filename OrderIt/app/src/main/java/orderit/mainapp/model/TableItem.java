package orderit.mainapp.model;

import java.io.Serializable;

/**
 * Created by LanTuan on 12/22/15.
 */

public class TableItem implements Serializable {

    public static final int TABLE_STATUS_AVAILABLE = 0;
    public static final int TABLE_STATUS_NOT_AVAILABLE = 1;
    public static final int TABLE_STATUS_STARTER_ORDER = 2;
    public static final int TABLE_STATUS_MAIN_ORDER = 3;
    public static final int TABLE_STATUS_DESSERT_ORDER = 4;
    public static final int TABLE_STATUS_DRINK_ORDER = 5;

    private int     id;
    private int     businessId;
    private String  name;
    private int     status = TABLE_STATUS_AVAILABLE;

    private OrderManager    orderManager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
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

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }
}
