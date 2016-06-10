package orderit.mainapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import android.content.ContentValues;
import android.util.Log;

public class PostgreDriver {
    private static String url = "jdbc:postgresql://ec2-54-163-239-28.compute-1.amazonaws.com:5432/d7mdp524vlsdf6?sslmode=require";
    private static String user = "dsiedwaaywinyo";
    private static String password = "42TlG1tdtoxVYhKQDRX15mNaNs";

    private static Connection conn;

    public Connection getConnection() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
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
            if(conn != null) {
                conn.close();
            }
        }
        catch (Exception e){

        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return -1;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return 0;
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        return 0;
    }
}