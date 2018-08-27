package id.co.jst.siar.Helpers.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

import id.co.jst.siar.Models.sql.TINV_RAAModel
import id.co.jst.siar.Models.sqlite.RAAActualModel
import id.co.jst.siar.Models.sqlite.RAAModel

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class DBHandlerRAAActual(context: Context) : DBHandlerSQLite(context) {

    // Getting RAA Count
    // return count
    val raaCount: Int
        get() {
            val countQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAAACTUAL
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }

    // Getting All RAAActual
    // Select All Query
    // Adding RAAActual to list
    // return RAAActual list
    val allRAAActual: List<RAAActualModel>
        get() {
            val RAAActualList = ArrayList<RAAActualModel>()
            val selectQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_RAAACTUAL

            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val RAAActual = RAAActualModel()
                    RAAActual.irPeriodID = cursor.getInt(0)
                    RAAActual.irAssetNo = cursor.getInt(1)
                    RAAActual.irModel = cursor.getString(2)
                    RAAActual.irmfgNo = cursor.getString(3)
                    RAAActual.irLocationID = cursor.getInt(4)
                    RAAActual.irGenerateDate = cursor.getString(5)
                    RAAActual.irGenerateUser = cursor.getString(6)
                    RAAActual.irDeptCode = cursor.getInt(7)
                    RAAActual.irpic = cursor.getString(8)
                    RAAActual.irScanDate = cursor.getString(9)
                    RAAActualList.add(RAAActual)
                } while (cursor.moveToNext())
            }

            cursor.close()
            return RAAActualList
        }

    // Adding new RAAActual
    fun addRAAActual(raaActual: RAAActualModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DBHandlerSQLite.KEY_IRPERIODID, raaActual.irPeriodID)
        values.put(DBHandlerSQLite.KEY_IRASSETNO, raaActual.irAssetNo)
        values.put(DBHandlerSQLite.KEY_IRMODEL, raaActual.irModel)
        values.put(DBHandlerSQLite.KEY_IRMFGNO, raaActual.irmfgNo)
        values.put(DBHandlerSQLite.KEY_IRLOCATIONID, raaActual.irLocationID)
        values.put(DBHandlerSQLite.KEY_IRGENERATEDATE, raaActual.irGenerateDate)
        values.put(DBHandlerSQLite.KEY_IRGENERATEUSER, raaActual.irGenerateUser)
        values.put(DBHandlerSQLite.KEY_IRDEPTCODE, raaActual.irDeptCode)
        values.put(DBHandlerSQLite.KEY_IRPIC, raaActual.irpic)
        values.put(DBHandlerSQLite.KEY_IRSCANDATE, raaActual.irScanDate)
        // Inserting Row
        db.insert(DBHandlerSQLite.TABLE_RAAACTUAL, null, values)
        db.close() // Closing database connection
    }

    // Truncate RAA
    fun truncateRAA() {
        val truncateQuery = "DELETE FROM " + DBHandlerSQLite.TABLE_RAAACTUAL
        val db = this.readableDatabase
        db.execSQL(truncateQuery)
    }


}
