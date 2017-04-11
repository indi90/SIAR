package id.co.jst.siar.Helpers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import id.co.jst.siar.Models.sqlite.LocationModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerRAA extends DBHandlerSQLite {

    public DBHandlerRAA(Context context) {
        super(context);
    }

    // Adding new RAA
    public void addRAA(RAAModel raa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IRPERIODID, raa.getIRAssetNo());
        values.put(KEY_IRASSETNO, raa.getIRAssetNo());
        values.put(KEY_IRMODEL, raa.getIRModel());
        values.put(KEY_IRMFGNO, raa.getIRMFGNo());
        values.put(KEY_IRLOCATIONID, raa.getIRLocationID());
        values.put(KEY_IRGENERATEDATE, raa.getIRGenerateDate().toString());
        values.put(KEY_IRGENERATEUSER, raa.getIRGenerateUser());
        values.put(KEY_IRDEPTCODE, raa.getIRDeptCode());
        // Inserting Row
        db.insert(super.TABLE_RAA, null, values);
        db.close(); // Closing database connection
    }

    // Deleting a shop
    public void deleteLocation(RAAModel raa) {
        String deleteQuery = "DELETE FROM " + super.TABLE_RAA + " WHERE " + KEY_IRASSETNO + " = " + raa.getIRAssetNo() + " AND " + KEY_IRPERIODID + " = " + raa.getIRPeriodID();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

    // Getting RAA Count
    public int getRAACount() {
        String countQuery = "SELECT * FROM " + super.TABLE_RAA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Truncate RAA
    public void truncateRAA() {
        String truncateQuery = "DELETE FROM " + super.TABLE_RAA;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(truncateQuery);
        db.close();
//        Log.d("Reading: ", "Count Test ..." + rs.next());
//        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
//        return cursor.getCount();
    }

    // Getting one raa
    public Object[] getRAA(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String getRAAQuery = "SELECT " + KEY_IRASSETNO + ", " + KEY_IRMODEL + ", " + KEY_IRMFGNO + ", " + KEY_PLACE + " FROM " + TABLE_RAA +
                " INNER JOIN " + TABLE_LOCATION +
                " ON " + KEY_ID + " = " + KEY_IRLOCATIONID +
                " WHERE IRAssetNo = " + id;

        Cursor cursor = db.rawQuery(getRAAQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        Object objects[] = new Object[2];
        RAAModel raa = new RAAModel();
        LocationModel location = new LocationModel();
        raa.setIRAssetNo(cursor.getInt(0));
        raa.setIRModel(cursor.getString(1));
        raa.setIRMFGNo(cursor.getString(2));
        location.setPl_place(cursor.getString(3));
        objects[0] = raa;
        objects[1] = location;
        // return
        return objects;
    }

    // Getting one raa
    public RAAModel getRAAforRAAActual(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String getRAAQuery = "SELECT * FROM " + TABLE_RAA +
                " INNER JOIN " + TABLE_LOCATION +
                " ON " + KEY_ID + " = " + KEY_IRLOCATIONID +
                " WHERE IRAssetNo = " + id;

        Cursor cursor = db.rawQuery(getRAAQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        RAAModel raa = new RAAModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));

        // return
        return raa;
    }
}
