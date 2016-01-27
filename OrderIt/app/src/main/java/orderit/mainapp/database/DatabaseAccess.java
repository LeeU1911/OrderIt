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

import orderit.mainapp.model.User;
import orderit.mainapp.model.TableItem;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_BUSINESSID = "business_id";
    private static final String USER_COLUMN_ROLEID = "role_id";
    private static final String USER_COLUMN_CREATEDATE = "created";
    private static final String USER_COLUMN_MODIFIEDDATE = "modified";

    private static final String TABLE_USERS = "users";
    /**;
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
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
    public List<TableItem> getTableItems() {
        List<TableItem> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM tables", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableItem tableItem = new TableItem();
            tableItem.setTableName(cursor.getString(0));
            //tableItem.setTableStat(cursor.getString(1));

            list.add(tableItem);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public String SearchPassword(String userName)
    {
        this.database = openHelper.getReadableDatabase();
        String query = "Select " + USER_COLUMN_USERNAME + ", " + USER_COLUMN_PASSWORD + " from " + TABLE_USERS;
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

    public void InsertUser(User u)
    {
        this.database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //String query = "Select * from " + TABLE_USERS;
        //Cursor cursor = database.rawQuery(query, null);
        //int count = cursor.getCount();

        values.put(USER_COLUMN_USERNAME, u.getUserName());
        values.put(USER_COLUMN_PASSWORD, u.getPassword());
        values.put(USER_COLUMN_BUSINESSID, u.getBusinessId());
        values.put(USER_COLUMN_ROLEID, u.getRoleId());
        //values.put(USER_COLUMN_CREATEDATE, u.getCreateDate());
        //values.put(USER_COLUMN_MODIFIEDDATE, u.getModifiedDate());
        this.database.insert(TABLE_USERS, null, values);

        this.database.close();
    }

}
