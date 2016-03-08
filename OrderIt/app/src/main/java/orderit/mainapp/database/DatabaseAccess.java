/** History: Created by Hieu Thien
 * Description: Database access class
 */

package orderit.mainapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import orderit.mainapp.model.BillOrderItem;
import orderit.mainapp.model.User;
import orderit.mainapp.model.TableItem;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
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
    private static final String ORDERDETAILS_COLUMN_ORDERID = "order_id";
    private static final String ORDERDETAILS_COLUMN_ITEMID = "item_id";
    private static final String ORDERDETAILS_COLUMN_ITEMQUANTITY = "item_quantity";
    private static final String ORDERDETAILS_COLUMN_STATUS = "status";
    private static final String ORDERDETAILS_COLUMN_CREATEDATE = "created";
    private static final String ORDERDETAILS_COLUMN_MODIFIEDDATE = "modified";

    /** "items" table */
 /**   private static final String ITEMS_TABLE = "items";
    private static final String ITEMS_COLUMN_ORDERID = "order_id";
    private static final String ITEMS_COLUMN_ITEMID = "item_id";
    private static final String ITEMS_COLUMN_ITEMQUANTITY = "item_quantity";
    private static final String ITEMS_COLUMN_STATUS = "status";
    private static final String ITEMS_COLUMN_CREATEDATE = "created";
    private static final String ITEMS_COLUMN_MODIFIEDDATE = "modified";
*/
    /** Private constructor to avoid object creation from outside classes */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /** Return a singleton instance of DatabaseAccess */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /** Open the database connection */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /** Close the database connection */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /** Read table name from the database */
    public List<TableItem> getTableItems() {
        List<TableItem> list = new ArrayList<>();
<<<<<<< HEAD
        String query = "Select " + TABLES_COLUMN_NAME + " from " + TABLES_TABLE;
        Cursor cursor = database.rawQuery(query, null);
=======
        Cursor cursor = database.rawQuery("SELECT name, status FROM tables", null);
>>>>>>> origin/master
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableItem tableItem = new TableItem();
            tableItem.setTableName(cursor.getString(0));
<<<<<<< HEAD
=======
            tableItem.setTableStatId(cursor.getInt(1));
>>>>>>> origin/master

            list.add(tableItem);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /** Search password */
    public String SearchPassword(String userName)
    {
        this.database = openHelper.getReadableDatabase();
        String query = "Select " + USERS_COLUMN_USERNAME + ", " + USERS_COLUMN_PASSWORD + " from " + USERS_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        String _userName, _password = "12345677890-qrwereytryuuiuiyuisdfsdfggfhgjhkljklkl;czxcxzcvcbcvnvbmnbm,,<><><:";
        if(cursor.moveToFirst())
        {
            do {
                _userName = cursor.getString(0);
                if(userName.equalsIgnoreCase(_userName))
                {
                    _password = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        this.database.close();
        return _password;
    }

    /** Insert user to local database */
    public void InsertUser(User u)
    {
        this.database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_ID, u.getId());
        values.put(USERS_COLUMN_USERNAME, u.getUserName());
        values.put(USERS_COLUMN_PASSWORD, u.getPassword());
        values.put(USERS_COLUMN_BUSINESSID, u.getBusinessId());
        values.put(USERS_COLUMN_ROLEID, u.getRoleId());
//        values.put(USERS_COLUMN_CREATEDATE, u.getCreateDate());
//        values.put(USERS_COLUMN_MODIFIEDDATE, u.getModifiedDate());
        this.database.insert(USERS_TABLE, null, values);
        this.database.close();
    }

    /** Get list of bill order details */
    public List<BillOrderItem> getBillOrderItemListByOrderID(int orderID) {
        List<BillOrderItem> list = new ArrayList<>();
        String query = "select items.name,order_details.item_quantity,items.price,items.price_unit from order_details" +
                " left join items on items.id = order_details.item_id and order_details.order_id = " + orderID;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BillOrderItem billOrderItem = new BillOrderItem();
            billOrderItem.setOrderName(cursor.getString(0));
            billOrderItem.setQuantity(cursor.getString(1));
            billOrderItem.setSinglePrice(cursor.getString(2));
            list.add(billOrderItem);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
