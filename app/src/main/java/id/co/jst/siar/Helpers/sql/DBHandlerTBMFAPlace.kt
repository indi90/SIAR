package id.co.jst.siar.Helpers.sql

import android.app.ProgressDialog
import android.content.Context
import android.os.StrictMode
import android.util.Log
import android.widget.Toast

import java.sql.DriverManager

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

import id.co.jst.siar.Models.sql.TBMFAPlaceModel

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

class DBHandlerTBMFAPlace {

    private var connect: Connection? = null
    private var rs: ResultSet? = null
    private val mDialog: ProgressDialog? = null
    private val sqlConnect = DBHandlerConnection()

    // Getting locations Count
    fun getPlacesCount(activity: Context): Int {
        var count = 0
        var z = ""

        connect = sqlConnect.connect(DATABASE_NAME)
        val countQuery = "SELECT COUNT(*) FROM $TABLE"
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
    fun getAllPlaces(activity: Context): List<TBMFAPlaceModel> {
        val placeList = ArrayList<TBMFAPlaceModel>()
        connect = sqlConnect.connect(DATABASE_NAME)
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE"
        var z = ""
        if (connect == null) {
            z = "Check Your Connection!"
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show()
        } else {
            try {
                val statement = connect!!.createStatement()
                rs = statement.executeQuery(selectQuery)
                while (rs!!.next()) {
                    val place = TBMFAPlaceModel()
                    place.pl_code = Integer.parseInt(rs!!.getString(1))
                    place.pl_building = rs!!.getString(2)
                    place.pl_floor = rs!!.getString(3)
                    place.pl_place = rs!!.getString(4)
                    place.pl_description = rs!!.getString(5)
                    // Adding to list
                    placeList.add(place)
                }
                connect!!.close()
            } catch (e: SQLException) {
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }

        // return contact list
        return placeList
    }

    companion object {
        // Database Name
        private val DATABASE_NAME = "JSTACCFINDB"
        // Contacts table name
        private val TABLE = "TBMFA_FAPlace"
    }

}
