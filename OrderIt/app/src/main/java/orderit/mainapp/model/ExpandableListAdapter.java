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
import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.dialog.OrderPopupDialog;

/**
 * Created by LanTuan on 1/19/16.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private LayoutInflater layoutInflater;

    private List<MenuGroup> groupMenuList;
    private Map<Integer, List<MenuItem>> mapMenu;
    private Map<Integer, List<MenuItem>> mapFilteredMenu;
    private Map<Integer, OrderItem> orderItemMap;

    private Filter                  filter;

    protected int selGroupPosition = -1;
    protected int selChildPosition = -1;

    private DatabaseAccess databaseAccess;


    public ExpandableListAdapter(Context context,
                                 List<MenuGroup> groupMenuList,
                                 Map<Integer, List<MenuItem>> mapMenu,
                                 Map<Integer, List<MenuItem>> mapFilteredMenu,
                                 Map<Integer, OrderItem> orderItemMap,
                                 DatabaseAccess databaseAccess) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupMenuList = groupMenuList;
        this.mapMenu = mapMenu;
        this.mapFilteredMenu = mapFilteredMenu;
        this.orderItemMap = orderItemMap;
        this.databaseAccess = databaseAccess;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.mapFilteredMenu.get(this.groupMenuList.get(groupPosition).getId()).get(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        return this.mapFilteredMenu.get(this.groupMenuList.get(groupPosition).getId()).size();
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        try {
            MenuItem menuItem = (MenuItem)this.getChild(groupPosition, childPosition);
            final String name = menuItem.getName();
            int value = 0;
            if(orderItemMap.get(menuItem.getId()) != null) {
                value = orderItemMap.get(menuItem.getId()).getMenuItemQuantity();
            }
            final int quantity = value;

            convertView = layoutInflater.inflate(R.layout.order_child_item, null);

            TextView disk = (TextView) convertView.findViewById(R.id.txtOrderDisk);
            disk.setText(name);
            TextView diskNo = (TextView) convertView.findViewById(R.id.txtOrderDiskNo);
            diskNo.setText(String.format("%s", quantity));

            ImageView btnAdd = (ImageView) convertView.findViewById(R.id.btnOrderAdd);
            btnAdd.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    selGroupPosition = groupPosition;
                    selChildPosition = childPosition;

                    DialogFragment newFragment = OrderPopupDialog.newInstance(ExpandableListAdapter.this, context, name, quantity);
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
        return this.groupMenuList.get(groupPosition);
    }

    public int getGroupCount() {
        return this.groupMenuList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try {
            MenuGroup menuGroup = (MenuGroup)this.getGroup(groupPosition);

            int resourceId = menuGroup.isHeader() ?
                        R.layout.order_group_header :
                        R.layout.order_group_item;

            convertView = layoutInflater.inflate(resourceId, null);


            if(!menuGroup.isHeader()) {
                TextView item = (TextView) convertView.findViewById(R.id.tvOrderCat);
                item.setText(menuGroup.getName());
                item.setTypeface(null, Typeface.BOLD);
            }

            TextView txtQuantity = (TextView) convertView.findViewById(R.id.tvOrderCnt);
            if(menuGroup.getOrderQuantity() > 0) {
                txtQuantity.setText(String.format("%s", menuGroup.getOrderQuantity()));
            }else {
                txtQuantity.setText("");
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
            MenuItem menuItem = (MenuItem)this.getChild(selGroupPosition, selChildPosition);
            MenuGroup menuGroup = (MenuGroup)this.getGroup(selGroupPosition);
            if(orderItemMap.get(menuItem.getId()) != null) {
                int curOrderQuantity = orderItemMap.get(menuItem.getId()).getMenuItemQuantity();
                orderItemMap.get(menuItem.getId()).setMenuItemQuantity(changedValue);

                // Update database
                databaseAccess.open();
                databaseAccess.UpdateOrderQuantity(orderItemMap.get(menuItem.getId()).getOrderId(), menuItem.getId(), changedValue);
                databaseAccess.close();

                int curGrpQuantity = menuGroup.getOrderQuantity();
                menuGroup.setOrderQuantity(curGrpQuantity + (changedValue - curOrderQuantity));
            }
            notifyDataSetChanged();
        }
    }
}
