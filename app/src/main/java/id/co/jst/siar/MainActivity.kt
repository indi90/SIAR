package id.co.jst.siar

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.facebook.stetho.Stetho
import com.google.zxing.integration.android.IntentIntegrator

import java.util.HashMap

import id.co.jst.siar.Helpers.sql.DBHandlerTBMST_Employee
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAAPeriod
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod
import id.co.jst.siar.Models.sql.TINV_RAAPeriodModel
import id.co.jst.siar.Models.sqlite.RAAPeriodModel

import android.R.id.message

class MainActivity : AppCompatActivity() {
    var btn_submit_pic: Button
    var btn_scan_barcode: Button
    var pic_barcode: EditText
    private var session: SessionManager? = null
    private val sqlRAAPeriod = DBHandlerTINV_RAAPeriod()
    private val sqlTBMSTEmployee = DBHandlerTBMST_Employee()
    private val sqliteRAAPeriod = DBHandlerRAAPeriod(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Stetho.initializeWithDefaults(this)
        setContentView(R.layout.activity_main)

        session = SessionManager(this)

        btn_submit_pic = findViewById(R.id.button2) as Button
        btn_scan_barcode = findViewById(R.id.button) as Button
        pic_barcode = findViewById(R.id.editText) as EditText

        val pdg = ProgressDialog(this@MainActivity)
        pdg.isIndeterminate = true
        pdg.setInverseBackgroundForced(false)
        pdg.setCanceledOnTouchOutside(true)
        pdg.setMessage("Please Wait ...")
        pdg.setCancelable(false)
        sqliteRAAPeriod.truncatePeriod()

        btn_scan_barcode.setOnClickListener {
            val integrator = IntentIntegrator(this@MainActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            val intent = integrator.createScanIntent()
            startActivityForResult(intent, 0)
        }

        btn_submit_pic.setOnClickListener {
            val pic = pic_barcode.text.toString()
            val activePeriod = sqlRAAPeriod.getPeriodCount(this@MainActivity)
            if (activePeriod > 0) {
                val period = sqlRAAPeriod.getActivePeriod(this@MainActivity)
                //                    RAAPeriodModel sqlitePeriod = new RAAPeriodModel(period.getIRPID(), period.getIRPPeriod(), period.getIRPYear(), period.getIRPMonth(), period.getIRPStatus(), period.getIRPGenerateDate(), period.getIRPInventoryOpen(), period.getIRPInventoryClose());
                sqliteRAAPeriod.addPeriod(RAAPeriodModel(period.irpid, period.irpPeriod, period.irpYear, period.irpMonth, period.irpStatus, period.irpGenerateDate, period.irpInventoryOpen, period.irpInventoryClose))
                if (pic.isEmpty()) {
                    Toast.makeText(this@MainActivity, "PIC Barcode Harus Diisi.", Toast.LENGTH_LONG).show()
                } else {
                    object : AsyncTask<Void, Void, Void>() {
                        override fun onPreExecute() {
                            pdg.show()
                        }

                        override fun doInBackground(vararg params: Void): Void? {
                            val barcode = sqlTBMSTEmployee.checkBarcode(pic)
                            if (barcode.size < 2) {
                                runOnUiThread {
                                    if (barcode[0] == "No current row in the ResultSet.") {
                                        Toast.makeText(this@MainActivity, "Barcode Tidak Terdaftar.", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this@MainActivity, barcode[0], Toast.LENGTH_LONG).show()
                                    }
                                }
                                pdg.dismiss()
                            } else {
                                if (barcode[0] == "0") {
                                    runOnUiThread { Toast.makeText(this@MainActivity, "Maaf, Barcode Anda Tidak Dapat Digunakan.", Toast.LENGTH_LONG).show() }
                                    pdg.dismiss()
                                } else {
                                    val message = session!!.createPICSession(barcode[1])
                                    pdg.dismiss()
                                    if (message != null) {
                                        runOnUiThread { Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show() }
                                    } else {
                                        runOnUiThread {
                                            //                                                    Log.d("Trial :",user.get(SessionManager.KEY_DIVISION));
                                            val user = session!!.userDetails
                                            session!!.createBalanceSession(user[SessionManager.KEY_DEPARTMENT], user[SessionManager.KEY_DIVISION], sqliteRAAPeriod.checkPeriod())
                                            //                                                    Log.d("Trial :",user.get(SessionManager.KEY_REMAINS));
                                            //                                            Toast.makeText(MainActivity.this, user.get(SessionManager.KEY_SECTION),Toast.LENGTH_LONG).show();
                                            val i = Intent(applicationContext, MenuActivity::class.java)
                                            startActivity(i)
                                        }
                                    }
                                }
                            }

                            return null
                        }
                    }.execute()
                }
            } else {
                Toast.makeText(this@MainActivity, "Periode Inventory Tidak Tersedia.", Toast.LENGTH_LONG).show()
            }
        }
    }

    //when press button back
    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you sure you want to exit ?")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    session!!.clearAllData()
                    this@MainActivity.finish()
                }
                .setNegativeButton("No", null).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (resultCode < 0) {
            val content = intent.getStringExtra("SCAN_RESULT")
            when (requestCode) {
                0 -> pic_barcode.setText(content)
            }
        } else {
            Toast.makeText(this@MainActivity, "Cancel scan by user", Toast.LENGTH_LONG).show()
        }

    }
}
