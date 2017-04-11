package id.co.jst.siar.Helpers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.co.jst.siar.Models.sqlite.RAAActualModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerRAAActual extends DBHandlerSQLite {

    public DBHandlerRAAActual(Context context) {
        super(context);
    }

    // Adding new RAAActual
    public void addRAAActual(RAAActualModel raaActual) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IRPERIODID, raaActual.getIRAssetNo());
        values.put(KEY_IRASSETNO, raaActual.getIRAssetNo());
        values.put(KEY_IRMODEL, raaActual.getIRModel());
        values.put(KEY_IRMFGNO, raaActual.getIRMFGNo());
        values.put(KEY_IRLOCATIONID, raaActual.getIRLocationID());
        values.put(KEY_IRGENERATEDATE, raaActual.getIRGenerateDate().toString());
        values.put(KEY_IRGENERATEUSER, raaActual.getIRGenerateUser());
        values.put(KEY_IRDEPTCODE, raaActual.getIRDeptCode());
        // Inserting Row
        db.insert(super.TABLE_RAA, null, values);
        db.close(); // Closing database connection
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
