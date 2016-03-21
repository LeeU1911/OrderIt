package orderit.mainapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList; /** Test */

import orderit.mainapp.R;
import orderit.mainapp.model.BillOrderItem;
import orderit.mainapp.model.BillOrderItemAdapter;
import orderit.mainapp.model.BillExpandListAdapter;
import orderit.mainapp.model.BillTotalChildItem;
import orderit.mainapp.model.BillTotalGroupItem;
import orderit.mainapp.database.DatabaseAccess;

public class BillManagementActivity extends AppCompatActivity {
    /**
     * Variables
     */
    private ListView orderList;
    private List<BillOrderItem> billOrderItemList = new ArrayList<>();
    private BillOrderItemAdapter adapter;
    ExpandableListView expListView;
    BillExpandListAdapter expListAdapter;
    private static final String ACTION_BAR_TITTLE = "Bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        orderList = (ListView) findViewById(R.id.OrderList);
        expListView = (ExpandableListView) findViewById(R.id.Bill);
        expListAdapter = new BillExpandListAdapter(this);

        /** Display data on list view */
        createListViewData();
    }

    private void createListViewData() {
        /** Query table list from database and assign to array */
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        billOrderItemList = databaseAccess.getBillOrderItemListByOrderID(1);
        databaseAccess.close();

        adapter = new BillOrderItemAdapter(this, billOrderItemList);
        this.orderList.setAdapter(adapter);

        BillTotalGroupItem Total = new BillTotalGroupItem();
        Total.setIsHeader(false);
        Total.setTitleItem("Total");
        List<BillTotalChildItem> staterChild = new ArrayList<BillTotalChildItem>();
        staterChild.add(new BillTotalChildItem("VAT", "10%"));
        staterChild.add(new BillTotalChildItem("Discount", "10%"));
        this.expListAdapter.addBillTotalItem(Total, staterChild);
        this.expListView.setAdapter(this.expListAdapter);
        this.expListView.setGroupIndicator(null);
    }

    public void onBtnFinishClick(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}