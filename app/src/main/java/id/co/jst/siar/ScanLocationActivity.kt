package id.co.jst.siar

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

import com.google.zxing.integration.android.IntentIntegrator

class ScanLocationActivity : AppCompatActivity() {
    var btn_next: Button
    var btn_scan: Button
    var location_barcode: EditText
    internal var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_location)

        session = SessionManager(this)

        btn_next = findViewById(R.id.button2) as Button
        btn_scan = findViewById(R.id.button) as Button
        location_barcode = findViewById(R.id.editText) as EditText

        val pdg = ProgressDialog(this@ScanLocationActivity)
        pdg.isIndeterminate = true
        pdg.setInverseBackgroundForced(false)
        pdg.setCanceledOnTouchOutside(true)
        pdg.setMessage("Please Wait ...")
        pdg.setCancelable(false)

        btn_scan.setOnClickListener {
            val integrator = IntentIntegrator(this@ScanLocationActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
            val intent = integrator.createScanIntent()
            startActivityForResult(intent, 0)
        }

        btn_next.setOnClickListener {
            if (location_barcode.text.toString().isEmpty()) {
                Toast.makeText(this@ScanLocationActivity, "Barcode lokasi harus diisi.", Toast.LENGTH_LONG).show()
            } else {
                pdg.show()
                val tmp = Integer.parseInt(location_barcode.text.toString().substring(3))
                session.createLocationSession(Integer.toString(tmp))
                object : AsyncTask<Void, Void, Void>() {
                    override fun doInBackground(vararg params: Void): Void? {
                        pdg.dismiss()
                        runOnUiThread {
                            val i = Intent(applicationContext, InventoryActivity::class.java)
                            startActivity(i)
                            this@ScanLocationActivity.finish()
                        }
                        return null
                    }
                }.execute()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (resultCode < 0) {
            val content = intent.getStringExtra("SCAN_RESULT")
            when (requestCode) {
                0 -> location_barcode.setText(content)
            }
        } else {
            Toast.makeText(this@ScanLocationActivity, "Cancel scan by user", Toast.LENGTH_LONG).show()
        }

    }
}
