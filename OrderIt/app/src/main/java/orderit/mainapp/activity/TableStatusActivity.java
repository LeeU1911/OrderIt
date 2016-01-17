package orderit.mainapp.activity;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import orderit.mainapp.R;

public class TableStatusActivity extends AppCompatActivity {

    public static final String ACTION_BAR_TITTLE = "Table Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(ACTION_BAR_TITTLE);
        setContentView(R.layout.activity_table_status);
        View order = findViewById(R.id.order_row);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableOrderIntent = new Intent(getApplicationContext(), TableOrderActivity.class);
                tableOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TaskStackBuilder builder = TaskStackBuilder.create(getApplicationContext());
                PendingIntent pendingIntent =
                        builder
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(tableOrderIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.startActivities();
                finish();
            }
        });
    }
}
