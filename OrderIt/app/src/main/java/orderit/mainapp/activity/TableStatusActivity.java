package orderit.mainapp.activity;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import orderit.mainapp.R;
import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.model.MenuItem;
import orderit.mainapp.model.OrderItem;
import orderit.mainapp.model.OrderManager;
import android.app.Activity;

public class TableStatusActivity extends Activity {

    public static final String ACTION_BAR_TITTLE = "Table Status";

    public final static String ORDER_INFO = "orderInfo";
    public final static String ORDER_ID = "orderId";

    private int tableId;
    private int tableStatus;
    private OrderManager orderManager;

    private Map<Integer, MenuItem> menuManager;

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_status);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            tableId = savedInstanceState.getInt(TableListActivity.TABLE_ID);
            tableStatus = savedInstanceState.getInt(TableListActivity.TABLE_STATUS);
        } else {
            // Probably initialize members with default values for a new instance
            // Receive table information from Table List Activity
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(TableListActivity.TABLE_INFO);
            tableId = bundle.getInt(TableListActivity.TABLE_ID);
            tableStatus = bundle.getInt(TableListActivity.TABLE_STATUS);
        }

        /** Query Order Information of specified Table ID from database */
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        orderManager = databaseAccess.QueryOrderInfoByTableID(tableId);
        menuManager = databaseAccess.InitMenu();
        databaseAccess.close();


        View order = findViewById(R.id.order_row);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableOrderIntent = new Intent(TableStatusActivity.this, NewTableOrderActivity.class);

                // Pass Order ID to Table Order Activity
                Bundle bundle = new Bundle();
                bundle.putInt(ORDER_ID, orderManager.getId());
                tableOrderIntent.putExtra(ORDER_INFO, bundle);

                startActivity(tableOrderIntent);
            }
        });

        View bill = findViewById(R.id.bill_row);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableBillIntent = new Intent(TableStatusActivity.this, BillManagementActivity.class);
                // Pass Order ID to Bill Activity
                Bundle bundle = new Bundle();
                bundle.putInt(ORDER_ID, orderManager.getId());
                tableBillIntent.putExtra(ORDER_INFO, bundle);

                startActivity(tableBillIntent);;
            }
        });

        displayInformation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);

        // Save the user's current game state
        outState.putInt(TableListActivity.TABLE_ID, tableId);
        outState.putInt(TableListActivity.TABLE_STATUS, tableStatus);
    }

    private int getBill() {
        int totalExpense = 0;

        databaseAccess.open();
        Map<Integer, OrderItem> orderItemMap = databaseAccess.QueryOrderItemByOrderID(orderManager.getId());
        databaseAccess.close();

        for(Map.Entry<Integer, OrderItem> entry : orderItemMap.entrySet()) {
            int quantity = entry.getValue().getMenuItemQuantity();
            int price = menuManager.get(entry.getValue().getMenuItemId()).getPrice();

            totalExpense += price*quantity;
        }

        return totalExpense;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayInformation();
    }

    private void displayInformation() {
        TextView billIndicator = (TextView)findViewById(R.id.tbl_bill_indicator);
        billIndicator.setText(String.format("$%s", getBill()));

        TextView orderStatus = (TextView) findViewById(R.id.tbl_order_indicator);
        databaseAccess.open();
        orderStatus.setText(databaseAccess.queryOrderStatusStringByOrderId(orderManager.getId()));
        databaseAccess.close();
    }
}
