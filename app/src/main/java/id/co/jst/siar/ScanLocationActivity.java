package id.co.jst.siar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanLocationActivity extends AppCompatActivity {
    public Button btn_next, btn_scan;
    public EditText location_barcode;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_location);

        session = new SessionManager(this);

        btn_next = (Button)findViewById(R.id.button2);
        btn_scan = (Button)findViewById(R.id.button);
        location_barcode = (EditText)findViewById(R.id.editText);

        final ProgressDialog pdg = new ProgressDialog(ScanLocationActivity.this);
        pdg.setIndeterminate(true);
        pdg.setInverseBackgroundForced(false);
        pdg.setCanceledOnTouchOutside(true);
        pdg.setMessage("Please Wait ...");
        pdg.setCancelable(false);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScanLocationActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 0);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (location_barcode.getText().toString().isEmpty()){
                Toast.makeText(ScanLocationActivity.this, "Barcode lokasi harus diisi.",Toast.LENGTH_LONG).show();
            } else {
                pdg.show();
                int tmp = Integer.parseInt(location_barcode.getText().toString().substring(3));
                session.createLocationSession(Integer.toString(tmp));
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        pdg.dismiss();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                            Intent i = new Intent(getApplicationContext(), InventoryActivity.class);
                            startActivity(i);
                            ScanLocationActivity.this.finish();
                            }
                        });
                        return null;
                    }
                }.execute();
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode < 0) {
            String content = intent.getStringExtra("SCAN_RESULT");
            switch (requestCode) {
                case 0:
                    location_barcode.setText(content);
                    break;
            }
        } else {
            Toast.makeText(ScanLocationActivity.this, "Cancel scan by user", Toast.LENGTH_LONG).show();
        }

    }
}
