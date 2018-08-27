package id.co.jst.siar.Helpers.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast

import id.co.jst.siar.Models.sqlite.LocationModel
import id.co.jst.siar.Models.sqlite.RAAModel

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class DBHandlerRAA(context: Context) : DBHandlerSQLite(context) {
    private var success = false
    private var message = ""

    // Getting RAA Count
    // return count
    val raaCount: Int
        get() {
            val countQuery = "SELECT * FROM " + super.TABLE_RAA
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }

    // Adding new RAA
    fun addRAA(raa: RAAModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DBHandlerSQLite.KEY_IRPERIODID, raa.irPeriodID)
        values.put(DBHandlerSQLite.KEY_IRASSETNO, raa.irAssetNo)
        values.put(DBHandlerSQLite.KEY_IRMODEL, raa.irModel)
        values.put(DBHandlerSQLite.KEY_IRMFGNO, raa.irmfgNo)
        values.put(DBHandlerSQLite.KEY_IRLOCATIONID, raa.irLocationID)
        values.put(DBHandlerSQLite.KEY_IRGENERATEDATE, raa.irGenerateDate)
        values.put(DBHandlerSQLite.KEY_IRGENERATEUSER, raa.irGenerateUser)
        values.put(DBHandlerSQLite.KEY_IRDEPTCODE, raa.irDeptCode)
        // Inserting Row
        db.insert(super.TABLE_RAA, null, values)
        db.close() // Closing database connection
    }

    // Truncate RAA
    fun truncateRAA() {
        val truncateQuery = "DELETE FROM " + super.TABLE_RAA
        val db = this.readableDatabase
        db.execSQL(truncateQuery)
        db.close()
    }

    // Getting one raa
    fun getRAA(id: Int): Array<Any> {
        val objects: Array<Any>
        val db = this.readableDatabase
        val getRAAQuery = "SELECT " + DBHandlerSQLite.KEY_IRASSETNO + ", " + DBHandlerSQLite.KEY_IRMODEL + ", " + DBHandlerSQLite.KEY_IRMFGNO + ", " + DBHandlerSQLite.KEY_PLACE + ", " + DBHandlerSQLite.KEY_ID + " FROM " + DBHandlerSQLite.TABLE_RAA +
                " INNER JOIN " + DBHandlerSQLite.TABLE_LOCATION +
                " ON " + DBHandlerSQLite.KEY_ID + " = " + DBHandlerSQLite.KEY_IRLOCATIONID +
                " WHERE IRAssetNo = " + id

        val cursor = db.rawQuery(getRAAQuery, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val raa = RAAModel()
            val location = LocationModel()
            raa.irAssetNo = cursor.getInt(0)
            raa.irModel = cursor.getString(1)
            raa.irmfgNo = cursor.getString(2)
            location.pl_place = cursor.getString(3)
            objects = arrayOf(raa, location)
            success = true
        } else {
            message = "Data RAA Tidak Ditemukan."
            objects = arrayOf(message)
        }

        // return
        return objects
    }

    // Getting one raa
    fun getRAAforRAAActual(id: Int): RAAModel {
        val db = this.readableDatabase
        val getRAAQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAA +
                " INNER JOIN " + DBHandlerSQLite.TABLE_LOCATION +
                " ON " + DBHandlerSQLite.KEY_ID + " = " + DBHandlerSQLite.KEY_IRLOCATIONID +
                " WHERE IRAssetNo = " + id

        val cursor = db.rawQuery(getRAAQuery, null)
        cursor?.moveToFirst()

// return
        return RAAModel(cursor!!.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7))
    }
}
