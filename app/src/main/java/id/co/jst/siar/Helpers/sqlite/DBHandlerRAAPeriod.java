package id.co.jst.siar.Helpers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import id.co.jst.siar.Models.sqlite.RAAPeriodModel;

/**
 * Created by endro.ngujiharto on 9/14/2017.
 */

public class DBHandlerRAAPeriod extends DBHandlerSQLite{
    public DBHandlerRAAPeriod(Context context) {
        super(context);
    }

    // Adding new period
    public void addPeriod(RAAPeriodModel period) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IRPID, period.getIRPID());
        values.put(KEY_IRPPERIOD, period.getIRPPeriod());
        values.put(KEY_IRPYEAR, period.getIRPYear());
        values.put(KEY_IRPMONTH, period.getIRPMonth());
        values.put(KEY_IRPSTATUS, period.getIRPStatus());
        values.put(KEY_IRPGENERATEDATE, period.getIRPGenerateDate());
        values.put(KEY_IRPINVENTORYOPEN, period.getIRPInventoryOpen());
        values.put(KEY_IRPINVENTORYCLOSE, period.getIRPInventoryClose());
        // Inserting Row
        db.insert(TABLE_RAAPERIOD, null, values);
//        Log.d("Trial : ","")
        db.close(); // Closing database connection
    }

    // Deleting a period
    public void deletePeriod(RAAPeriodModel period) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RAAPERIOD, KEY_IRPID + " = ?",
                new String[] { String.valueOf(period.getIRPID()) });
        db.close();
    }

    // Getting period Count
    public int getLocationCount() {
        String countQuery = "SELECT * FROM " + TABLE_RAAPERIOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting period
    public RAAPeriodModel getPeriod(int irp_id) {
        String query = "SELECT * FROM " + TABLE_RAAPERIOD + " WHERE IRPID = " + irp_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();

        RAAPeriodModel period = new RAAPeriodModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        // return count
        return period;
    }

    // Getting period
    public int checkPeriod() {
        String query = "SELECT * FROM " + TABLE_RAAPERIOD + " WHERE IRPStatus = 1";
//        Log.d("Trial :",getReadableDatabase().toString());
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();

//        RAAPeriodModel period = new RAAPeriodModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        // return count
        return cursor.getInt(0);
    }

    // Truncate Period
    public void truncatePeriod() {
        String truncateQuery = "DELETE FROM " + TABLE_RAAPERIOD;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(truncateQuery);
    }
}
