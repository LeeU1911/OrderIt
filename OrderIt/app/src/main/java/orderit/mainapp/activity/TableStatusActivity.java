package orderit.mainapp.activity;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import orderit.mainapp.R;
import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.model.OrderManager;

public class TableStatusActivity extends AppCompatActivity {

    public static final String ACTION_BAR_TITTLE = "Table Status";

    public final static String ORDER_INFO = "orderInfo";
    public final static String ORDER_ID = "orderId";

    private int tableId;
    private int tableStatus;
    private OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
        }
        setContentView(R.layout.activity_table_status);

        // Receive table information from Table List Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TableListActivity.TABLE_INFO);
        tableId = bundle.getInt(TableListActivity.TABLE_ID);
        tableStatus = bundle.getInt(TableListActivity.TABLE_STATUS);

        /** Query Order Information of specified Table ID from database */
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        orderManager = databaseAccess.QueryOrderInfoByTableID(tableId);
        databaseAccess.close();


        View order = findViewById(R.id.order_row);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableOrderIntent = new Intent(getApplicationContext(), NewTableOrderActivity.class);
                tableOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Pass Order ID to Table Order Activity
                Bundle bundle = new Bundle();
                bundle.putInt(ORDER_ID, orderManager.getId());
                tableOrderIntent.putExtra(ORDER_INFO, bundle);

                TaskStackBuilder builderOrder = TaskStackBuilder.create(getApplicationContext());
                PendingIntent pendingOrderIntent =
                        builderOrder
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(tableOrderIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builderOrder.startActivities();
                finish();
            }
        });

        View bill = findViewById(R.id.bill_row);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableBillIntent = new Intent(getApplicationContext(), BillManagementActivity.class);
                tableBillIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TaskStackBuilder builderBill = TaskStackBuilder.create(getApplicationContext());
                PendingIntent pendingBillIntent =
                        builderBill
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(tableBillIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builderBill.startActivities();
                finish();
            }
        });
    }
}
