package id.co.jst.siar.Helpers.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

import id.co.jst.siar.Models.sqlite.LocationModel

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

class DBHandlerLocations(context: Context) : DBHandlerSQLite(context) {

    // Getting locations Count
    // return count
    val locationCount: Int
        get() {
            val countQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_LOCATION
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }

    // Getting All Locations
    // Select All Query
    // looping through all rows and adding to list
    // Adding to list
    // return contact list
    val allLocations: List<LocationModel>
        get() {
            val locationList = ArrayList<LocationModel>()
            val selectQuery = "SELECT * FROM " + DBHandlerSQLite.TABLE_LOCATION

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val location = LocationModel()
                    location.pl_code = Integer.parseInt(cursor.getString(0))
                    location.pl_building = cursor.getString(1)
                    location.pl_floor = cursor.getString(2)
                    location.pl_place = cursor.getString(3)
                    location.pl_description = cursor.getString(4)
                    locationList.add(location)
                } while (cursor.moveToNext())
            }
            return locationList
        }

    // Adding new location
    fun addLocation(location: LocationModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DBHandlerSQLite.KEY_ID, location.pl_code)
        values.put(DBHandlerSQLite.KEY_BUILDING, location.pl_building)
        values.put(DBHandlerSQLite.KEY_FLOOR, location.pl_floor)
        values.put(DBHandlerSQLite.KEY_PLACE, location.pl_place)
        values.put(DBHandlerSQLite.KEY_DESCRIPTION, location.pl_description)
        values.put(DBHandlerSQLite.KEY_DATE, location.pl_date!!.toString())
        // Inserting Row
        db.insert(DBHandlerSQLite.TABLE_LOCATION, null, values)
        db.close() // Closing database connection
    }

    // Deleting a shop
    fun deleteLocation(location: LocationModel) {
        val db = this.writableDatabase
        db.delete(DBHandlerSQLite.TABLE_LOCATION, DBHandlerSQLite.KEY_ID + " = ?",
                arrayOf(location.pl_code.toString()))
        db.close()
    }

    // Getting location
    fun getLocation(pl_code: String): LocationModel {
        val query = "SELECT * FROM " + DBHandlerSQLite.TABLE_LOCATION + " WHERE pl_code = " + pl_code
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        cursor?.moveToFirst()

// return count
        return LocationModel(cursor!!.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5))
    }

    // Truncate Location
    fun truncateLocation() {
        val success: Boolean?
        val truncateQuery = "DELETE FROM " + DBHandlerSQLite.TABLE_LOCATION
        val db = this.readableDatabase
        db.execSQL(truncateQuery)
    }
}
