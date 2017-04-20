package id.co.jst.siar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

import id.co.jst.siar.Helpers.sql.DBHandlerTBMST_Employee;
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations;
import id.co.jst.siar.Models.sqlite.LocationModel;

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

    // Location (make variable public to access from outside)
    public static final String KEY_EMPLCODE = "emplcode";

    private DBHandlerTBMST_Employee sqlEmployee = new DBHandlerTBMST_Employee();

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public String createPICSession(String emplcode, Context context){
        String[] employee;

        employee = sqlEmployee.getEmployee(emplcode, context);

//        Log.d("Message ", employee.length);
        if (employee.length < 2 ){
            return employee[0];
        } else {
            // Storing login value as TRUE
            editor.putBoolean(IS_ACTIVE, true);

            // Storing emplcode in pref
            editor.putString(KEY_EMPLCODE, employee[0]);

            // Storing emplname in pref
            editor.putString(KEY_NAME, employee[1]);

            // commit changes
            editor.commit();

            return null;
        }
    }

    public void createLocationSession(String locationID, Context context){
        DBHandlerLocations sqliteLocation = new DBHandlerLocations(context);
        LocationModel location;

        location = sqliteLocation.getLocation(locationID);

        // Storing location in pref
        editor.putString(KEY_LOCATION, location.getPl_place());

        editor.putString(KEY_IDLOCATION, locationID);

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
