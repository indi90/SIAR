package id.co.jst.siar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAA;
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations;
import id.co.jst.siar.Helpers.sql.DBHandlerTBMFAPlace;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA;
import id.co.jst.siar.Models.sql.TINV_RAAModel;
import id.co.jst.siar.Models.sqlite.LocationModel;
import id.co.jst.siar.Models.sql.TBMFAPlaceModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

public class MenuActivity extends AppCompatActivity {
    private ImageButton btn_down_location, btn_down_raa, btn_inventory;
    private DBHandlerLocations sqliteLocation = new DBHandlerLocations(this);
    private DBHandlerRAA sqliteRAA = new DBHandlerRAA(this);
    private DBHandlerTBMFAPlace sqlPlace = new DBHandlerTBMFAPlace();
    private DBHandlerTINV_RAA sqlRAA = new DBHandlerTINV_RAA();
    private Date dNow = new Date( );
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_down_location = (ImageButton)findViewById(R.id.imageButton2);
        btn_down_raa = (ImageButton)findViewById(R.id.imageButton1);
        btn_inventory = (ImageButton)findViewById(R.id.imageButton4);

        final ProgressDialog pdg = new ProgressDialog(MenuActivity.this);
        pdg.setIndeterminate(true);
        pdg.setInverseBackgroundForced(false);
        pdg.setCanceledOnTouchOutside(true);
        pdg.setMessage("Loading data");
        pdg.setCancelable(false);

        btn_down_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdg.show();
//                Log.d("Insert: ", "Inserting ..");
//                db.addLocation(new LocationModel(1, "Dockers", "475 Brannan St", "#330, San Francisco", "CA 94107", ft.format(dNow)));
//                db.addLocation(new LocationModel(12, "Dockers 2", "475 Brannan St 2", "#330, San Francisco 2", "CA 94107 2", ft.format(dNow)));
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        sqliteLocation.truncateLocation();
                        if (sqliteLocation.getLocationCount() == 0) {
                            List<TBMFAPlaceModel> places = sqlPlace.getAllPlaces(MenuActivity.this);
                            for (TBMFAPlaceModel place : places){
                                sqliteLocation.addLocation(new LocationModel(place.getPl_code(), place.getPl_building(), place.getPl_floor(), place.getPl_place(), place.getPl_description(),ft.format(dNow)));
                            }
                            if (sqliteLocation.getLocationCount() == sqlPlace.getPlacesCount(MenuActivity.this)){
                                // To dismiss the dialog
                                pdg.dismiss();
                                Toast.makeText(MenuActivity.this, "Berhasil Download Lokasi", Toast.LENGTH_LONG).show();
                            }
                        }
                        return null;
                    }
                }.execute();
            }
        });

        btn_down_raa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdg.show();
//                Log.d("Insert: ", "Inserting ..");
//                db.addLocation(new LocationModel(1, "Dockers", "475 Brannan St", "#330, San Francisco", "CA 94107", ft.format(dNow)));
//                db.addLocation(new LocationModel(12, "Dockers 2", "475 Brannan St 2", "#330, San Francisco 2", "CA 94107 2", ft.format(dNow)));
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        sqliteRAA.truncateRAA();
                        if (sqliteRAA.getRAACount() == 0) {
                            List<TINV_RAAModel> RAAs = sqlRAA.getAllRAA(MenuActivity.this);
                            for (TINV_RAAModel RAA : RAAs){
                                sqliteRAA.addRAA(new RAAModel(RAA.getIRPeriodID(), RAA.getIRAssetNo(), RAA.getIRModel(), RAA.getIRMFGNo(), RAA.getIRLocationID(), RAA.getIRGenerateDate(), RAA.getIRGenerateUser(), RAA.getIRDeptCode()));
                            }

                            if (sqliteRAA.getRAACount() == sqlRAA.getRAACount(MenuActivity.this)){
                                pdg.dismiss();
                                // To dismiss the dialog
                                Toast.makeText(MenuActivity.this, "Berhasil Download RAA", Toast.LENGTH_LONG).show();
                            }
                        }
                        return null;
                    }
                }.execute();
            }
        });

        btn_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScanLocationActivity.class);
                startActivity(i);
            }
        });
    }
}
