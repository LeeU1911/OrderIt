package orderit.mainapp.model;

import java.io.Serializable;

/**
 * Created by LanTuan on 12/22/15.
 */
public class TableItem implements Serializable {
    private String tableName;
    private String tableStat;
    private int tableIcon;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableStat() {
        return tableStat;
    }

    public void setTableStat(String tableStat) {
        this.tableStat = tableStat;
    }

    public int getTableIcon() {
        return tableIcon;
    }

    public void setTableIcon(int tableIcon) {
        this.tableIcon = tableIcon;
    }
}
