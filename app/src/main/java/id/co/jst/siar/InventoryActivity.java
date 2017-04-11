package id.co.jst.siar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA;
import id.co.jst.siar.Models.sqlite.LocationModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

public class InventoryActivity extends AppCompatActivity {
    public Button btn_scan;
    private EditText raa_barcode;
    public TextView asset_number, asset_name, model, serial_number, location;
    private DBHandlerRAA sqliteRAA = new DBHandlerRAA(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        raa_barcode = (EditText)findViewById(R.id.editText2);
        asset_number = (TextView) findViewById(R.id.textView22);
        model = (TextView)findViewById(R.id.textView24);
        serial_number = (TextView)findViewById(R.id.textView25);
        location = (TextView)findViewById(R.id.textView26);
        btn_scan = (Button)findViewById(R.id.button8);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(InventoryActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode < 0) {
            String content = intent.getStringExtra("SCAN_RESULT");
            switch (requestCode) {
                case 0:
                    raa_barcode.setText(content);
                    Object[] result = sqliteRAA.getRAA(Integer.parseInt(content.substring(4)));
//                    Log.d("Insert: ", "Inserting .."+Integer.parseInt(content.substring(4)));
                    asset_number.setText(content);
                    model.setText(((RAAModel)result[0]).getIRModel());
                    serial_number.setText(((RAAModel)result[0]).getIRMFGNo());
                    location.setText(((LocationModel)result[1]).getPl_place());
                    break;
            }
        } else {
            Toast.makeText(InventoryActivity.this, "Cancel scan by user", Toast.LENGTH_LONG).show();
        }

    }
}
