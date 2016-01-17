/** History: Created by Hieu Thien
 * Description: SQLite Asset helper class
 */

package orderit.mainapp.database;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "OrderIt.db";
    private static final int DATABASE_VERSION = 49;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
