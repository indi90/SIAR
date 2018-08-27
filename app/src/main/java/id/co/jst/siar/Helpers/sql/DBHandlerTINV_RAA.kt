package id.co.jst.siar.Helpers.sql

import android.content.Context
import android.util.Log
import android.widget.Toast

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList
import java.util.HashMap

import id.co.jst.siar.Models.sql.TINV_RAAModel
import id.co.jst.siar.SessionManager

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class DBHandlerTINV_RAA {
    private var connect: Connection? = null
    private var rs: ResultSet? = null
    private val sqlConnect = DBHandlerConnection()
    private var cond = ""

    // Getting Balance
    fun getBalance(dept: String, div: String, period: Int, activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        if (div != "Logistic Control Division" && div != "Administration Division") {
            cond = "  AND sec_department = '$dept'"
        }
        val countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                " ) AND IRPeriodID = " + period

        //        Log.d("Trial :", countQuery);
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

    // Getting locations Count
    fun getRAACount(dept: String, div: String, period: Int, activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        if (div != "Logistic Control Division" && div != "Administration Division") {
            cond = "  AND sec_department = '$dept'"
        }
        val countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                "  ) AND IRPeriodID = " + period
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

    // Getting All Locations
    fun getAllRAA(dept: String, div: String, period: Int, activity: Context): List<TINV_RAAModel> {
        val RAAList = ArrayList<TINV_RAAModel>()
        connect = sqlConnect.connect(DATABASE_NAME)

        if (div != "Logistic Control Division" && div != "Administration Division") {
            cond = "  AND sec_department = '$dept'"
        }
        // Select All Query
        val selectQuery = "SELECT * FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' " +
                cond +
                "  ) AND IRPeriodID = " + period

        Log.d("trial", selectQuery)
        var z = ""
        if (connect == null) {
            z = "Check Your Connection!"
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show()
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(selectQuery)
                while (rs!!.next()) {
                    val RAA = TINV_RAAModel()
                    RAA.irPeriodID = Integer.parseInt(rs!!.getString(1))
                    RAA.irAssetNo = Integer.parseInt(rs!!.getString(2))
                    RAA.irModel = rs!!.getString(3)
                    RAA.irmfgNo = rs!!.getString(4)
                    RAA.irLocationID = Integer.parseInt(rs!!.getString(5))
                    RAA.irGenerateDate = rs!!.getString(6)
                    RAA.irGenerateUser = rs!!.getString(7)
                    RAA.irDeptCode = Integer.parseInt(rs!!.getString(8))
                    // Adding to list
                    RAAList.add(RAA)
                }
            } catch (e: SQLException) {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }

        // return contact list
        return RAAList
    }

    companion object {
        private val DATABASE_NAME = "JSTACCFINDB"
        // Contacts table name
        private val TABLE = "TINV_RAA"
    }
}
