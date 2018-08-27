package id.co.jst.siar.Helpers.sql

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast

import java.lang.reflect.Array
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

import id.co.jst.siar.Models.sql.TINV_RAAActualModel
import id.co.jst.siar.Models.sql.TINV_RAAModel
import id.co.jst.siar.Models.sqlite.RAAModel

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class DBHandlerTINV_RAAActual {

    private var connect: Connection? = null
    private var rs: ResultSet? = null
    private val mDialog: ProgressDialog? = null
    private val sqlConnect = DBHandlerConnection()
    private var message: String? = null

    // Adding new RAA
    fun addRAAActual(raaActual: TINV_RAAActualModel, activity: Context): Array<String> {
        var kembali: Array<String>
        val tmp: Array<String>
        connect = sqlConnect.connect(DATABASE_NAME)
        val model = raaActual.irModel
        val generateUser = raaActual.irGenerateUser
        val pic = raaActual.irGenerateUser
        val insertQuery = "INSERT INTO " + TABLE + " (IRPeriodID, IRAssetNo, IRModel, IRMFGNo, IRLocationID, IRGenerateDate, IRGenerateUser, IRDeptCode, IRPIC, IRScanDate) VALUES ('" +
                raaActual.irPeriodID +
                "','" + raaActual.irAssetNo +
                "','" + model!!.replace("'", "''") +
                "','" + raaActual.irmfgNo +
                "','" + raaActual.irLocationID +
                "','" + raaActual.irGenerateDate +
                "','" + generateUser!!.replace("'", "''") +
                "','" + raaActual.irDeptCode +
                "','" + pic!!.replace("'", "''") +
                "','" + raaActual.irScanDate + "')"
        println("query $insertQuery")
        if (connect == null) {
            message = "Check Your Connection!"
            kembali = arrayOf<String>(message)
        } else {
            try {
                val statement = connect!!.prepareStatement(insertQuery)
                statement.executeUpdate()

                connect!!.close()
                kembali = arrayOf("")
            } catch (e: SQLException) {
                if (e.errorCode == 2627) {
                    tmp = this.updateRAAActual(raaActual, activity)
                    message = tmp[0]
                } else {
                    message = e.message
                }
                kembali = arrayOf<String>(message)
            }

        }
        return kembali
    }

    // Update RAA
    fun updateRAAActual(raaActual: TINV_RAAActualModel, activity: Context): Array<String> {
        var kembali: Array<String>
        connect = sqlConnect.connect(DATABASE_NAME)
        val insertQuery = "UPDATE " + TABLE + " SET IRLocationID = " +
                raaActual.irLocationID +
                " WHERE IRAssetNo = " +
                raaActual.irAssetNo
        if (connect == null) {
            message = "Check Your Connection!"
            kembali = arrayOf<String>(message)
        } else {
            try {
                val statement = connect!!.prepareStatement(insertQuery)
                statement.executeUpdate()

                connect!!.close()
                kembali = arrayOf("")
            } catch (e: SQLException) {
                message = e.message
                kembali = arrayOf<String>(message)
            }

        }
        return kembali
    }

    fun getBalance(dept: String, sec: String, period: Int, activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        val countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND (SELECT max(sec_sectioncode) FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND IRPeriodID = " + period

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
    fun getRAAActualCount(dept: String, sec: String, period: Int, activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        val countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND (SELECT max(sec_sectioncode) FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND IRPeriodID = " + period
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

    // Getting All RAAActual
    fun getAllRAAActual(dept: String, sec: String, period: Int, activity: Context): List<TINV_RAAActualModel> {
        val RAAActualList = ArrayList<TINV_RAAActualModel>()
        connect = sqlConnect.connect(DATABASE_NAME)
        // Select All Query
        val selectQuery = "SELECT * FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND (SELECT max(sec_sectioncode) FROM jincommon..tbmst_employee INNER JOIN jincommon..tbmst_section" +
                "  ON em_sectioncode = sec_sectioncode and sec_status = 1" +
                "  WHERE sec_department = '" + dept + "')" +
                "  AND IRPeriodID = " + period
        var z = ""
        if (connect == null) {
            z = "Check Your Connection!"
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show()
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(selectQuery)
                while (rs!!.next()) {
                    val RAAActual = TINV_RAAActualModel()
                    RAAActual.irPeriodID = Integer.parseInt(rs!!.getString(1))
                    RAAActual.irAssetNo = Integer.parseInt(rs!!.getString(2))
                    RAAActual.irModel = rs!!.getString(3)
                    RAAActual.irmfgNo = rs!!.getString(4)
                    RAAActual.irLocationID = Integer.parseInt(rs!!.getString(5))
                    RAAActual.irGenerateDate = rs!!.getString(6)
                    RAAActual.irGenerateUser = rs!!.getString(7)
                    RAAActual.irDeptCode = Integer.parseInt(rs!!.getString(8))
                    // Adding to list
                    RAAActualList.add(RAAActual)
                }
            } catch (e: SQLException) {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }

        // return contact list
        return RAAActualList
    }

    companion object {
        private val DATABASE_NAME = "JSTACCFINDB"
        // Contacts table name
        private val TABLE = "TINV_RAAActual"
    }
}
