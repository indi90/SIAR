package id.co.jst.siar.Helpers.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import id.co.jst.siar.Models.sqlite.RAAPeriodModel

/**
 * Created by endro.ngujiharto on 9/14/2017.
 */

class DBHandlerRAAPeriod(context: Context) : DBHandlerSQLite(context) {

    // Getting period Count
    // return count
    val locationCount: Int
        get() {
            val countQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAAPERIOD
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }

    // Adding new period
    fun addPeriod(period: RAAPeriodModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DBHandlerSQLite.KEY_IRPID, period.irpid)
        values.put(DBHandlerSQLite.KEY_IRPPERIOD, period.irpPeriod)
        values.put(DBHandlerSQLite.KEY_IRPYEAR, period.irpYear)
        values.put(DBHandlerSQLite.KEY_IRPMONTH, period.irpMonth)
        values.put(DBHandlerSQLite.KEY_IRPSTATUS, period.irpStatus)
        values.put(DBHandlerSQLite.KEY_IRPGENERATEDATE, period.irpGenerateDate)
        values.put(DBHandlerSQLite.KEY_IRPINVENTORYOPEN, period.irpInventoryOpen)
        values.put(DBHandlerSQLite.KEY_IRPINVENTORYCLOSE, period.irpInventoryClose)
        // Inserting Row
        db.insert(DBHandlerSQLite.TABLE_RAAPERIOD, null, values)
        //        Log.d("Trial : ","")
        db.close() // Closing database connection
    }

    // Deleting a period
    fun deletePeriod(period: RAAPeriodModel) {
        val db = this.writableDatabase
        db.delete(DBHandlerSQLite.TABLE_RAAPERIOD, DBHandlerSQLite.KEY_IRPID + " = ?",
                arrayOf(period.irpid.toString()))
        db.close()
    }

    // Getting period
    fun getPeriod(irp_id: Int): RAAPeriodModel {
        val query = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAAPERIOD + " WHERE IRPID = " + irp_id
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        cursor?.moveToFirst()

// return count
        return RAAPeriodModel(cursor!!.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7))
    }

    // Getting period
    fun checkPeriod(): Int {
        val query = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAAPERIOD + " WHERE IRPStatus = 1"
        //        Log.d("Trial :",getReadableDatabase().toString());
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)

        cursor?.moveToFirst()

        //        RAAPeriodModel period = new RAAPeriodModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        // return count
        return cursor!!.getInt(0)
    }

    // Truncate Period
    fun truncatePeriod() {
        val truncateQuery = "DELETE FROM " + DBHandlerSQLite.TABLE_RAAPERIOD
        val db = this.readableDatabase
        db.execSQL(truncateQuery)
    }
}
