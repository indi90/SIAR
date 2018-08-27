package id.co.jst.siar.Helpers.sql

import android.content.Context
import android.util.Log
import android.widget.Toast

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import android.R.attr.data

/**
 * Created by endro.ngujiharto on 4/18/2017.
 */

class DBHandlerTBMST_Employee {

    private var connect: Connection? = null
    private var rs: ResultSet? = null
    private val sqlConnect = DBHandlerConnection()
    private var success = false
    private val message = arrayOfNulls<String>(1)

    // Getting employee
    fun getEmployee(EmplCode: String): Array<String> {
        val data = arrayOfNulls<String>(8)

        connect = sqlConnect.connect(DATABASE_NAME)
        val Query = "SELECT em_emplCode, em_emplname, em_sectioncode, sec_sectionnaming, sec_departmentnaming, sec_department, sec_section, SEC_DIVISION FROM $TABLE inner JOIN TBMST_SECTION ON em_sectioncode = sec_sectioncode WHERE EM_EmplCode = $EmplCode and sec_status = 1"
        if (connect == null) {
            message[0] = "Check Your Connection!"
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(Query)
                rs!!.next()
                data[0] = rs!!.getString(1)
                data[1] = rs!!.getString(2)
                data[2] = rs!!.getString(3)
                data[3] = rs!!.getString(4)
                data[4] = rs!!.getString(5)
                data[5] = rs!!.getString(6)
                data[6] = rs!!.getString(7)
                data[7] = rs!!.getString(8)
                connect!!.close()
                success = true
            } catch (e: SQLException) {
                message[0] = e.message
            }

        }

        return if (success) {
            data
        } else {
            message
        }
    }

    fun checkBarcode(barcode: String): Array<String> {
        val data = arrayOfNulls<String>(2)
        connect = sqlConnect.connect(DATABASE_NAME)
        val Query = "SELECT AMU_MINVRAA, amu_employeeno FROM TBAM_User WHERE AMU_QRCode = '$barcode'"

        if (connect == null) {
            message[0] = "Check Your Connection!"
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(Query)
                rs!!.next()
                data[0] = rs!!.getString(1)
                data[1] = rs!!.getString(2)
                connect!!.close()
                success = true
            } catch (e: SQLException) {
                message[0] = e.message
            }

        }

        return if (success) {
            data
        } else {
            message
        }
    }

    companion object {

        private val DATABASE_NAME = "JINCOMMON"
        // Contacts table name
        private val TABLE = "TBMST_employee"
    }

}
