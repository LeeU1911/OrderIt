package orderit.mainapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import orderit.mainapp.R;
import orderit.mainapp.dialog.OrderPopupDialog;

/**
 * Created by LanTuan on 1/19/16.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private LayoutInflater layoutInflater;

    private List<OrderGroupItem> categoryList = new ArrayList<OrderGroupItem>();

    private Map<OrderGroupItem, List<OrderChildItem>> orderList = new LinkedHashMap<OrderGroupItem, List<OrderChildItem>>();

    private Map<OrderGroupItem, List<OrderChildItem>> filteredList = new LinkedHashMap<OrderGroupItem, List<OrderChildItem>>();

    private Filter                  filter;

    protected int selGroupPosition = -1;
    protected int selChildPosition = -1;


    public ExpandableListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addCategory(OrderGroupItem groupItems, List<OrderChildItem> childItems) {
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
            OrderChildItem orderChildItem = (OrderChildItem)this.getChild(groupPosition, childPosition);
            final String diskName = orderChildItem.getChildName();
            final int diskCnt = orderChildItem.getOrderCnt();


            convertView = layoutInflater.inflate(R.layout.order_child_item, null);

            TextView disk = (TextView) convertView.findViewById(R.id.txtOrderDisk);
            disk.setText(diskName);
            TextView diskNo = (TextView) convertView.findViewById(R.id.txtOrderDiskNo);
            diskNo.setText(String.format("%s", diskCnt));

            ImageView btnAdd = (ImageView) convertView.findViewById(R.id.btnOrderAdd);
            btnAdd.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    selGroupPosition = groupPosition;
                    selChildPosition = childPosition;

                    DialogFragment newFragment = OrderPopupDialog.newInstance(ExpandableListAdapter.this, context, diskName, diskCnt);
                    FragmentActivity activity = (FragmentActivity)(context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    newFragment.show(fm, "popup_dialog");
                }
            });
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
            OrderGroupItem orderGroupItem = (OrderGroupItem)this.getGroup(groupPosition);

            int resourceId = orderGroupItem.isHeader() ?
                        R.layout.order_group_header :
                        R.layout.order_group_item;

            convertView = layoutInflater.inflate(resourceId, null);


            if(!orderGroupItem.isHeader()) {
                TextView item = (TextView) convertView.findViewById(R.id.tvOrderCat);
                item.setText(orderGroupItem.getCatName());
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

    /**
     * callback method from QuantityDialogFragment, returning the value of user
     * input.
     *
     * @param changedValue
     */
    public void onUserChangedDiskCnt(int changedValue) {

        // TODO add your implementation.
        if((selGroupPosition >= 0) && (selChildPosition >= 0)) {
            Log.d("TAG", String.format("%s", changedValue));
            OrderChildItem orderChildItem = (OrderChildItem)this.getChild(selGroupPosition, selChildPosition);
            orderChildItem.setOrderCnt(changedValue);
            notifyDataSetChanged();
        }
    }
}
