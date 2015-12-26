/** History: Created by Hieu Thien
 * Description: Table list activity class
 */

package orderit.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import orderit.mainapp.database.DatabaseAccess;
import orderit.mainapp.R;
import orderit.mainapp.model.TableItem;

public class TableListActivity extends AppCompatActivity {

    /**
     * Variables
     */
    private ListView listView;
    private List<TableItem> tableItems;

    /**
     * Method of creating
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        /** Get table list by ID */
        listView = (ListView) findViewById(R.id.TableList);

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
            }
        });
    }

    private void updateListView() {
        /** Create the adapter and assign to ListView */
        TableItemAdapter adapter = new TableItemAdapter(this, tableItems);
        this.listView.setAdapter(adapter);
    }


    /**
     * Custom ArrayAdapter for Contacts.
     */
    private class TableItemAdapter extends ArrayAdapter<TableItem> {


        public TableItemAdapter(Context context, List<TableItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_row, parent, false);
            }
            TextView txtName = (TextView) convertView.findViewById(R.id.tvTableName);
            TableItem tableItem = tableItems.get(position);
            txtName.setText(tableItem.getTableName());
            return convertView;
        }
    }

}
