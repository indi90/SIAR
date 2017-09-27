package id.co.jst.siar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

import id.co.jst.siar.Helpers.sql.DBHandlerTBMST_Employee;
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAA;
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAAActual;
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod;
import id.co.jst.siar.Models.sqlite.LocationModel;

import static android.content.ContentValues.TAG;
import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by endro.ngujiharto on 4/18/2017.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_ACTIVE = "IsActived";

    // Name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Location (make variable public to access from outside)
    public static final String KEY_LOCATION = "location";
    public static final String KEY_IDLOCATION = "IDlocation";

    public static final String KEY_REMAINS = "remains";
    public static final String KEY_ALL = "all";

    // Location (make variable public to access from outside)
    public static final String KEY_EMPLCODE = "emplcode";
    public static final String KEY_SECTIONCODE = "sectioncode";
    public static final String KEY_SECTIONNAME = "sectionname";
    public static final String KEY_DEPARTMETNAME = "departmentname";
    public static final String KEY_DEPARTMENT = "department";
    public static final String KEY_SECTION = "section";

    private DBHandlerTBMST_Employee sqlEmployee = new DBHandlerTBMST_Employee();

    public SessionManager(){
    }
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public String createPICSession(String emplcode){
        String[] employee;

        employee = sqlEmployee.getEmployee(emplcode);

        if (employee.length < 2 ){
            return employee[0];
        } else {
            // Storing login value as TRUE
            editor.putBoolean(IS_ACTIVE, true);

            // Storing emplcode in pref
            editor.putString(KEY_EMPLCODE, employee[0]);

            // Storing emplname in pref
            editor.putString(KEY_NAME, employee[1]);
            editor.putString(KEY_SECTIONCODE, employee[2]);
            editor.putString(KEY_SECTIONNAME, employee[3]);
            editor.putString(KEY_DEPARTMETNAME, employee[4]);
            editor.putString(KEY_DEPARTMENT, employee[5]);
            editor.putString(KEY_SECTION, employee[6]);

            // commit changes
            editor.commit();

            return null;
        }
    }

    public void createLocationSession(String locationID){
        DBHandlerLocations sqliteLocation = new DBHandlerLocations(_context);
        LocationModel location;

        location = sqliteLocation.getLocation(locationID);

        // Storing location in pref
        editor.putString(KEY_LOCATION, location.getPl_place());

        editor.putString(KEY_IDLOCATION, locationID);

        // commit changes
        editor.commit();
    }

    public void createBalanceSession(String dept, String sec, int period){
        DBHandlerTINV_RAA sqlRAA = new DBHandlerTINV_RAA();
        DBHandlerTINV_RAAActual sqlRAAActual = new DBHandlerTINV_RAAActual();
        int RAABalance, RAAActualBalance, remains = 0;
//        sqlitePeriod.checkPeriod();
//        Log.d(Integer.toString(sqlitePeriod.checkPeriod()), "createBalanceSession: ");

        RAABalance = sqlRAA.getBalance(dept, sec, period, _context);
        RAAActualBalance = sqlRAAActual.getBalance(dept, sec, period, _context);

        remains = RAABalance - RAAActualBalance;

        // Storing location in pref
        editor.putString(KEY_REMAINS, Integer.toString(remains));

        editor.putString(KEY_ALL, Integer.toString(RAABalance));

        // commit changes
        editor.commit();
    }

    public void updateBalanceSession(int balance, int remains){
        // Storing location in pref
        editor.putString(KEY_REMAINS, Integer.toString(remains));

        editor.putString(KEY_ALL, Integer.toString(balance));

        // commit changes
        editor.commit();
    }

    public void checkPIC(){
        // Check login status
        if(!this.isActived()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public boolean isActived(){
        return pref.getBoolean(IS_ACTIVE, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // Name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // Empl Code
        user.put(KEY_EMPLCODE, pref.getString(KEY_EMPLCODE, null));

        // Location
        user.put(KEY_LOCATION, pref.getString(KEY_LOCATION, null));

        user.put(KEY_IDLOCATION, pref.getString(KEY_IDLOCATION, null));

        user.put(KEY_DEPARTMENT, pref.getString(KEY_DEPARTMENT, null));

        user.put(KEY_SECTION, pref.getString(KEY_SECTION, null));

        // Location
        user.put(KEY_REMAINS, pref.getString(KEY_REMAINS, null));

        user.put(KEY_ALL, pref.getString(KEY_ALL, null));

        // return user
        return user;
    }

    public void clearAllData(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

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


}
