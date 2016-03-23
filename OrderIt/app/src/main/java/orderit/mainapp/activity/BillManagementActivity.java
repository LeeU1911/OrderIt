package orderit.mainapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import orderit.mainapp.R;
import orderit.mainapp.model.BillOrderItem;
import orderit.mainapp.model.BillOrderItemAdapter;
import orderit.mainapp.database.DatabaseAccess;

public class BillManagementActivity extends AppCompatActivity {
    /**
     * Variables
     */
    private ListView orderList;
    private List<BillOrderItem> billOrderItemList;
    private BillOrderItemAdapter adapter;

    private static final String ACTION_BAR_TITTLE = "Bill";

    private static final int TAX = 17; // 17%
    private static final int DISCOUNT = 10; // 10%

    private TextView tvSubTotal;
    private TextView tvTax;
    private TextView tvDiscount;
    private TextView tvTotal;

    private int orderId;

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Receive order information from Table Status Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TableStatusActivity.ORDER_INFO);
        orderId = bundle.getInt(TableStatusActivity.ORDER_ID);

        orderList = (ListView) findViewById(R.id.OrderList);

        /** Display data on list view */
        createListViewData();

        /** Display price */
        tvSubTotal = (TextView) findViewById(R.id.txtSubTotalPrice);
        tvSubTotal.setText(String.format("$ %d", calculateSubTotal()));

        tvTax = (TextView) findViewById(R.id.txtTax);
        tvTax.setText(String.format("%d%%", TAX));

        tvDiscount = (TextView) findViewById(R.id.txtDiscount);
        tvDiscount.setText(String.format("%d%%", DISCOUNT));

        tvTotal = (TextView) findViewById(R.id.txtTotal);
        tvTotal.setText(String.format("$ %d", calculateTotal(calculateSubTotal(), TAX, DISCOUNT)));
    }

    private void createListViewData() {
        /** Query table list from database and assign to array */
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        billOrderItemList = databaseAccess.getBillOrderItemListByOrderID(orderId);
        databaseAccess.close();

        adapter = new BillOrderItemAdapter(this, billOrderItemList);
        this.orderList.setAdapter(adapter);
    }

    public void onBtnFinishClick(View view) {
        databaseAccess.open();
        databaseAccess.UpdateOrderStatus4SpecifiedOrderId(orderId, DatabaseAccess.ORDER_STATUS_PAID);
        databaseAccess.close();

        this.finish();
    }

    private int calculateSubTotal() {
        int subTotal = 0;

        for(BillOrderItem billOrderItem : billOrderItemList) {
            subTotal += (billOrderItem.getQuantity() * billOrderItem.getSinglePrice());
        }

        return subTotal;
    }

    private int calculateTotal(int subTotal, int tax, int discount) {
        int total = 0;
        int afterTax = subTotal*tax/100;

        total = (subTotal + afterTax) - (subTotal + afterTax)*discount/100;

        return total;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}