package id.co.jst.siar.Helpers.sql;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static android.R.attr.password;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class DBHandlerConnection {
    // Database Name
    private static final String DATABASE_SERVER = "192.9.18.1";
    private static final String DATABASE_USER = "gapura";
    private static final String DATABASE_PASS = "gapura";

    public Connection connect(String database)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + DATABASE_SERVER + ";"
                    + "databaseName=" + database + ";user=" + DATABASE_USER + ";password="
                    + DATABASE_PASS + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
