/** History: Created by Hieu Thien
 * Description: Table list activity class
 */

package orderit.mainapp.activity;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.R;
import orderit.mainapp.model.TableItem;
import orderit.mainapp.model.TableItemAdapter;

public class TableListActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    /**
     * Variables
     */
    private ListView listView;
    private List<TableItem> tableItems;
    private SearchView searchView;
    private TableItemAdapter adapter;

    /**
     * Method of creating
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        /** Get table list by ID */
        listView = (ListView) findViewById(R.id.TableList);
        /** Get search view by ID */
        searchView = (SearchView) findViewById(R.id.SearchView);

        /** Query table list from database and assign to array */
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        tableItems = databaseAccess.getTableItems();
        databaseAccess.close();

        /** Data from data source(database) is assigned to ArrayAdapter */
        updateListView();

        /** Set event listener to ListView */
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tableStatusIntent = new Intent(getApplicationContext(), TableStatusActivity.class);
                tableStatusIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TaskStackBuilder builder = TaskStackBuilder.create(getApplicationContext());
                PendingIntent pendingIntent =
                        builder
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(tableStatusIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.startActivities();
                finish();
            }
        });

        /** Set event listener to SearchView */
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void updateListView() {
        /** Create the adapter and assign to ListView */
        adapter = new TableItemAdapter(this, tableItems);
        this.listView.setAdapter(adapter);
    }
}
