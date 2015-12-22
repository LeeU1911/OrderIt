/** History: Created by Hieu Thien
 * Description: Table list activity class
 */

package orderit.mainapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import orderit.mainapp.TableItem;
import java.util.ArrayList;
import java.util.List;

public class TableList extends ActionBarActivity {

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

        // Set event listener to ListView
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void updateListView() {
        // Create the adapter and assign to ListView
        TableItemAdapter adapter = new TableItemAdapter(this, tableItems);
        this.listView.setAdapter(adapter);
    }

    /**
     * Start ViewActivity to update a Contact.
     *
     * @param index the index of the contact
     */
    /*private void updateContact(int index) {
        TableItem contact = contacts.get(index);
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("CONTACT", contact);
        startActivity(intent);
    }*/

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
