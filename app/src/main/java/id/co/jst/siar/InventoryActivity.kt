package id.co.jst.siar

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.zxing.integration.android.IntentIntegrator

import java.text.SimpleDateFormat
import java.util.Date

import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAActual
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod
import id.co.jst.siar.Models.sqlite.LocationModel
import id.co.jst.siar.Models.sqlite.RAAActualModel
import id.co.jst.siar.Models.sqlite.RAAModel
import java.lang.Integer.parseInt

class InventoryActivity : AppCompatActivity() {
    var btn_scan: Button
    var btn_submit: Button
    var btn_done: Button
    private var raa_barcode: EditText? = null
    var asset_number: TextView
    var model: TextView
    var serial_number: TextView
    var location: TextView
    var pic: TextView
    var locationActual: TextView
    var balance: TextView
    private val sqliteRAA = DBHandlerRAA(this)
    private val sqliteRAAActual = DBHandlerRAAActual(this)
    private val sqliteRAAPeriod = DBHandlerRAAPeriod(this)
    private var barcode: Int? = null
    private var remains: Int = 0
    val session: SessionManager
    private val dNow = Date()
    private val ft = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        session = SessionManager(this)
        val user = session.userDetails

        pic = findViewById(R.id.textView14) as TextView
        raa_barcode = findViewById(R.id.editText2) as EditText
        asset_number = findViewById(R.id.textView22) as TextView
        model = findViewById(R.id.textView24) as TextView
        serial_number = findViewById(R.id.textView25) as TextView
        location = findViewById(R.id.textView26) as TextView
        locationActual = findViewById(R.id.textView15) as TextView
        btn_scan = findViewById(R.id.button8) as Button
        btn_done = findViewById(R.id.button9) as Button
        btn_submit = findViewById(R.id.button11) as Button
        balance = findViewById(R.id.textView2) as TextView

        //        Log.d("Trial ",user.get(SessionManager.KEY_REMAINS));
        balance.text = user[SessionManager.KEY_REMAINS] + " of " + user[SessionManager.KEY_ALL]
        remains = Integer.parseInt(user[SessionManager.KEY_REMAINS])
        locationActual.text = " " + user[SessionManager.KEY_LOCATION]
        pic.text = " " + user[SessionManager.KEY_EMPLCODE] + " - " + user[SessionManager.KEY_NAME]

        btn_scan.setOnClickListener {
            val integrator = IntentIntegrator(this@InventoryActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
            val intent = integrator.createScanIntent()
            startActivityForResult(intent, 0)
        }

        btn_done.setOnClickListener {
            val asset_number_ = asset_number.text.toString()
            val model_ = model.text.toString()
            val serial_number_ = serial_number.text.toString()
            val location_ = location.text.toString()
            if (asset_number_.isEmpty() && model_.isEmpty() && serial_number_.isEmpty() && location_.isEmpty()) {
                val i = Intent(applicationContext, MenuActivity::class.java)
                startActivity(i)
                this@InventoryActivity.finish()
            } else {
                AlertDialog.Builder(this@InventoryActivity)
                        .setCancelable(false)
                        .setMessage("Data Belum Disimpan.\nApakah Yakin Akan Keluar ?")
                        .setPositiveButton("Ya") { dialogInterface, i -> this@InventoryActivity.finish() }
                        .setNegativeButton("Tidak", null).show()
            }
        }

        btn_submit.setOnClickListener {
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void): Void? {
                    val RAA = sqliteRAA.getRAAforRAAActual(barcode!!)
                    println("date print " + ft.format(dNow))
                    sqliteRAAActual.addRAAActual(RAAActualModel(RAA.irPeriodID, RAA.irAssetNo, RAA.irModel, RAA.irmfgNo, parseInt(user[SessionManager.KEY_IDLOCATION]), RAA.irGenerateDate, RAA.irGenerateUser, RAA.irDeptCode, user[SessionManager.KEY_NAME], ft.format(dNow)))
                    return null
                }
            }.execute()
            remains = remains - 1
            asset_number.text = ""
            model.text = ""
            serial_number.text = ""
            location.text = ""
            raa_barcode!!.setText("")
            session.updateBalanceSession(remains)
            balance.text = remains.toString() + " of " + user[SessionManager.KEY_ALL]
            Toast.makeText(this@InventoryActivity, "Berhasil Menambah Data", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (resultCode < 0) {
            val content = intent.getStringExtra("SCAN_RESULT")
            val asseno = Integer.parseInt(content)
            Log.d("trial", Integer.toString(asseno))
            when (requestCode) {
                0 -> {
                    raa_barcode!!.setText(content)
                    val result = sqliteRAA.getRAA(asseno)
                    Log.d("result length", Integer.toString(result.size))
                    if (result.size < 2) {
                        Toast.makeText(this@InventoryActivity, result[0].toString(), Toast.LENGTH_LONG).show()
                    } else {
                        asset_number.text = " $content"
                        model.text = " " + (result[0] as RAAModel).irModel!!
                        serial_number.text = " " + (result[0] as RAAModel).irmfgNo!!
                        location.text = " " + (result[1] as LocationModel).pl_place!!
                        barcode = parseInt(content.substring(4))
                    }
                }
            }
        } else {
            Toast.makeText(this@InventoryActivity, "Cancel scan by user", Toast.LENGTH_LONG).show()
        }

    }
}
