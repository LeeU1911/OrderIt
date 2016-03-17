package orderit.mainapp.activity;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import orderit.mainapp.R;
import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.model.ExpandableListAdapter;
import orderit.mainapp.model.MenuItem;
import orderit.mainapp.model.MenuGroup;
import orderit.mainapp.model.OrderChildItem;
import orderit.mainapp.model.OrderItem;

public class NewTableOrderActivity extends AppCompatActivity {

    private static final String ACTION_BAR_TITTLE = "Order";

    private static final int GROUP_ID_RANGE_DRINK = 10; // 1 ~ 10
    private static final int GROUP_ID_HEADER_RECENT = 101;
    private static final int GROUP_ID_RECENT = 102;
    private static final int GROUP_ID_HEADER_TOPRANK = 103;
    private static final int GROUP_ID_TOPRANK = 104;

    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;

    private int orderId;

    public Map<Integer, OrderItem> orderItemMap;
    private Map<Integer, MenuItem> menuManager;


    public List<MenuGroup> groupMenuList;
    public Map<Integer, List<MenuItem>> mapMenu;
    public Map<Integer, List<MenuItem>> mapFilteredMenu;


    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order_new);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
        }

        // Receive order information from Table Status Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TableStatusActivity.ORDER_INFO);
        orderId = bundle.getInt(TableStatusActivity.ORDER_ID);

        /** Query Order Item base on Order ID from database */
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        orderItemMap = databaseAccess.QueryOrderItemByOrderID(orderId);
        databaseAccess.close();

        /** Init menu item */
        databaseAccess.open();
        menuManager = databaseAccess.InitMenu();
        databaseAccess.close();

        makeMenuGroupData();
        makeMenuItemData();

        this.expListView = (ExpandableListView) findViewById(R.id.OrderList);
        this.expListAdapter = new ExpandableListAdapter(this, groupMenuList, mapMenu, mapFilteredMenu, orderItemMap, databaseAccess);
        this.expListView.setAdapter(this.expListAdapter);

        //setGroupIndicatorToRight();
        this.expListView.setGroupIndicator(null);
    }

    private void makeMenuGroupData() {

        // Init menu group
        databaseAccess.open();
        groupMenuList = databaseAccess.QueryGroupMenu();
        databaseAccess.close();

        // Init other group
        // Recent Header
        MenuGroup headerRecent = new MenuGroup();
        headerRecent.setId(GROUP_ID_HEADER_RECENT);
        headerRecent.setIsHeader(true);
        groupMenuList.add(headerRecent);

        // Recent
        MenuGroup recent = new MenuGroup();
        recent.setId(GROUP_ID_RECENT);
        recent.setName("Recent");
        groupMenuList.add(recent);

        // Top Ranking Header
        MenuGroup headerTopRank = new MenuGroup();
        headerTopRank.setId(GROUP_ID_HEADER_TOPRANK);
        headerTopRank.setIsHeader(true);
        groupMenuList.add(headerTopRank);

        // Top Ranking
        MenuGroup topRank = new MenuGroup();
        topRank.setId(GROUP_ID_TOPRANK);
        topRank.setName("Top Ranking");
        groupMenuList.add(topRank);
    }

    private void makeMenuItemData() {
        // Init menu group
        databaseAccess.open();
        mapFilteredMenu = mapMenu = databaseAccess.makeMenu(groupMenuList, orderItemMap);
        databaseAccess.close();
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width - getDipsFromPixel(50), width - getDipsFromPixel(10));
        } else {
            expListView.setIndicatorBoundsRelative(width - getDipsFromPixel(50), width - getDipsFromPixel(10));
        }
        //expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
         //       - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backBtnClick();
                break;
            default:
                break;
        }

        return true;
    }

    private void backBtnClick() {
        Intent tableStatusIntent = new Intent(getApplicationContext(), TableStatusActivity.class);
        tableStatusIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        databaseAccess.open();
        int tableId, tableStatus;
        tableId = databaseAccess.QueryTableIdByOrderId(orderId);
        //tableStatus = databaseAccess.QueryTableStatusByTableId(tableId);
        tableStatus = 0;
        databaseAccess.close();
        // Pass table ID and Status to Table Status Activity

        Bundle bundle = new Bundle();
        bundle.putInt(TableListActivity.TABLE_ID, tableId);
        bundle.putInt(TableListActivity.TABLE_STATUS, tableStatus);
        tableStatusIntent.putExtra(TableListActivity.TABLE_INFO, bundle);

        TaskStackBuilder builder = TaskStackBuilder.create(getApplicationContext());
        PendingIntent pendingIntent =
                builder
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(tableStatusIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.startActivities();
        finish();
    }
}
