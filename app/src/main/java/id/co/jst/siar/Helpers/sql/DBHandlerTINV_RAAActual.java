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
                if (e.getErrorCode() == 2627){
                    message = "";
                } else {
                    message = e.getMessage();
                }
                kembali = new String[]{message};
            }
        }
        return kembali;
    }

    public int getBalance(String emplcode, Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        String  countQuery = "SELECT count(*) FROM " + TABLE + " WHERE IRDeptCode = (SELECT Section_ID" +
                "  FROM TBMST_Section INNER JOIN  TBMST_SectionDet" +
                "  ON Section_ID = SED_HeaderID INNER JOIN jincommon..tbmst_employee" +
                "  ON em_sectioncode = sed_sectioncode" +
                "  WHERE em_emplcode = " + emplcode + ")";
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
    public int getRAAActualCount(String emplcode, Context activity) {
        int count = 0;
        String z = "";

        connect = sqlConnect.connect(DATABASE_NAME);
        String countQuery = "SELECT COUNT(*) FROM " + TABLE + " WHERE IRDeptCode = (SELECT Section_ID" +
                "  FROM TBMST_Section INNER JOIN  TBMST_SectionDet" +
                "  ON Section_ID = SED_HeaderID INNER JOIN jincommon..tbmst_employee" +
                "  ON em_sectioncode = sed_sectioncode" +
                "  WHERE em_emplcode = " + emplcode + ")";
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

    // Getting All RAAActual
    public List<TINV_RAAActualModel> getAllRAAActual(String emplcode, Context activity) {
        List<TINV_RAAActualModel> RAAActualList = new ArrayList<TINV_RAAActualModel>();
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
