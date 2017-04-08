package id.co.jst.siar.Helpers.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static net.sourceforge.jtds.jdbc.DefaultProperties.DATABASE_NAME;

/**
 * Created by endro.ngujiharto on 4/8/2017.
 */

public class DBHandlerSQLite extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SIAR";
    // Location table name
    protected static final String TABLE_LOCATION = "Locations";
    // Location Table Columns names
    protected static final String KEY_ID = "pl_code";
    protected static final String KEY_BUILDING = "pl_building";
    protected static final String KEY_FLOOR = "pl_floor";
    protected static final String KEY_PLACE = "pl_place";
    protected static final String KEY_DESCRIPTION = "pl_description";
    protected static final String KEY_DATE = "pl_date";
    // RAA Table names
    protected static final String TABLE_RAA = "RAA";
    // RAA Table Columns names
    protected static final String KEY_IRPERIODID = "IRPeriodID";
    protected static final String KEY_IRASSETNO = "IRAssetNo";
    protected static final String KEY_IRMODEL = "IRModel";
    protected static final String KEY_IRMFGNO = "IRMFGNo";
    protected static final String KEY_IRLOCATIONID = "IRLocationID";
    protected static final String KEY_IRGENERATEDATE = "IRGenerateDate";
    protected static final String KEY_IRGENERATEUSER = "IRGenerateUser";
    protected static final String KEY_IRDEPTCODE = "IRDeptCode";
    // RAAACTUAL table name
    protected static final String TABLE_RAAACTUAL = "RAAACTUAL";

    // Table Create Statements
    // Location table create statement
    private static final String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_BUILDING + " VARCHAR,"
            + KEY_FLOOR + " VARCHAR,"
            + KEY_PLACE + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_DATE + " DATE" + ")";

    // RAA table create statement
    private static final String CREATE_RAA_TABLE = "CREATE TABLE " + TABLE_RAA + " ("
            + KEY_IRPERIODID + " INTEGER,"
            + KEY_IRASSETNO + " INTEGER,"
            + KEY_IRMODEL + " TEXT,"
            + KEY_IRMFGNO + " VARCHAR,"
            + KEY_IRLOCATIONID + " INTEGER,"
            + KEY_IRGENERATEDATE + " DATE,"
            + KEY_IRGENERATEUSER + " VARCHAR,"
            + KEY_IRDEPTCODE + " INTEGER,"
            + "PRIMARY KEY ("+ KEY_IRPERIODID +", "+ KEY_IRASSETNO +")"
            + ")";

    // RAA table create statement
    private static final String CREATE_RAAACTUAL_TABLE = "CREATE TABLE " + TABLE_RAAACTUAL + " ("
            + KEY_IRPERIODID + " INTEGER,"
            + KEY_IRASSETNO + " INTEGER,"
            + KEY_IRMODEL + " TEXT,"
            + KEY_IRMFGNO + " VARCHAR,"
            + KEY_IRLOCATIONID + " INTEGER,"
            + KEY_IRGENERATEDATE + " DATE,"
            + KEY_IRGENERATEUSER + " VARCHAR,"
            + KEY_IRDEPTCODE + " INTEGER,"
            + "PRIMARY KEY ("+ KEY_IRPERIODID +", "+ KEY_IRASSETNO +")"
            + ")";

    public DBHandlerSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_RAAACTUAL_TABLE);
        db.execSQL(CREATE_RAA_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAAACTUAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        // Creating tables again
        onCreate(db);
    }
}
