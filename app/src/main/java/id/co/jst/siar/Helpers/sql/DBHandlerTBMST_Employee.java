package id.co.jst.siar.Helpers.sql;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.R.attr.data;

/**
 * Created by endro.ngujiharto on 4/18/2017.
 */

public class DBHandlerTBMST_Employee {

    private static final String DATABASE_NAME = "JINCOMMON";
    // Contacts table name
    private static final String TABLE = "TBMST_employee";

    private Connection connect;
    private ResultSet rs;
    private  DBHandlerConnection sqlConnect = new DBHandlerConnection();
    private boolean success = false;
    private String[] message = new String[1];

    // Getting employee
    public String[] getEmployee(String EmplCode) {
        String[] data = new String[7];

        connect = sqlConnect.connect(DATABASE_NAME);
        String Query = "SELECT em_emplCode, em_emplname, em_sectioncode, sec_sectionnaming, sec_departmentnaming, sec_department, sec_section FROM " + TABLE + " inner JOIN TBMST_SECTION ON em_sectioncode = sec_sectioncode WHERE EM_EmplCode = " + EmplCode +" and sec_status = 1";
        if (connect == null)
        {
            message[0] = "Check Your Connection!";
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(Query);
                rs.next();
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                data[6] = rs.getString(7);
                connect.close();
                success = true;
            } catch (SQLException e) {
                message[0] = e.getMessage();
            }
        }

        if (success){
            return data;
        } else {
            return message;
        }
    }

    public String[] checkBarcode(String barcode){
        String[] data = new String[2];
        connect = sqlConnect.connect(DATABASE_NAME);
        String Query = "SELECT AMU_MINVRAA, amu_employeeno FROM TBAM_User WHERE AMU_QRCode = '" + barcode + "'";

        if (connect == null)
        {
            message[0] = "Check Your Connection!";
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(Query);
                rs.next();
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                connect.close();
                success = true;
            } catch (SQLException e) {
                message[0] = e.getMessage();
            }
        }

        if (success){
            return data;
        } else {
            return message;
        }
    }

}
