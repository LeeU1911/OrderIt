package orderit.mainapp.model;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by LanTuan on 3/8/16.
 */
public class OrderManager {

    private int     id;
    private int     userId;
    private int     tableId;
    private int     status;
    private Map<Integer, OrderItem> orderItemManager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<Integer, OrderItem> getOrderItemManager() {
        return orderItemManager;
    }

    public void setOrderItemManager(Map<Integer, OrderItem> orderItemManager) {
        this.orderItemManager = orderItemManager;
    }
}
