package id.co.jst.siar.Helpers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.Models.sqlite.LocationModel;

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

public class DBHandlerLocations extends DBHandlerSQLite{

    public DBHandlerLocations(Context context) {
        super(context);
    }

    // Adding new location
    public void addLocation(LocationModel location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, location.getPl_code());
        values.put(KEY_BUILDING, location.getPl_building());
        values.put(KEY_FLOOR, location.getPl_floor());
        values.put(KEY_PLACE, location.getPl_place());
        values.put(KEY_DESCRIPTION, location.getPl_description());
        values.put(KEY_DATE, location.getPl_date().toString());
        // Inserting Row
        db.insert(TABLE_LOCATION, null, values);
        db.close(); // Closing database connection
    }

    // Deleting a shop
    public void deleteLocation(LocationModel location) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATION, KEY_ID + " = ?",
                new String[] { String.valueOf(location.getPl_code()) });
        db.close();
    }

    // Getting locations Count
    public int getLocationCount() {
        String countQuery = "SELECT * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting location
    public LocationModel getLocation(String pl_code) {
        String query = "SELECT * FROM " + TABLE_LOCATION + " WHERE pl_code = " + pl_code;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationModel location = new LocationModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        // return count
        return location;
    }

    // Truncate Location
    public void truncateLocation() {
        Boolean success;
        String truncateQuery = "DELETE FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(truncateQuery);
    }

    // Getting All Locations
    public List<LocationModel> getAllLocations() {
        List<LocationModel> locationList = new ArrayList<LocationModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LOCATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LocationModel location = new LocationModel();
                location.setPl_code(Integer.parseInt(cursor.getString(0)));
                location.setPl_building(cursor.getString(1));
                location.setPl_floor(cursor.getString(2));
                location.setPl_place(cursor.getString(3));
                location.setPl_description(cursor.getString(4));
                // Adding to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }
}
