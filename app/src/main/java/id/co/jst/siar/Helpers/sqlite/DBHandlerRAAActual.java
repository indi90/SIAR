package id.co.jst.siar.Helpers.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerRAAActual extends DBHandlerSQLite {

    public DBHandlerRAAActual(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RAAACTUAL + " ("
                + KEY_IRPERIODID + " INTEGER,"
                + KEY_IRASSETNO + " INTEGER,"
                + KEY_IRMODEL + " TEXT,"
                + KEY_IRMFGNO + " VARCHAR,"
                + KEY_IRLOCATIONID + " INTEGER,"
                + KEY_IRGENERATEDATE + " DATE,"
                + KEY_IRGENERATEUSER + " VARCHAR,"
                + KEY_IRDEPTCODE + " INTEGER,"
                + "PRIMARY KEY ("+ KEY_IRPERIODID +", "+ KEY_IRASSETNO +")"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAAACTUAL);
        // Creating tables again
        onCreate(db);
    }

    // Getting RAA Count
    public int getRAACount() {
        String countQuery = "SELECT * FROM " + TABLE_RAAACTUAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Truncate RAA
    public void truncateRAA() {
        String truncateQuery = "DELETE FROM " + TABLE_RAAACTUAL;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(truncateQuery);
//        Log.d("Reading: ", "Count Test ..." + rs.next());
//        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
//        return cursor.getCount();
    }


}
