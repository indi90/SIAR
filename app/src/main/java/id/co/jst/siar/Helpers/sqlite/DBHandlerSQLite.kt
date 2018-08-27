package id.co.jst.siar.Helpers.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//import static net.sourceforge.jtds.jdbc.DefaultProperties.DATABASE_NAME;

/**
 * Created by endro.ngujiharto on 4/8/2017.
 */

open class DBHandlerSQLite(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // creating required tables
        db.execSQL(CREATE_RAAACTUAL_TABLE)
        db.execSQL(CREATE_RAA_TABLE)
        db.execSQL(CREATE_LOCATION_TABLE)
        db.execSQL(CREATE_RAAPERIOD_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RAAACTUAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RAA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RAAPERIOD")
        // Creating tables again
        onCreate(db)
    }

    companion object {

        // Database Version
        private val DATABASE_VERSION = 1
        // Database Name
        private val DATABASE_NAME = "SIAR.db"

        // Location table name
        protected val TABLE_LOCATION = "Locations"
        // Location Table Columns names
        protected val KEY_ID = "pl_code"
        protected val KEY_BUILDING = "pl_building"
        protected val KEY_FLOOR = "pl_floor"
        protected val KEY_PLACE = "pl_place"
        protected val KEY_DESCRIPTION = "pl_description"
        protected val KEY_DATE = "pl_date"

        // RAA Table names
        protected val TABLE_RAA = "RAA"
        // RAA Table Columns names
        protected val KEY_IRPERIODID = "IRPeriodID"
        protected val KEY_IRASSETNO = "IRAssetNo"
        protected val KEY_IRMODEL = "IRModel"
        protected val KEY_IRMFGNO = "IRMFGNo"
        protected val KEY_IRLOCATIONID = "IRLocationID"
        protected val KEY_IRGENERATEDATE = "IRGenerateDate"
        protected val KEY_IRGENERATEUSER = "IRGenerateUser"
        protected val KEY_IRDEPTCODE = "IRDeptCode"

        // RAAACTUAL table name
        protected val TABLE_RAAACTUAL = "RAAACTUAL"

        // RAA Period Table names
        protected val TABLE_RAAPERIOD = "RAAPeriod"
        // RAA Period Columns names
        protected val KEY_IRPID = "IRPID"
        protected val KEY_IRPPERIOD = "IRPPeriod"
        protected val KEY_IRPYEAR = "IRPYear"
        protected val KEY_IRPMONTH = "IRPMonth"
        protected val KEY_IRPSTATUS = "IRPStatus"
        protected val KEY_IRPGENERATEDATE = "IRPGenerateDate"
        protected val KEY_IRPINVENTORYOPEN = "IRPInventoryOpen"
        protected val KEY_IRPINVENTORYCLOSE = "IRPInventoryClose"
        protected val KEY_IRPIC = "IRPIC"
        protected val KEY_IRSCANDATE = "IRScanDate"

        // Table Create Statements
        // Location table create statement
        private val CREATE_LOCATION_TABLE = ("CREATE TABLE " + TABLE_LOCATION + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_BUILDING + " VARCHAR,"
                + KEY_FLOOR + " VARCHAR,"
                + KEY_PLACE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " DATE" + ")")

        // RAA table create statement
        private val CREATE_RAA_TABLE = ("CREATE TABLE " + TABLE_RAA + " ("
                + KEY_IRPERIODID + " INTEGER,"
                + KEY_IRASSETNO + " INTEGER,"
                + KEY_IRMODEL + " TEXT,"
                + KEY_IRMFGNO + " VARCHAR,"
                + KEY_IRLOCATIONID + " INTEGER,"
                + KEY_IRGENERATEDATE + " DATE,"
                + KEY_IRGENERATEUSER + " VARCHAR,"
                + KEY_IRDEPTCODE + " INTEGER,"
                + "PRIMARY KEY (" + KEY_IRPERIODID + ", " + KEY_IRASSETNO + ")"
                + ")")

        // RAA Actual table create statement
        private val CREATE_RAAACTUAL_TABLE = ("CREATE TABLE " + TABLE_RAAACTUAL + " ("
                + KEY_IRPERIODID + " INTEGER,"
                + KEY_IRASSETNO + " INTEGER,"
                + KEY_IRMODEL + " TEXT,"
                + KEY_IRMFGNO + " VARCHAR,"
                + KEY_IRLOCATIONID + " INTEGER,"
                + KEY_IRGENERATEDATE + " DATE,"
                + KEY_IRGENERATEUSER + " VARCHAR,"
                + KEY_IRDEPTCODE + " INTEGER,"
                + KEY_IRPIC + " VARCHAR,"
                + KEY_IRSCANDATE + " DATE,"
                + "PRIMARY KEY (" + KEY_IRPERIODID + ", " + KEY_IRASSETNO + ")"
                + ")")

        // RAA Period table create statement
        private val CREATE_RAAPERIOD_TABLE = ("CREATE TABLE " + TABLE_RAAPERIOD + " ("
                + KEY_IRPID + " INTEGER,"
                + KEY_IRPPERIOD + " INTEGER,"
                + KEY_IRPYEAR + " INTEGER,"
                + KEY_IRPMONTH + " INTEGER,"
                + KEY_IRPSTATUS + " INTEGER,"
                + KEY_IRPGENERATEDATE + " DATE,"
                + KEY_IRPINVENTORYOPEN + " DATE,"
                + KEY_IRPINVENTORYCLOSE + " DATE,"
                + "PRIMARY KEY (" + KEY_IRPID + ")"
                + ")")
    }
}
