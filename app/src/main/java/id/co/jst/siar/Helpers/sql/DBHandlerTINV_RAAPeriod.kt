package id.co.jst.siar.Helpers.sql

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

import id.co.jst.siar.MainActivity
import id.co.jst.siar.Models.sql.TINV_RAAPeriodModel

import android.R.attr.data

/**
 * Created by endro.ngujiharto on 9/13/2017.
 */

class DBHandlerTINV_RAAPeriod {

    private var connect: Connection? = null
    private var rs: ResultSet? = null
    private val sqlConnect = DBHandlerConnection()

    // Getting period Count
    fun getPeriodCount(activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        //        Log.d(Connection.(connect), "Connect : ");
        val countQuery = "SELECT COUNT(*) FROM $TABLE WHERE IRPStatus = 1"
        if (connect == null) {
            z = "Check Your Connection!"
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show()
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(countQuery)
                // return count
                rs!!.next()
                count = rs!!.getInt(1)
                connect!!.close()
            } catch (e: SQLException) {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }
        return count
    }

    // Getting Active Period
    fun getActivePeriod(activity: Context): TINV_RAAPeriodModel {
        var period = TINV_RAAPeriodModel()
        connect = sqlConnect.connect(DATABASE_NAME)
        // Select All Query
        val selectQuery = "SELECT IRPID,IRPPeriod,IRPYear,IRPMonth,IRPStatus,IRPGenerateDate,IRPInventoryOpen,IRPInventoryClose FROM $TABLE WHERE IRPStatus = 1"
        var z = ""
        if (connect == null) {
            z = "Check Your Connection!"
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show()
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(selectQuery)
                rs!!.next()
                period = TINV_RAAPeriodModel(rs!!.getInt(1), rs!!.getInt(2), rs!!.getInt(3), rs!!.getInt(4), rs!!.getInt(5), rs!!.getString(6), rs!!.getString(7), rs!!.getString(8))
                connect!!.close()
            } catch (e: SQLException) {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }
        // return contact list
        return period
    }

    companion object {
        // Database Name
        private val DATABASE_NAME = "JSTACCFINDB"
        // Contacts table name
        private val TABLE = "TINV_RAAPeriod"
    }
}
