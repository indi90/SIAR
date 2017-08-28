package id.co.jst.siar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.zxing.integration.android.IntentIntegrator;

import id.co.jst.siar.Helpers.Helpers;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Button btn_submit_pic, btn_scan_barcode;
    public EditText pic_barcode;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        btn_submit_pic = (Button)findViewById(R.id.button2);
        btn_scan_barcode = (Button)findViewById(R.id.button);
        pic_barcode = (EditText)findViewById(R.id.editText);

        final ProgressDialog pdg = new ProgressDialog(MainActivity.this);
        pdg.setIndeterminate(true);
        pdg.setInverseBackgroundForced(false);
        pdg.setCanceledOnTouchOutside(true);
        pdg.setMessage("Please Wait ...");
        pdg.setCancelable(false);

//        Toast.makeText(MainActivity.this, Helpers.getMAC(),Toast.LENGTH_LONG).show();

        btn_scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            Intent intent = integrator.createScanIntent();
            startActivityForResult(intent, 0);
            }
        });

        btn_submit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String pic = pic_barcode.getText().toString();
            if (pic.isEmpty()){
                Toast.makeText(MainActivity.this, "PIC Barcode Harus Diisi.",Toast.LENGTH_LONG).show();
            } else {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        pdg.show();
                    }

                    protected Void doInBackground(Void... params) {
                        final String message = session.createPICSession(pic, MainActivity.this);
                        pdg.dismiss();
                        if (message != null){
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    Toast.makeText(MainActivity.this, message,Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(i);
                                }
                            });
                        }
                        return null;
                    }
                }.execute();
            }
            }
        });
    }

    //when press button back
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you sure you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        session.clearAllData();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode < 0) {
            String content = intent.getStringExtra("SCAN_RESULT");
            switch (requestCode) {
                case 0:
                    pic_barcode.setText(content);
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Cancel scan by user", Toast.LENGTH_LONG).show();
        }

    }
}
