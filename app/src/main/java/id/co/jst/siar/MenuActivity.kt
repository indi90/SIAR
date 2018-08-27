package id.co.jst.siar

import android.app.ProgressDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAA
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAAActual
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations
import id.co.jst.siar.Helpers.sql.DBHandlerTBMFAPlace
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAActual
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod
import id.co.jst.siar.Models.sql.TINV_RAAActualModel
import id.co.jst.siar.Models.sql.TINV_RAAModel
import id.co.jst.siar.Models.sqlite.LocationModel
import id.co.jst.siar.Models.sql.TBMFAPlaceModel
import id.co.jst.siar.Models.sqlite.RAAActualModel
import id.co.jst.siar.Models.sqlite.RAAModel

class MenuActivity : AppCompatActivity() {
    var btn_down_location: ImageButton
    var btn_down_raa: ImageButton
    var btn_inventory: ImageButton
    var btn_upload: ImageButton
    private val sqliteLocation = DBHandlerLocations(this)
    private val sqliteRAA = DBHandlerRAA(this)
    private val sqliteRAAActual = DBHandlerRAAActual(this)
    private val sqliteRAAPeriod = DBHandlerRAAPeriod(this)
    private val sqlPlace = DBHandlerTBMFAPlace()
    private val sqlRAA = DBHandlerTINV_RAA()
    private val sqlRAAActual = DBHandlerTINV_RAAActual()
    private val dNow = Date()
    private val ft = SimpleDateFormat("yyyy-MM-dd")
    private var places: List<TBMFAPlaceModel>? = null
    private var RAAs: List<TINV_RAAModel>? = null
    private var resultRAAActual: Array<String>? = null


    // Session Manager Class
    internal var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        session = SessionManager(this)

        // get user data from session
        val user = session.userDetails

        btn_down_location = findViewById(R.id.imageButton2) as ImageButton
        btn_down_raa = findViewById(R.id.imageButton1) as ImageButton
        btn_inventory = findViewById(R.id.imageButton4) as ImageButton
        btn_upload = findViewById(R.id.imageButton3) as ImageButton

        val pdg = ProgressDialog(this@MenuActivity)
        pdg.isIndeterminate = true
        pdg.setInverseBackgroundForced(false)
        pdg.setCanceledOnTouchOutside(true)
        pdg.setMessage("Please Wait ...")
        pdg.setCancelable(false)

        btn_down_location.setOnClickListener {
            pdg.show()
            //                Log.d("Insert: ", "Inserting ..");
            //                db.addLocation(new LocationModel(1, "Dockers", "475 Brannan St", "#330, San Francisco", "CA 94107", ft.format(dNow)));
            //                db.addLocation(new LocationModel(12, "Dockers 2", "475 Brannan St 2", "#330, San Francisco 2", "CA 94107 2", ft.format(dNow)));
            object : AsyncTask<Void, Void, Void>() {
                override fun onPreExecute() {
                    places = sqlPlace.getAllPlaces(this@MenuActivity)
                    if (places!!.isEmpty()) {
                        pdg.dismiss()
                    }
                }

                override fun doInBackground(vararg params: Void): Void? {
                    if (!places!!.isEmpty()) {
                        sqliteLocation.truncateLocation()
                        if (sqliteLocation.locationCount == 0) {
                            for (place in places!!) {
                                sqliteLocation.addLocation(LocationModel(place.pl_code, place.pl_building, place.pl_floor, place.pl_place, place.pl_description, ft.format(dNow)))
                            }
                            if (sqliteLocation.locationCount == sqlPlace.getPlacesCount(this@MenuActivity)) {
                                pdg.dismiss()
                                // To dismiss the dialog
                                runOnUiThread {
                                    //update ui here
                                    Toast.makeText(this@MenuActivity, "Berhasil Mengambil Lokasi", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    return null
                }
            }.execute()
        }

        btn_down_raa.setOnClickListener {
            pdg.show()
            //                Log.d("Insert: ", "Inserting ..");
            //                db.addLocation(new LocationModel(1, "Dockers", "475 Brannan St", "#330, San Francisco", "CA 94107", ft.format(dNow)));
            //                db.addLocation(new LocationModel(12, "Dockers 2", "475 Brannan St 2", "#330, San Francisco 2", "CA 94107 2", ft.format(dNow)));
            object : AsyncTask<Void, Void, Void>() {
                override fun onPreExecute() {
                    RAAs = sqlRAA.getAllRAA(user[SessionManager.KEY_DEPARTMENT], user[SessionManager.KEY_DIVISION], sqliteRAAPeriod.checkPeriod(), this@MenuActivity)
                    if (RAAs!!.isEmpty()) {
                        pdg.dismiss()
                    }
                }

                override fun doInBackground(vararg params: Void): Void? {
                    if (!RAAs!!.isEmpty()) {
                        sqliteRAA.truncateRAA()
                        if (sqliteRAA.raaCount == 0) {
                            for (RAA in RAAs!!) {
                                Log.d("Insert: ", "Inserting .." + RAA.irAssetNo + " " + RAA.irLocationID)
                                sqliteRAA.addRAA(RAAModel(RAA.irPeriodID, RAA.irAssetNo, RAA.irModel, RAA.irmfgNo, RAA.irLocationID, RAA.irGenerateDate, RAA.irGenerateUser, RAA.irDeptCode))
                            }
                        }

                        if (sqliteRAA.raaCount == sqlRAA.getRAACount(user[SessionManager.KEY_DEPARTMENT], user[SessionManager.KEY_DIVISION], sqliteRAAPeriod.checkPeriod(), this@MenuActivity)) {
                            pdg.dismiss()
                            // To dismiss the dialog
                            runOnUiThread {
                                //update ui here
                                Toast.makeText(this@MenuActivity, "Berhasil Mengambil RAA", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            //update ui here
                            Toast.makeText(this@MenuActivity, "Data Inventory Belum diGenerate", Toast.LENGTH_LONG).show()
                        }

                    }
                    return null
                }
            }.execute()
        }

        btn_inventory.setOnClickListener {
            if (sqliteRAA.raaCount != 0 && sqliteLocation.locationCount != 0) {
                val i = Intent(applicationContext, ScanLocationActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@MenuActivity, "Ambil RAA dan Lokasi Terlebih Dahulu.", Toast.LENGTH_LONG).show()
            }
        }

        btn_upload.setOnClickListener {
            object : AsyncTask<Void, Void, Void>() {
                override fun onPreExecute() {
                    pdg.show()
                }

                override fun doInBackground(vararg params: Void): Void? {
                    if (sqliteRAAActual.raaCount > 0) {
                        var success = true
                        val RAAActuals = sqliteRAAActual.allRAAActual
                        for (RAAACtual in RAAActuals) {
                            resultRAAActual = sqlRAAActual.addRAAActual(TINV_RAAActualModel(RAAACtual.irPeriodID, RAAACtual.irAssetNo, RAAACtual.irModel, RAAACtual.irmfgNo, RAAACtual.irLocationID, RAAACtual.irGenerateDate, RAAACtual.irGenerateUser, RAAACtual.irDeptCode, RAAACtual.irpic, RAAACtual.irScanDate), this@MenuActivity)
                            //                            Log.d("Result12 ", resultRAAActual[0]);
                            println("Result " + resultRAAActual!![0])
                            if (resultRAAActual!![0] != "") {
                                pdg.dismiss()
                                runOnUiThread {
                                    //update ui here
                                    Toast.makeText(this@MenuActivity, resultRAAActual!![0], Toast.LENGTH_LONG).show()
                                }
                                //                                Log.d("Result ", resultRAAActual[0]);
                                success = false
                                break
                            }
                        }
                        //                        Log.d("Result ", String.valueOf(success));
                        if (success) {
                            sqliteRAAActual.truncateRAA()
                            if (sqliteRAAActual.raaCount == 0) {
                                pdg.dismiss()
                                // To dismiss the dialog
                                runOnUiThread {
                                    //update ui here
                                    Toast.makeText(this@MenuActivity, "Berhasil Menyimpan RAA Actual", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        pdg.dismiss()
                        runOnUiThread {
                            //update ui here
                            Toast.makeText(this@MenuActivity, "Data RAA Actual Belum Tersedia.", Toast.LENGTH_LONG).show()
                        }
                    }
                    return null
                }
            }.execute()
        }
    }
}
