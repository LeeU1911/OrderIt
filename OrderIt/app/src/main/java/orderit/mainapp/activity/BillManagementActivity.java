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

        /** Query table list from database and assign to array */
        //DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        //databaseAccess.open();
        //billRowItem = databaseAccess.getBillRowItems();
        //databaseAccess.close();

        orderList = (ListView) findViewById(R.id.OrderList);
        expListView = (ExpandableListView) findViewById(R.id.Bill);
        expListAdapter = new BillExpandListAdapter(this);

        /** Test */
        createTestListView();
    }

    private void createTestListView() {
        BillOrderItem billOrderItem1 = new BillOrderItem();
        billOrderItem1.setOrderName("Bia Saigon");
        billOrderItem1.setQuantity("x10");
        billOrderItem1.setSinglePrice("150.000");
        billOrderItemList.add(billOrderItem1);

        BillOrderItem billOrderItem2 = new BillOrderItem();
        billOrderItem2.setOrderName("Hao nuong pho mai");
        billOrderItem2.setQuantity("x6");
        billOrderItem2.setSinglePrice("15.000");
        billOrderItemList.add(billOrderItem2);

        BillOrderItem billOrderItem3 = new BillOrderItem();
        billOrderItem3.setOrderName("Lau bo");
        billOrderItem3.setQuantity("x1");
        billOrderItem3.setSinglePrice("280.000");
        billOrderItemList.add(billOrderItem3);

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
}