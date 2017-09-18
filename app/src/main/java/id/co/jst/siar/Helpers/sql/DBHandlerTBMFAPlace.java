package id.co.jst.siar.Helpers.sql;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.Models.sql.TBMFAPlaceModel;

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

public class DBHandlerTBMFAPlace {
    // Database Name
    private static final String DATABASE_NAME = "JSTACCFINDB";
    // Contacts table name
    private static final String TABLE = "TBMFA_FAPlace";

    private Connection connect;
    private ResultSet rs;
    private ProgressDialog mDialog;
    private  DBHandlerConnection sqlConnect = new DBHandlerConnection();

    // Getting locations Count
    public int getPlacesCount(Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        String countQuery = "SELECT COUNT(*) FROM " + TABLE;
        if (connect == null)
        {
            z = "Check Your Connection!";
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show();
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(countQuery);
                // return count
                rs.next();
                count = rs.getInt(1);
                connect.close();
            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
        return count;
    }

    // Getting All Locations
    public List<TBMFAPlaceModel> getAllPlaces(Context activity) {
        List<TBMFAPlaceModel> placeList = new ArrayList<TBMFAPlaceModel>();
        connect = sqlConnect.connect(DATABASE_NAME);
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE;
        String z = "";
        if (connect == null)
        {
            z = "Check Your Connection!";
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show();
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(selectQuery);
                while (rs.next()) {
                    TBMFAPlaceModel place = new TBMFAPlaceModel();
                    place.setPl_code(Integer.parseInt(rs.getString(1)));
                    place.setPl_building(rs.getString(2));
                    place.setPl_floor(rs.getString(3));
                    place.setPl_place(rs.getString(4));
                    place.setPl_description(rs.getString(5));
                    // Adding to list
                    placeList.add(place);
                }
                connect.close();
            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        // return contact list
        return placeList;
    }

}
