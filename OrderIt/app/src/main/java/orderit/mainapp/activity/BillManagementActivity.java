package orderit.mainapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private TextView tvTotal;

    private int orderId;

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_manage_bill);
        }

        /** Receive order information from Table Status Activity */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TableStatusActivity.ORDER_INFO);
        orderId = bundle.getInt(TableStatusActivity.ORDER_ID);

        orderList = (ListView) findViewById(R.id.OrderList);

        /** Display data on list view */
        createListViewData();

        /** Display price */
        tvTotal = (TextView) findViewById(R.id.txtTotal);
        tvTotal.setText(String.format("$ %d", calculateSubTotal()));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar_bill_manage, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print:
                printBtnClick();
                break;
            default:
                break;
        }

        return true;
    }

    public void printBtnClick(){
    }

    private int calculateSubTotal() {
        int subTotal = 0;

        for(BillOrderItem billOrderItem : billOrderItemList) {
            subTotal += (billOrderItem.getQuantity() * billOrderItem.getSinglePrice());
        }

        return subTotal;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}