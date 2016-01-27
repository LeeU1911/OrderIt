package orderit.mainapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import orderit.mainapp.R;
import orderit.mainapp.model.ExpandableListAdapter;
import orderit.mainapp.model.OrderChildItem;
import orderit.mainapp.model.OrderGroupItem;

public class NewTableOrderActivity extends AppCompatActivity {

    private static final String ACTION_BAR_TITTLE = "Order";

    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order_new);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
        }

        this.expListView = (ExpandableListView) findViewById(R.id.OrderList);

        this.expListAdapter = new ExpandableListAdapter(this);
        createTestData();

        this.expListView.setAdapter(this.expListAdapter);

        //setGroupIndicatorToRight();
        this.expListView.setGroupIndicator(null);
    }

    private void createTestData() {
        // Stater
        OrderGroupItem stater = new OrderGroupItem();
        stater.setIsHeader(false);
        stater.setCatName("Stater");
        List<OrderChildItem> staterChild = new ArrayList<OrderChildItem>();
        staterChild.add(new OrderChildItem("Soup1", 1));
        staterChild.add(new OrderChildItem("Soup2", 2));
        staterChild.add(new OrderChildItem("Soup3", 1));
        this.expListAdapter.addCategory(stater, staterChild);

        // Main
        OrderGroupItem main = new OrderGroupItem();
        main.setIsHeader(false);
        main.setCatName("Mains");
        List<OrderChildItem> mainChild = new ArrayList<OrderChildItem>();
        mainChild.add(new OrderChildItem("Chicken", 2));
        mainChild.add(new OrderChildItem("Fish", 2));
        mainChild.add(new OrderChildItem("Fork", 3));
        mainChild.add(new OrderChildItem("Egg", 4));
        this.expListAdapter.addCategory(main, mainChild);

        // Desert
        OrderGroupItem desert = new OrderGroupItem();
        desert.setIsHeader(false);
        desert.setCatName("Desert");
        List<OrderChildItem> desertChild = new ArrayList<OrderChildItem>();
        desertChild.add(new OrderChildItem("Fruit", 5));
        this.expListAdapter.addCategory(desert, desertChild);

        // Drinks
        OrderGroupItem drink = new OrderGroupItem();
        drink.setIsHeader(false);
        drink.setCatName("Drinks");
        List<OrderChildItem> drinkChild = new ArrayList<OrderChildItem>();
        drinkChild.add(new OrderChildItem("Beer", 50));
        drinkChild.add(new OrderChildItem("Coca cola", 2));
        drinkChild.add(new OrderChildItem("Pepsi", 4));
        this.expListAdapter.addCategory(drink, drinkChild);

        // Header1
        OrderGroupItem header1 = new OrderGroupItem();
        header1.setIsHeader(true);
        header1.setCatName("Header1");
        List<OrderChildItem> header1Child = new ArrayList<OrderChildItem>();
        this.expListAdapter.addCategory(header1, header1Child);

        // Recent
        OrderGroupItem recent = new OrderGroupItem();
        recent.setIsHeader(false);
        recent.setCatName("Recent");
        List<OrderChildItem> recentChild = new ArrayList<OrderChildItem>();
        this.expListAdapter.addCategory(recent, recentChild);

        OrderGroupItem header2 = new OrderGroupItem();
        header2.setIsHeader(true);
        header2.setCatName("Header2");
        List<OrderChildItem> header2Child = new ArrayList<OrderChildItem>();
        this.expListAdapter.addCategory(header2, header2Child);

        // Top Ranking
        OrderGroupItem topRank = new OrderGroupItem();
        topRank.setIsHeader(false);
        topRank.setCatName("Top Ranking");
        List<OrderChildItem> topRankChild = new ArrayList<OrderChildItem>();
        this.expListAdapter.addCategory(topRank, topRankChild);
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
}
