package id.co.jst.siar.Helpers.sql;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import id.co.jst.siar.Models.sql.TINV_RAAActualModel;
import id.co.jst.siar.Models.sql.TINV_RAAModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerTINV_RAAActual {
    private static final String DATABASE_NAME = "JSTACCFINDB";
    // Contacts table name
    private static final String TABLE = "TINV_RAAActual";

    private Connection connect;
    private ResultSet rs;
    private ProgressDialog mDialog;
    private  DBHandlerConnection sqlConnect = new DBHandlerConnection();
    private String message;

    // Adding new RAA
    public String[] addRAAActual(TINV_RAAActualModel raaActual, Context activity) {
        String[] kembali;
        connect = sqlConnect.connect(DATABASE_NAME);
        String insertQuery = "INSERT INTO " + TABLE + " (IRPeriodID, IRAssetNo, IRModel, IRMFGNo, IRLocationID, IRGenerateDate, IRGenerateUser, IRDeptCode) VALUES ('" +
                raaActual.getIRPeriodID() +
                "','" + raaActual.getIRAssetNo() +
                "','" + raaActual.getIRModel() +
                "','" + raaActual.getIRMFGNo() +
                "','" + raaActual.getIRLocationID() +
                "','" + raaActual.getIRGenerateDate() +
                "','" + raaActual.getIRGenerateUser() +
                "','" + raaActual.getIRDeptCode() + "')";
        if (connect == null)
        {
            message = "Check Your Connection!";
            kembali = new String[]{message};
        } else {
            try {
                PreparedStatement statement = connect.prepareStatement(insertQuery);
                statement.executeUpdate();

                connect.close();
                kembali = new String[]{""};
            } catch (SQLException e) {
                message = e.getMessage();
                kembali = new String[]{message};
//                Log.d("Reading: ", "Count Test ..." + e.getMessage().toString());
//                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
        return kembali;
    }

    // Getting locations Count
    public int getRAAActualCount(Context activity) {
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

    // Getting All RAAActual
    public List<TINV_RAAActualModel> getAllRAAActual(Context activity) {
        List<TINV_RAAActualModel> RAAActualList = new ArrayList<TINV_RAAActualModel>();
        connect = sqlConnect.connect(DATABASE_NAME);
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE + " WHERE IRDeptCode = 7";
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
                    TINV_RAAActualModel RAAActual = new TINV_RAAActualModel();
                    RAAActual.setIRPeriodID(Integer.parseInt(rs.getString(1)));
                    RAAActual.setIRAssetNo(Integer.parseInt(rs.getString(2)));
                    RAAActual.setIRModel(rs.getString(3));
                    RAAActual.setIRMFGNo(rs.getString(4));
                    RAAActual.setIRLocationID(Integer.parseInt(rs.getString(5)));
                    RAAActual.setIRGenerateDate(rs.getString(6));
                    RAAActual.setIRGenerateUser(rs.getString(7));
                    RAAActual.setIRDeptCode(Integer.parseInt(rs.getString(8)));
                    // Adding to list
                    RAAActualList.add(RAAActual);
                }
            } catch (SQLException e) {
                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        // return contact list
        return RAAActualList;
    }
}
