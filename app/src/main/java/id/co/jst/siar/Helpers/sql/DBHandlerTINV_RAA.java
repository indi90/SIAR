package id.co.jst.siar.Helpers.sql;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.Models.sql.TINV_RAAModel;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerTINV_RAA {
    private static final String DATABASE_NAME = "JSTACCFINDB";
    // Contacts table name
    private static final String TABLE = "TINV_RAA";

    private Connection connect;
    private ResultSet rs;
    private DBHandlerConnection sqlConnect = new DBHandlerConnection();

    // Getting locations Count
    public int getRAACount(Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        String countQuery = "SELECT COUNT(*) FROM " + TABLE + " WHERE IRDeptCode = 7";
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
//                Log.d("Reading: ", "Count Test ..." + countQuery);
//                            List<Map<String, String>> data = null;
//                            data = new ArrayList<Map<String, String>>();
                //
//                            while (rs.next()) {
//                                Map<String, String> datanum = new HashMap<String, String>();
//                                datanum.put("A", rs.getString("PL_PLACE"));
//                                Log.d("Reading: ", "Count Test ..." + rs.getString("PL_PLACE"));
//                                data.add(datanum);
//                            }
                //            String[] fromwhere = { "A" };
                //            int[] viewswhere = { R.id.lblcountryname };
                //            ADAhere = new SimpleAdapter(CountryList.this, data,
                //                    R.layout.listtemplate, fromwhere, viewswhere);
                //            lstcountry.setAdapter(ADAhere);

            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);

        return count;
    }

    // Getting All Locations
    public List<TINV_RAAModel> getAllRAA(String emplcode, Context activity) {
        List<TINV_RAAModel> RAAList = new ArrayList<TINV_RAAModel>();
        connect = sqlConnect.connect(DATABASE_NAME);
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE + " WHERE IRDeptCode = (SELECT Section_ID" +
                "  FROM TBMST_Section INNER JOIN  TBMST_SectionDet" +
                "  ON Section_ID = SED_HeaderID INNER JOIN jincommon..tbmst_employee" +
                "  ON em_sectioncode = sed_sectioncode" +
                "  WHERE em_emplcode = " + emplcode + ")";
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
