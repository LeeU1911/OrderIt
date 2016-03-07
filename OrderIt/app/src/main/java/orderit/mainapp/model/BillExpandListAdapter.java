package orderit.mainapp.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import orderit.mainapp.R;

/**
 * Created by Blackcool on 3/3/16.
 */
public class BillExpandListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private LayoutInflater layoutInflater;

    private List<BillTotalGroupItem> categoryList = new ArrayList<BillTotalGroupItem>();

    private Map<BillTotalGroupItem, List<BillTotalChildItem>> orderList = new LinkedHashMap<BillTotalGroupItem, List<BillTotalChildItem>>();

    private Map<BillTotalGroupItem, List<BillTotalChildItem>> filteredList = new LinkedHashMap<BillTotalGroupItem, List<BillTotalChildItem>>();


    public BillExpandListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addBillTotalItem(BillTotalGroupItem groupItems, List<BillTotalChildItem> childItems) {
        this.categoryList.add(groupItems);
        this.orderList.put(groupItems, childItems);
        this.filteredList.put(groupItems, childItems);
        notifyDataSetChanged();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.filteredList.get(this.categoryList.get(groupPosition)).get(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        return this.filteredList.get(this.categoryList.get(groupPosition)).size();
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        try {
            BillTotalChildItem billChildItem = (BillTotalChildItem)this.getChild(groupPosition, childPosition);
            final String childName = billChildItem.getChildName();
            final String childValue = billChildItem.getValue();


            convertView = layoutInflater.inflate(R.layout.bill_total_child_item, null);

            TextView txtChild = (TextView) convertView.findViewById(R.id.txtChildName);
            txtChild.setText(childName);
            TextView txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            txtValue.setText(String.format("%s", childValue));
        } catch (Exception e) {
        }

        return convertView;
    }

    public Object getGroup(int groupPosition) {
        return this.categoryList.get(groupPosition);
    }

    public int getGroupCount() {
        return this.categoryList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try {
            BillTotalGroupItem billGroupItem = (BillTotalGroupItem)this.getGroup(groupPosition);

            int resourceId = billGroupItem.isHeader() ?
                    R.layout.order_group_header :
                    R.layout.bill_total_group_item;

            convertView = layoutInflater.inflate(resourceId, null);


            if(!billGroupItem.isHeader()) {
                TextView item = (TextView) convertView.findViewById(R.id.tvTotalName);
                item.setText(billGroupItem.getTitleItem());
                item.setTypeface(null, Typeface.BOLD);
            }

        }catch (Exception e) {
        }

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
