package id.co.jst.siar.Helpers.sql

import android.os.StrictMode
import android.util.Log

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

//import static android.R.attr.password;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class DBHandlerConnection {

    fun connect(database: String): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var connection: Connection? = null
        var ConnectionURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            ConnectionURL = ("jdbc:jtds:sqlserver://" + DATABASE_SERVER + ";"
                    + "databaseName=" + database + ";user=" + DATABASE_USER + ";password="
                    + DATABASE_PASS + ";")
            connection = DriverManager.getConnection(ConnectionURL)
        } catch (se: SQLException) {
            Log.e("error here 1 : ", se.message)
        } catch (e: ClassNotFoundException) {
            Log.e("error here 2 : ", e.message)
        } catch (e: Exception) {
            Log.e("error here 3 : ", e.message)
        }

        return connection
    }

    companion object {
        // Database Name
        private val DATABASE_SERVER = "192.9.18.1"
        private val DATABASE_USER = "gapura"
        private val DATABASE_PASS = "gapura"
    }
}
