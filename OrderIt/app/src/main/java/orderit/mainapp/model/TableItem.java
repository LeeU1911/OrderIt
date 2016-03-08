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

    private String tableName;
    private int tableStatId = TABLE_STATUS_AVAILABLE;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableStatId() {
        return tableStatId;
    }

    public void setTableStatId(int tableStat) {
        this.tableStatId = tableStat;
    }
}
