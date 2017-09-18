package id.co.jst.siar.Helpers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.Models.sql.TINV_RAAModel;
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
        values.put(KEY_IRPERIODID, raaActual.getIRPeriodID());
        values.put(KEY_IRASSETNO, raaActual.getIRAssetNo());
        values.put(KEY_IRMODEL, raaActual.getIRModel());
        values.put(KEY_IRMFGNO, raaActual.getIRMFGNo());
        values.put(KEY_IRLOCATIONID, raaActual.getIRLocationID());
        values.put(KEY_IRGENERATEDATE, raaActual.getIRGenerateDate());
        values.put(KEY_IRGENERATEUSER, raaActual.getIRGenerateUser());
        values.put(KEY_IRDEPTCODE, raaActual.getIRDeptCode());
        // Inserting Row
        db.insert(TABLE_RAAACTUAL, null, values);
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
    }

    // Getting All RAAActual
    public List<RAAActualModel> getAllRAAActual() {
        List<RAAActualModel> RAAActualList = new ArrayList<RAAActualModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RAAACTUAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RAAActualModel RAAActual = new RAAActualModel();
                RAAActual.setIRPeriodID(cursor.getInt(0));
                RAAActual.setIRAssetNo(cursor.getInt(1));
                RAAActual.setIRModel(cursor.getString(2));
                RAAActual.setIRMFGNo(cursor.getString(3));
                RAAActual.setIRLocationID(cursor.getInt(4));
                RAAActual.setIRGenerateDate(cursor.getString(5));
                RAAActual.setIRGenerateUser(cursor.getString(6));
                RAAActual.setIRDeptCode(cursor.getInt(7));
                // Adding RAAActual to list
                RAAActualList.add(RAAActual);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return RAAActual list
        return RAAActualList;
    }


}
