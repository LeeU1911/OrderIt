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
        TextView txtName = (TextView) convertView.findViewById(R.id.tvTableName);
        TableItem tableItem = tableItemList.get(position);
        txtName.setText(tableItem.getTableName());
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
}
