package orderit.mainapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import orderit.mainapp.R;

public class BillManagementActivity extends AppCompatActivity {

    private static final String ACTION_BAR_TITTLE = "Bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
        }
    }
}