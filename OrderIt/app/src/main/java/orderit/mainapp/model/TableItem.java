package orderit.mainapp.model;

import java.io.Serializable;

/**
 * Created by LanTuan on 12/22/15.
 */
public class TableItem implements Serializable {
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
