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
    public String[] getEmployee(String EmplCode, Context activity) {
        String[] data = new String[2];

        connect = sqlConnect.connect(DATABASE_NAME);
        String Query = "SELECT EM_EmplCode, EM_EmplName FROM " + TABLE + " WHERE EM_EmplCode = " + EmplCode;
        if (connect == null)
        {
            message[0] = "Check Your Connection!";
        } else {
            try {
                Statement statement = connect.createStatement();
                rs = statement.executeQuery(Query);
                // return count
                rs.next();
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                connect.close();
                success = true;
            } catch (SQLException e) {
//                Log.d("Error : ", e.getMessage());
                message[0] = e.getMessage();
//                Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        if (success){
            return data;
        } else {
            return message;
        }
    }

}
