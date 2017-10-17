package id.co.jst.siar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.List;

import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAActual;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod;
import id.co.jst.siar.Models.sql.TINV_RAAModel;
import id.co.jst.siar.Models.sqlite.LocationModel;
import id.co.jst.siar.Models.sqlite.RAAActualModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

import static java.lang.Integer.parseInt;

public class InventoryActivity extends AppCompatActivity {
    public Button btn_scan, btn_submit, btn_done;
    private EditText raa_barcode;
    public TextView asset_number, model, serial_number, location, pic, locationActual, balance;
    private DBHandlerRAA sqliteRAA = new DBHandlerRAA(this);
    private DBHandlerRAAActual sqliteRAAActual = new DBHandlerRAAActual(this);
    private DBHandlerRAAPeriod sqliteRAAPeriod = new DBHandlerRAAPeriod(this);
    private Integer barcode;
    private int remains;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        session = new SessionManager(this);
        final HashMap<String, String> user = session.getUserDetails();

        pic = (TextView)findViewById(R.id.textView14);
        raa_barcode = (EditText)findViewById(R.id.editText2);
        asset_number = (TextView) findViewById(R.id.textView22);
        model = (TextView)findViewById(R.id.textView24);
        serial_number = (TextView)findViewById(R.id.textView25);
        location = (TextView)findViewById(R.id.textView26);
        locationActual = (TextView)findViewById(R.id.textView15);
        btn_scan = (Button)findViewById(R.id.button8);
        btn_done = (Button)findViewById(R.id.button9);
        btn_submit = (Button)findViewById(R.id.button11);
        balance = (TextView)findViewById(R.id.textView2);

//        Log.d("Trial ",user.get(SessionManager.KEY_REMAINS));
        balance.setText(user.get(SessionManager.KEY_REMAINS) + " of " + user.get(SessionManager.KEY_ALL));
        remains = Integer.parseInt(user.get(SessionManager.KEY_REMAINS));
        locationActual.setText(" " + user.get(SessionManager.KEY_LOCATION));
        pic.setText(" " + user.get(SessionManager.KEY_EMPLCODE) + " - " + user.get(SessionManager.KEY_NAME));

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(InventoryActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 0);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asset_number_ = asset_number.getText().toString();
                String model_ = model.getText().toString();
                String serial_number_ = serial_number.getText().toString();
                String location_ = location.getText().toString();
                if (asset_number_.isEmpty() && model_.isEmpty() && serial_number_.isEmpty() && location_.isEmpty()){
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                    InventoryActivity.this.finish();
                } else {
                    new AlertDialog.Builder(InventoryActivity.this)
                            .setCancelable(false)
                            .setMessage("Data Belum Disimpan.\nApakah Yakin Akan Keluar ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    InventoryActivity.this.finish();
                                }
                            })
                            .setNegativeButton("Tidak", null).show();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        RAAModel RAA = sqliteRAA.getRAAforRAAActual(barcode);
                        sqliteRAAActual.addRAAActual(new RAAActualModel(RAA.getIRPeriodID(), RAA.getIRAssetNo(), RAA.getIRModel(), RAA.getIRMFGNo(), parseInt(user.get(SessionManager.KEY_IDLOCATION)), RAA.getIRGenerateDate(), RAA.getIRGenerateUser(), RAA.getIRDeptCode()));
                        return null;
                    }
                }.execute();
                remains = remains - 1;
                asset_number.setText("");
                model.setText("");
                serial_number.setText("");
                location.setText("");
                raa_barcode.setText("");
                session.updateBalanceSession(remains);
                balance.setText(remains + " of " + user.get(SessionManager.KEY_ALL));
                Toast.makeText(InventoryActivity.this, "Berhasil Menambah Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode < 0) {
            String content = intent.getStringExtra("SCAN_RESULT");
            int asseno = Integer.parseInt(content);
            Log.d("trial", Integer.toString(asseno));
            switch (requestCode) {
                case 0:
                    raa_barcode.setText(content);
                    Object[] result = sqliteRAA.getRAA(asseno);
                    Log.d("result length", Integer.toString(result.length));
                    if (result.length < 2){
                        Toast.makeText(InventoryActivity.this, result[0].toString(), Toast.LENGTH_LONG).show();
                    } else {
                        asset_number.setText(" " + content);
                        model.setText(" " + ((RAAModel)result[0]).getIRModel());
                        serial_number.setText(" " + ((RAAModel)result[0]).getIRMFGNo());
                        location.setText(" " + ((LocationModel)result[1]).getPl_place());
                        barcode = parseInt(content.substring(4));
                    }
                    break;
            }
        } else {
            Toast.makeText(InventoryActivity.this, "Cancel scan by user", Toast.LENGTH_LONG).show();
        }

    }
}
