package id.co.jst.siar.Helpers.sql;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.MainActivity;
import id.co.jst.siar.Models.sql.TINV_RAAPeriodModel;

import static android.R.attr.data;

/**
 * Created by endro.ngujiharto on 9/13/2017.
 */

public class DBHandlerTINV_RAAPeriod {
    // Database Name
    private static final String DATABASE_NAME = "JSTACCFINDB";
    // Contacts table name
    private static final String TABLE = "TINV_RAAPeriod";

    private Connection connect;
    private ResultSet rs;
    private  DBHandlerConnection sqlConnect = new DBHandlerConnection();

    // Getting period Count
    public int getPeriodCount(Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
//        Log.d(Connection.(connect), "Connect : ");
        String countQuery = "SELECT COUNT(*) FROM " + TABLE + " WHERE IRPStatus = 1";
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

    // Getting Active Period
    public TINV_RAAPeriodModel getActivePeriod(Context activity) {
        TINV_RAAPeriodModel period = new TINV_RAAPeriodModel();
        connect = sqlConnect.connect(DATABASE_NAME);
        // Select All Query
        String selectQuery = "SELECT IRPID,IRPPeriod,IRPYear,IRPMonth,IRPStatus,IRPGenerateDate,IRPInventoryOpen,IRPInventoryClose FROM " + TABLE + " WHERE IRPStatus = 1";
        String z = "";
        if (connect == null)
        {
            z = "Check Your Connection!";
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show();
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(selectQuery);
                rs.next();
                period = new TINV_RAAPeriodModel(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8));
                connect.close();
            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
        // return contact list
        return period;
    }
}
