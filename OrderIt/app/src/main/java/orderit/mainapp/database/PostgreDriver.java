package orderit.mainapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import android.content.ContentValues;

public class PostgreDriver {
    private static String url = "jdbc:postgresql://dsiedwaaywinyo:42TlG1tdtoxVYhKQDRX15mNaNs@ec2-54-163-239-28.compute-1.amazonaws.com:5432/d7mdp524vlsdf6";
    private static String user = "dsiedwaaywinyo";
    private static String password = "42TlG1tdtoxVYhKQDRX15mNaNs";

    private static Connection conn;

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url,user,password);
        }
        catch (Exception e) {

        }
        return conn;
    }

    public ResultSet rawQuery(String sql, String[] selectionArgs){
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                rs = st.executeQuery(sql);
            }
        }
        catch (Exception e) {
        }
        return rs;
    }

    public void close(){
        try {
            conn.close();
        }
        catch (Exception e){

        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return -1;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        Set<String> keys = values.keySet();
        StringBuffer update = new StringBuffer();
        boolean end = false;
        for(String key: keys) {
            update.append(key);
            update.append("=");
            update.append(values.get(key));
            if (keys.size() == 1) {
                continue;
            }else {
                update.append(", ");
            }
        }
        update.delete(update.length() - 2, update.length());
        String query = "Update " + table + " set " + update.toString() + " where " + whereClause;
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        String query = "Delete from " + table + " where " + whereClause;
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }
}