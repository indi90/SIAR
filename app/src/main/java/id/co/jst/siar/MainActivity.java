package id.co.jst.siar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {
    public Button btn_submit_pic, btn_scan_barcode;
    public EditText pic_barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_submit_pic = (Button)findViewById(R.id.button2);
        btn_scan_barcode = (Button)findViewById(R.id.button);
        pic_barcode = (EditText)findViewById(R.id.editText);

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
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
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
