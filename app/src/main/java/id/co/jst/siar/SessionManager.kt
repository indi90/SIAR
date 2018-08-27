package id.co.jst.siar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log

import java.util.HashMap

import id.co.jst.siar.Helpers.sql.DBHandlerTBMST_Employee
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAA
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAAActual
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod
import id.co.jst.siar.Models.sqlite.LocationModel

import android.content.ContentValues.TAG
import android.provider.Contacts.SettingsColumns.KEY

/**
 * Created by endro.ngujiharto on 4/18/2017.
 */

class SessionManager {
    // Shared Preferences
    internal var pref: SharedPreferences

    // Editor for Shared preferences
    internal var editor: Editor

    // Context
    internal var _context: Context

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    private val sqlEmployee = DBHandlerTBMST_Employee()

    val isActived: Boolean
        get() = pref.getBoolean(IS_ACTIVE, false)

    // Name
    // Empl Code
    // Location
    // Location
    // return user
    val userDetails: HashMap<String, String>
        get() {
            val user = HashMap<String, String>()
            user[KEY_NAME] = pref.getString(KEY_NAME, null)
            user[KEY_EMPLCODE] = pref.getString(KEY_EMPLCODE, null)
            user[KEY_LOCATION] = pref.getString(KEY_LOCATION, null)

            user[KEY_IDLOCATION] = pref.getString(KEY_IDLOCATION, null)

            user[KEY_DEPARTMENT] = pref.getString(KEY_DEPARTMENT, null)

            user[KEY_SECTION] = pref.getString(KEY_SECTION, null)

            user[KEY_DIVISION] = pref.getString(KEY_DIVISION, null)
            user[KEY_REMAINS] = pref.getString(KEY_REMAINS, null)

            user[KEY_ALL] = pref.getString(KEY_ALL, null)
            return user
        }

    constructor() {}
    // Constructor
    constructor(context: Context) {
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    /**
     * Create login session
     */
    fun createPICSession(emplcode: String): String? {
        val employee: Array<String>

        employee = sqlEmployee.getEmployee(emplcode)

        Log.d("trial", employee[7])

        if (employee.size < 2) {
            return employee[0]
        } else {
            // Storing login value as TRUE
            editor.putBoolean(IS_ACTIVE, true)

            // Storing emplcode in pref
            editor.putString(KEY_EMPLCODE, employee[0])

            // Storing emplname in pref
            editor.putString(KEY_NAME, employee[1])
            editor.putString(KEY_SECTIONCODE, employee[2])
            editor.putString(KEY_SECTIONNAME, employee[3])
            editor.putString(KEY_DEPARTMETNAME, employee[4])
            editor.putString(KEY_DEPARTMENT, employee[5])
            editor.putString(KEY_SECTION, employee[6])
            editor.putString(KEY_DIVISION, employee[7])

            // commit changes
            editor.commit()

            return null
        }
    }

    fun createLocationSession(locationID: String) {
        val sqliteLocation = DBHandlerLocations(_context)
        val location: LocationModel
        val Loc: String

        location = sqliteLocation.getLocation(locationID)

        Loc = location.pl_floor + " - Phase " + location.pl_building + " - " + location.pl_place
        // Storing location in pref
        editor.putString(KEY_LOCATION, Loc)

        editor.putString(KEY_IDLOCATION, locationID)

        // commit changes
        editor.commit()
    }

    fun createBalanceSession(dept: String, div: String, period: Int) {
        val sqlRAA = DBHandlerTINV_RAA()
        val sqlRAAActual = DBHandlerTINV_RAAActual()
        val RAABalance: Int
        val RAAActualBalance: Int
        var remains = 0
        //        sqlitePeriod.checkPeriod();
        //        Log.d(Integer.toString(sqlitePeriod.checkPeriod()), "createBalanceSession: ");

        RAABalance = sqlRAA.getBalance(dept, div, period, _context)
        RAAActualBalance = sqlRAAActual.getBalance(dept, div, period, _context)

        remains = RAABalance - RAAActualBalance

        // Storing location in pref
        editor.putString(KEY_REMAINS, Integer.toString(remains))

        editor.putString(KEY_ALL, Integer.toString(RAABalance))

        // commit changes
        editor.commit()
    }

    fun updateBalanceSession(remains: Int) {
        // Storing location in pref
        editor.putString(KEY_REMAINS, Integer.toString(remains))

        // commit changes
        editor.commit()
    }

    fun checkPIC() {
        // Check login status
        if (!this.isActived) {
            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, MainActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context.startActivity(i)
        }

    }

    fun clearAllData() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

        //        // After logout redirect user to Loing Activity
        //        Intent i = new Intent(_context, LoginActivity.class);
        //        // Closing all the Activities
        //        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //
        //        // Add new Flag to start new Activity
        //        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        //        // Staring Login Activity
        //        _context.startActivity(i);
    }

    companion object {

        // Sharedpref file name
        private val PREF_NAME = "AndroidHivePref"

        // All Shared Preferences Keys
        private val IS_ACTIVE = "IsActived"

        // Name (make variable public to access from outside)
        val KEY_NAME = "name"

        // Location (make variable public to access from outside)
        val KEY_LOCATION = "location"
        val KEY_IDLOCATION = "IDlocation"

        val KEY_REMAINS = "remains"
        val KEY_ALL = "all"

        // Location (make variable public to access from outside)
        val KEY_EMPLCODE = "emplcode"
        val KEY_SECTIONCODE = "sectioncode"
        val KEY_SECTIONNAME = "sectionname"
        val KEY_DEPARTMETNAME = "departmentname"
        val KEY_DEPARTMENT = "department"
        val KEY_SECTION = "section"
        val KEY_DIVISION = "division"
    }


}
