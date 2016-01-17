package orderit.mainapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import orderit.mainapp.R;

public class NewTableOrderActivity extends AppCompatActivity {

    private static final String ACTION_BAR_TITTLE = "Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order_new);
        getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
    }
}
