/** History: Created by Hieu Thien
 * Description: Table list activity class
 */

package OrderIt.MainApp;

import android.app.Activity;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.View;
import android.os.Bundle;

public class TableList extends Activity {

    /**
     * Variables
     */
    private ArrayList<String> arrayTableName = new ArrayList<String>(); /** array of table name */

    /**
     * Method of creating
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        /** Query table list from database and assign to array */
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        arrayTableName = databaseAccess.getTableList();
        databaseAccess.close();

        /** Get table list by ID */
        ListView tablelist = (ListView) findViewById(R.id.TableList);

        /** Data from data source(database) is assigned to ArrayAdapter */
        ArrayAdapter<String>adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, arrayTableName);
        /** ArrayAdapter is set to ListView */
        tablelist.setAdapter(adapter);

        /** Clicking each item of list view event */
        tablelist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1,
                                            int arg2,
                                            long arg3){

                    }
                }
        );
    }

}
