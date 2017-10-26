package id.co.jst.siar.Helpers.sql;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.jst.siar.Models.sql.TINV_RAAModel;
import id.co.jst.siar.SessionManager;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerTINV_RAA{
    private static final String DATABASE_NAME = "JSTACCFINDB";
    // Contacts table name
    private static final String TABLE = "TINV_RAA";
    private Connection connect;
    private ResultSet rs;
    private DBHandlerConnection sqlConnect = new DBHandlerConnection();
    private String cond = "";

    // Getting Balance
    public int getBalance(String dept, String div, int period, Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        if (!div.equals("Logistic Control Division") && !div.equals("Administration Division")){
            cond = "  AND sec_department = '" + dept + "'";
        }
        String  countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                " ) AND IRPeriodID = " + period;

//        Log.d("Trial :", countQuery);
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

    // Getting locations Count
    public int getRAACount(String dept, String div, int period, Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        if (!div.equals("Logistic Control Division") && !div.equals("Administration Division")){
            cond = "  AND sec_department = '" + dept + "'";
        }
        String countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                "  ) AND IRPeriodID = " + period;
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
    public List<TINV_RAAModel> getAllRAA(String dept, String div, int period, Context activity) {
        List<TINV_RAAModel> RAAList = new ArrayList<TINV_RAAModel>();
        connect = sqlConnect.connect(DATABASE_NAME);

        if (!div.equals("Logistic Control Division") && !div.equals("Administration Division")){
            cond = "  AND sec_department = '" + dept + "'";
        }
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE + " WHERE IRDeptCode BETWEEN (SELECT min(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                "  ) AND (SELECT max(sec_sectioncode)" +
                "  FROM jincommon..tbmst_section" +
                "  WHERE  sec_status = 1 and SEC_DIVISION = '" + div + "' "+
                cond +
                "  ) AND IRPeriodID = " + period;

        Log.d("trial", selectQuery);
        String z = "";
        if (connect == null) {
            z = "Check Your Connection!";
            Toast.makeText(activity, z, Toast.LENGTH_LONG).show();
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(selectQuery);
                while (rs.next()) {
                    TINV_RAAModel RAA = new TINV_RAAModel();
                    RAA.setIRPeriodID(Integer.parseInt(rs.getString(1)));
                    RAA.setIRAssetNo(Integer.parseInt(rs.getString(2)));
                    RAA.setIRModel(rs.getString(3));
                    RAA.setIRMFGNo(rs.getString(4));
                    RAA.setIRLocationID(Integer.parseInt(rs.getString(5)));
                    RAA.setIRGenerateDate(rs.getString(6));
                    RAA.setIRGenerateUser(rs.getString(7));
                    RAA.setIRDeptCode(Integer.parseInt(rs.getString(8)));
                    // Adding to list
                    RAAList.add(RAA);
                }
            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        // return contact list
        return RAAList;
    }
}
