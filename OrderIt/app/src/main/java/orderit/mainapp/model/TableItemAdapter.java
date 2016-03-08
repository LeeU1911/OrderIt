package orderit.mainapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import orderit.mainapp.R;

/**
 * Created by LanTuan on 1/16/16.
 */
public class TableItemAdapter extends ArrayAdapter<TableItem> {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<TableItem> tableItemList = null;
    private ArrayList<TableItem> dataList;


    public TableItemAdapter(Context context, List<TableItem> objects) {
        super(context, 0, objects);

        this.mContext = context;
        this.tableItemList = objects;
        this.inflater = LayoutInflater.from(mContext);
        this.dataList = new ArrayList<TableItem>();
        this.dataList.addAll(tableItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_row, parent, false);
        }

        TableItem tableItem = tableItemList.get(position);

        TextView txtName = (TextView) convertView.findViewById(R.id.tvTableName);
        txtName.setText(tableItem.getTableName());

        TextView txtStatus = (TextView) convertView.findViewById(R.id.tvTableStat);
        txtStatus.setText(getTableStatusbyId(tableItem.getTableStatId()));

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tableItemList.clear();
        if (charText.length() == 0) {
            tableItemList.addAll(dataList);
        }
        else
        {
            for (TableItem tableItem : dataList)
            {
                if (tableItem.getTableName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    tableItemList.add(tableItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    protected String getTableStatusbyId(int tableStatusId) {

        String tableStatus = "";
        switch (tableStatusId) {
            case TableItem.TABLE_STATUS_AVAILABLE:
                tableStatus = mContext.getString(R.string.table_status_available);
                break;
            case TableItem.TABLE_STATUS_NOT_AVAILABLE:
                tableStatus = mContext.getString(R.string.table_status_not_available);
                break;
            case TableItem.TABLE_STATUS_STARTER_ORDER:
                tableStatus = mContext.getString(R.string.table_status_starter);
                break;
            case TableItem.TABLE_STATUS_MAIN_ORDER:
                tableStatus = mContext.getString(R.string.table_status_main);
                break;
            case TableItem.TABLE_STATUS_DESSERT_ORDER:
                tableStatus = mContext.getString(R.string.table_status_dessert);
                break;
            case TableItem.TABLE_STATUS_DRINK_ORDER:
                tableStatus = mContext.getString(R.string.table_status_drink);
                break;
            default:
                tableStatus = mContext.getString(R.string.table_status_available);
                break;
        }

        return tableStatus;
    }
}
