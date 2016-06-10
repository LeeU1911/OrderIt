/** History: Created by Hieu Thien
 * Description: Database access class
 */

package orderit.mainapp.database;

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

import orderit.mainapp.model.BillOrderItem;
import orderit.mainapp.model.MenuItem;
import orderit.mainapp.model.MenuGroup;
import orderit.mainapp.model.OrderItem;
import orderit.mainapp.model.OrderManager;
import orderit.mainapp.model.User;
import orderit.mainapp.model.TableItem;
import orderit.mainapp.database.PostgreDriver;

public class DatabaseAccess {
    private PostgreDriver database;
    private static DatabaseAccess instance;

    /** "users" table */
    private static final String USERS_TABLE = "users";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_USERNAME = "username";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USERS_COLUMN_BUSINESSID = "business_id";
    private static final String USERS_COLUMN_ROLEID = "role_id";
    private static final String USERS_COLUMN_CREATEDATE = "created";
    private static final String USERS_COLUMN_MODIFIEDDATE = "modified";

    /** "tables" table */
    private static final String TABLES_TABLE = "tables";
    private static final String TABLES_COLUMN_ID = "id";
    private static final String TABLES_COLUMN_BUSINESSID = "business_id";
    private static final String TABLES_COLUMN_NAME = "name";
    private static final String TABLES_COLUMN_STATUS = "status";
    private static final String TABLES_COLUMN_CREATEDATE = "created";
    private static final String TABLES_COLUMN_MODIFIEDDATE = "modified";

    /** "orders" table */
    private static final String ORDERS_TABLE = "orders";
    private static final String ORDERS_COLUMN_ID = "id";
    private static final String ORDERS_COLUMN_USERID = "user_id";
    private static final String ORDERS_COLUMN_TABLEID = "table_id";
    private static final String ORDERS_COLUMN_STATUS = "status";
    private static final String ORDERS_COLUMN_CREATEDATE = "created";
    private static final String ORDERS_COLUMN_MODIFIEDDATE = "modified";

    /** "order_details" table */
    private static final String ORDERDETAILS_TABLE = "order_details";
    private static final String ORDERDETAILS_COLUMN_ID = "id";
    private static final String ORDERDETAILS_COLUMN_ORDERID = "order_id";
    private static final String ORDERDETAILS_COLUMN_ITEMID = "item_id";
    private static final String ORDERDETAILS_COLUMN_ITEMQUANTITY = "item_quantity";
    private static final String ORDERDETAILS_COLUMN_CREATEDATE = "created";
    private static final String ORDERDETAILS_COLUMN_MODIFIEDDATE = "modified";

    /** Private constructor to avoid object creation from outside classes */

    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_BUSINESSID = "business_id";
    private static final String USER_COLUMN_ROLEID = "role_id";
    private static final String USER_COLUMN_CREATEDATE = "created";
    private static final String USER_COLUMN_MODIFIEDDATE = "modified";

    private static final String TABLE_USERS = "users";


    private static final int ITEM_CATEGORY_1_ID = 1;
    private static final int ITEM_CATEGORY_2_ID = 2;
    private static final int ITEM_CATEGORY_3_ID = 3;
    private static final int ITEM_CATEGORY_4_ID = 4;
    private static final int ITEM_CATEGORY_5_ID = 5;
    private static final int ITEM_CATEGORY_6_ID = 6;
    private static final int ITEM_CATEGORY_7_ID = 7;
    private static final int ITEM_CATEGORY_8_ID = 8;

    public static final int ORDER_STATUS_NEW = 1;
    public static final int ORDER_STATUS_ORDER = 2;
    public static final int ORDER_STATUS_PAID = 3;

    /** Return a singleton instance of DatabaseAccess */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess();
        }
        return instance;
    }

    /** Open the database connection */
    public void open() {
        this.database = new PostgreDriver();
    }

    /** Close the database connection */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read table name from the database.
     *
     * @return a list of table name
     */
    public List<TableItem> InitTableItems() {
        List<TableItem> list = new ArrayList<>();
        try {
            ResultSet rs = database.rawQuery("SELECT * FROM tables", null);
            while (rs.next()) {
                TableItem tableItem = new TableItem();

                tableItem.setId(rs.getInt(0));
                tableItem.setBusinessId(rs.getInt(1));
                tableItem.setName(rs.getString(2));
                tableItem.setStatus(rs.getInt(3));

                list.add(tableItem);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return list;
    }

    /** Search password */
    public String SearchPassword(String userName)
    {
        String query = "Select " + USERS_COLUMN_USERNAME + ", " + USERS_COLUMN_PASSWORD + " from " + USERS_TABLE;
        ResultSet rs = database.rawQuery(query, null);
        String _userName, _password = "12345677890-qrwereytryuuiuiyuisdfsdfggfhgjhkljklkl;czxcxzcvcbcvnvbmnbm,,<><><:";
        try {
            while (rs.next()) {
                _userName = rs.getString(0);
                if (userName.equalsIgnoreCase(_userName)) {
                    _password = rs.getString(1);
                    break;
                }
            }
        }
        catch(Exception e){

        }
        return _password;
    }

    /** Insert user to local database */
    public void InsertUser(User u)
    {
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_ID, u.getId());
        values.put(USERS_COLUMN_USERNAME, u.getUserName());
        values.put(USERS_COLUMN_PASSWORD, u.getPassword());
        values.put(USERS_COLUMN_BUSINESSID, u.getBusinessId());
        values.put(USERS_COLUMN_ROLEID, u.getRoleId());
        this.database.insert(USERS_TABLE, null, values);
    }

    /** Get list of bill order details */
    public List<BillOrderItem> getBillOrderItemListByOrderID(int orderID) {
        List<BillOrderItem> list = new ArrayList<>();
        String query = "SELECT order_details.item_id,items.name,order_details.item_quantity,items.price,items.average_make_time FROM order_details" +
                " LEFT JOIN items ON items.id = order_details.item_id WHERE order_details.order_id = " + orderID;
        ResultSet rs = database.rawQuery(query, null);
        try {
            while (rs.next()) {
                BillOrderItem billOrderItem = new BillOrderItem();

                billOrderItem.setId(rs.getInt(0));
                billOrderItem.setOrderName(rs.getString(1));
                billOrderItem.setQuantity(rs.getInt(2));
                billOrderItem.setSinglePrice(rs.getInt(3));
                billOrderItem.setTime(rs.getInt(4));

                list.add(billOrderItem);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return list;
    }

    /**
     * Read Menu from the database.
     *
     * @return a hash map of Menu
     */
    public Map<Integer, MenuItem> InitMenu() {
        Map<Integer, MenuItem> menuManager = new LinkedHashMap<Integer, MenuItem>();

        ResultSet rs = database.rawQuery("SELECT * FROM items", null);
        try {
            while (rs.next()) {
                // Create new item
                MenuItem menuItem = new MenuItem();

                menuItem.setId(rs.getInt(0));
                menuItem.setName(rs.getString(1));
                menuItem.setPrice(rs.getInt(2));
                menuItem.setPriceUnit(rs.getString(3));
                menuItem.setAveMakeTime(rs.getInt(4));
                menuItem.setAveMakeTimeUnit(rs.getString(5));
                menuItem.setCategoryId(rs.getInt(8));

                menuManager.put(menuItem.getId(), menuItem);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return menuManager;
    }

    public OrderManager QueryOrderInfoByTableID(int tableID) {
        OrderManager orderManager = new OrderManager();

        ResultSet rs = database.rawQuery("SELECT * FROM " + ORDERS_TABLE +
                " WHERE " + ORDERS_COLUMN_TABLEID + "=" + tableID + " AND " + ORDERS_COLUMN_STATUS + "<" + ORDER_STATUS_PAID, null);

        try {
            if (rs.getRow() > 0) {
                while (rs.next()) {
                    orderManager.setId(rs.getInt(0));
                    orderManager.setUserId(rs.getInt(1));
                    orderManager.setTableId(rs.getInt(2));
                    orderManager.setStatus(rs.getInt(3));
                }
            } else {
                orderManager.setUserId(1); // Temporarily assign
                orderManager.setTableId(tableID);
                orderManager.setStatus(ORDER_STATUS_NEW);

                // Insert new order information to database
                ContentValues cv = new ContentValues(5);

                cv.put(ORDERS_COLUMN_USERID, 1); // Temporarily assign
                cv.put(ORDERS_COLUMN_TABLEID, tableID);
                cv.put(ORDERS_COLUMN_STATUS, ORDER_STATUS_NEW);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cv.put(ORDERS_COLUMN_CREATEDATE, dateFormat.format(new Date())); //Insert 'now' as the date
                cv.put(ORDERS_COLUMN_MODIFIEDDATE, dateFormat.format(new Date())); //Insert 'now' as the date

                long insertId = database.insert(ORDERS_TABLE, null, cv);

                if (insertId != -1) {
                    orderManager.setId(safeLongToInt(insertId));
                }
            }

            rs.close();
        }
        catch(Exception e){

        }
        return orderManager;
    }

    public int queryOrderStatusIdByOrderId(int orderId) {
        int status = -1;

        ResultSet rs = database.rawQuery("SELECT " + ORDERS_COLUMN_STATUS + " FROM " + ORDERS_TABLE +
                " WHERE " + ORDERS_COLUMN_ID + "=" + orderId, null);
        try {
            while (rs.next()) {
                status = rs.getInt(0);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return status;
    }

    public String queryOrderStatusStringByOrderId(int orderId) {
        String status = "";

        ResultSet rs = database.rawQuery("SELECT order_status.name FROM orders" +
                " LEFT JOIN order_status ON orders.status = order_status.id WHERE orders.id = " + orderId, null);
        try {
            while (rs.next()) {
                status = rs.getString(0);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return status;
    }

    public boolean UpdateOrderStatus4SpecifiedOrderId(int orderId, int newStatus) {
        ContentValues cv = new ContentValues();
        cv.put(ORDERS_COLUMN_STATUS, newStatus);
        return database.update(ORDERS_TABLE, cv, ORDERS_COLUMN_ID + "=" + orderId, null) > 0;
    }

    public Map<Integer, OrderItem> QueryOrderItemByOrderID(int orderID) {
        Map<Integer, OrderItem> orderItemMap = new LinkedHashMap<Integer, OrderItem>();

        ResultSet rs = database.rawQuery("SELECT * FROM order_details where order_id="+orderID+"", null);
        try {
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();

                orderItem.setId(rs.getInt(0));
                orderItem.setOrderId(rs.getInt(1));
                orderItem.setMenuItemId(rs.getInt(2));
                orderItem.setMenuItemQuantity(rs.getInt(3));

                orderItemMap.put(orderItem.getMenuItemId(), orderItem);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return orderItemMap;
    }

    public List<MenuGroup> QueryGroupMenu() {
        List<MenuGroup> list = new ArrayList<MenuGroup>();

        ResultSet rs = database.rawQuery("SELECT * FROM categories", null);
        try {
            while (rs.next()) {
                MenuGroup menuGroup = new MenuGroup();

                menuGroup.setId(rs.getInt(0));
                menuGroup.setName(rs.getString(1));

                list.add(menuGroup);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return list;
    }

    public Map<Integer, List<MenuItem>> makeMenu(List<MenuGroup> menuGroups, Map<Integer, OrderItem> orderItemMap ) {
        Map<Integer, List<MenuItem>> menuMap = new LinkedHashMap<Integer, List<MenuItem>>();

        for (MenuGroup menuGroup : menuGroups) {

            List<MenuItem> menuItemList = new ArrayList<MenuItem>();

            int totalOrderQuantity = 0;

            ResultSet rs = database.rawQuery("SELECT * FROM items where category_id="+menuGroup.getId()+"", null);
            try {
                while (rs.next()) {
                    // Create new item
                    MenuItem menuItem = new MenuItem();

                    menuItem.setId(rs.getInt(0));
                    menuItem.setName(rs.getString(1));
                    menuItem.setPrice(rs.getInt(2));
                    menuItem.setPriceUnit(rs.getString(3));
                    menuItem.setAveMakeTime(rs.getInt(4));
                    menuItem.setAveMakeTimeUnit(rs.getString(5));
                    menuItem.setCategoryId(rs.getInt(8));

                    if (orderItemMap.get(menuItem.getId()) != null) {
                        totalOrderQuantity += orderItemMap.get(menuItem.getId()).getMenuItemQuantity();
                    }

                    menuItemList.add(menuItem);
                }
                rs.close();
            }
            catch(Exception e){

            }
            menuGroup.setOrderQuantity(totalOrderQuantity);
            menuMap.put(menuGroup.getId(), menuItemList);
        }

        return menuMap;
    }

    public int QueryTableIdByOrderId(int OrderId) {
        int tableId = 0;

        ResultSet rs = database.rawQuery("SELECT table_id FROM orders where id="+OrderId+"", null);
        try {
            while (rs.next()) {
                tableId = rs.getInt(0);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return tableId;
    }

    public int QueryTableStatusByTableId(int TableId) {
        int tableStatus = 0;

        ResultSet rs = database.rawQuery("SELECT status FROM tables where id="+TableId+"", null);
        try {
            while (rs.next()) {
                tableStatus = rs.getInt(0);
            }
            rs.close();
        }
        catch(Exception e){

        }
        return tableStatus;
    }

    public void UpdateOrderQuantity(int orderId, int itemId, int newQuantity) {
        ContentValues cv = new ContentValues();
        cv.put(ORDERDETAILS_COLUMN_ITEMQUANTITY, newQuantity);
        database.update(ORDERDETAILS_TABLE, cv, ORDERDETAILS_COLUMN_ORDERID + "=" + orderId + " and " + ORDERDETAILS_COLUMN_ITEMID + "=" + itemId, null);
    }

    public void InsertNewOrder(OrderItem newOrder) {
        ContentValues cv = new ContentValues(5);

        cv.put(ORDERDETAILS_COLUMN_ORDERID, newOrder.getOrderId());
        cv.put(ORDERDETAILS_COLUMN_ITEMID, newOrder.getMenuItemId());
        cv.put(ORDERDETAILS_COLUMN_ITEMQUANTITY, newOrder.getMenuItemQuantity());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(ORDERDETAILS_COLUMN_CREATEDATE, dateFormat.format(new Date())); //Insert 'now' as the date
        cv.put(ORDERDETAILS_COLUMN_MODIFIEDDATE, dateFormat.format(new Date())); //Insert 'now' as the date

        database.insert(ORDERDETAILS_TABLE, null, cv);
    }

    public void DeleteOrder(int orderId, int itemId) {
        database.delete(ORDERDETAILS_TABLE, ORDERDETAILS_COLUMN_ORDERID + "=" + orderId + " and " + ORDERDETAILS_COLUMN_ITEMID + "=" + itemId, null);
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
