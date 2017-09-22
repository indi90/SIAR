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
import java.util.HashMap;
import java.util.List;

import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAA;
import id.co.jst.siar.Helpers.sql.DBHandlerTINV_RAAActual;
import id.co.jst.siar.Helpers.sqlite.DBHandlerLocations;
import id.co.jst.siar.Helpers.sql.DBHandlerTBMFAPlace;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAA;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAActual;
import id.co.jst.siar.Helpers.sqlite.DBHandlerRAAPeriod;
import id.co.jst.siar.Models.sql.TINV_RAAActualModel;
import id.co.jst.siar.Models.sql.TINV_RAAModel;
import id.co.jst.siar.Models.sqlite.LocationModel;
import id.co.jst.siar.Models.sql.TBMFAPlaceModel;
import id.co.jst.siar.Models.sqlite.RAAActualModel;
import id.co.jst.siar.Models.sqlite.RAAModel;

public class MenuActivity extends AppCompatActivity {
    public ImageButton btn_down_location, btn_down_raa, btn_inventory, btn_upload;
    private DBHandlerLocations sqliteLocation = new DBHandlerLocations(this);
    private DBHandlerRAA sqliteRAA = new DBHandlerRAA(this);
    private DBHandlerRAAActual sqliteRAAActual = new DBHandlerRAAActual(this);
    private DBHandlerRAAPeriod sqliteRAAPeriod = new DBHandlerRAAPeriod(this);
    private DBHandlerTBMFAPlace sqlPlace = new DBHandlerTBMFAPlace();
    private DBHandlerTINV_RAA sqlRAA = new DBHandlerTINV_RAA();
    private DBHandlerTINV_RAAActual sqlRAAActual = new DBHandlerTINV_RAAActual();
    private Date dNow = new Date( );
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
    private List<TBMFAPlaceModel> places;
    private List<TINV_RAAModel> RAAs;
    private String [] resultRAAActual;


    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        session = new SessionManager(this);

        // get user data from session
        final HashMap<String, String> user = session.getUserDetails();

        btn_down_location = (ImageButton)findViewById(R.id.imageButton2);
        btn_down_raa = (ImageButton)findViewById(R.id.imageButton1);
        btn_inventory = (ImageButton)findViewById(R.id.imageButton4);
        btn_upload = (ImageButton)findViewById(R.id.imageButton3);

        final ProgressDialog pdg = new ProgressDialog(MenuActivity.this);
        pdg.setIndeterminate(true);
        pdg.setInverseBackgroundForced(false);
        pdg.setCanceledOnTouchOutside(true);
        pdg.setMessage("Please Wait ...");
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
                protected void onPreExecute() {
                    places = sqlPlace.getAllPlaces(MenuActivity.this);
                    if (places.isEmpty()){
                        pdg.dismiss();
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    if (!places.isEmpty()){
                        sqliteLocation.truncateLocation();
                        if (sqliteLocation.getLocationCount() == 0) {
                            for (TBMFAPlaceModel place : places){
                                sqliteLocation.addLocation(new LocationModel(place.getPl_code(), place.getPl_building(), place.getPl_floor(), place.getPl_place(), place.getPl_description(),ft.format(dNow)));
                            }
                            if (sqliteLocation.getLocationCount() == sqlPlace.getPlacesCount(MenuActivity.this)){
                                pdg.dismiss();
                                // To dismiss the dialog
                                runOnUiThread(new Runnable(){

                                    @Override
                                    public void run(){
                                        //update ui here
                                        Toast.makeText(MenuActivity.this, "Berhasil Mengambil Lokasi", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
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
                protected void onPreExecute() {
                    RAAs = sqlRAA.getAllRAA(user.get(SessionManager.KEY_DEPARTMENT), user.get(SessionManager.KEY_SECTION), sqliteRAAPeriod.checkPeriod(),MenuActivity.this);
                    if (RAAs.isEmpty()){
                        pdg.dismiss();
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    if (!RAAs.isEmpty()){
                        sqliteRAA.truncateRAA();
                        if (sqliteRAA.getRAACount() == 0) {
                            for (TINV_RAAModel RAA : RAAs){
//                            Log.d("Insert: ", "Inserting .."+RAA.getIRAssetNo()+" "+RAA.getIRLocationID());
                                sqliteRAA.addRAA(new RAAModel(RAA.getIRPeriodID(), RAA.getIRAssetNo(), RAA.getIRModel(), RAA.getIRMFGNo(), RAA.getIRLocationID(), RAA.getIRGenerateDate(), RAA.getIRGenerateUser(), RAA.getIRDeptCode()));
                            }
                        }

                        if (sqliteRAA.getRAACount() == sqlRAA.getRAACount(user.get(SessionManager.KEY_DEPARTMENT), user.get(SessionManager.KEY_SECTION), sqliteRAAPeriod.checkPeriod(),MenuActivity.this)){
                            pdg.dismiss();
                            // To dismiss the dialog
                            runOnUiThread(new Runnable(){

                                @Override
                                public void run(){
                                    //update ui here
                                    Toast.makeText(MenuActivity.this, "Berhasil Mengambil RAA", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable(){

                            @Override
                            public void run(){
                                //update ui here
                                Toast.makeText(MenuActivity.this, "Data Inventory Belum diGenerate", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    return null;
                }
            }.execute();
            }
        });

        btn_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (sqliteRAA.getRAACount() != 0 && sqliteLocation.getLocationCount() != 0){
                Intent i = new Intent(getApplicationContext(), ScanLocationActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(MenuActivity.this, "Ambil RAA dan Lokasi Terlebih Dahulu.", Toast.LENGTH_LONG).show();
            }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    pdg.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    if (sqliteRAAActual.getRAACount() > 0) {
                        boolean success = true;
                        List<RAAActualModel> RAAActuals = sqliteRAAActual.getAllRAAActual();
                        for (RAAActualModel RAAACtual : RAAActuals){
                            resultRAAActual = sqlRAAActual.addRAAActual(new TINV_RAAActualModel(RAAACtual.getIRPeriodID(), RAAACtual.getIRAssetNo(), RAAACtual.getIRModel(), RAAACtual.getIRMFGNo(), RAAACtual.getIRLocationID(), RAAACtual.getIRGenerateDate(), RAAACtual.getIRGenerateUser(), RAAACtual.getIRDeptCode()), MenuActivity.this);
//                            Log.d("Result ", resultRAAActual[0]);
                            if (!resultRAAActual[0].equals("")){
                                pdg.dismiss();
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run(){
                                        //update ui here
                                        Toast.makeText(MenuActivity.this, resultRAAActual[0], Toast.LENGTH_LONG).show();
                                    }
                                });
//                                Log.d("Result ", resultRAAActual[0]);
                                success = false;
                                break;
                            }
                        }
//                        Log.d("Result ", String.valueOf(success));
                        if (success){
                            sqliteRAAActual.truncateRAA();
                            if (sqliteRAAActual.getRAACount() == 0){
                                pdg.dismiss();
                                // To dismiss the dialog
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run(){
                                        //update ui here
                                        Toast.makeText(MenuActivity.this, "Berhasil Menyimpan RAA Actual", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    } else {
                        pdg.dismiss();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                //update ui here
                                Toast.makeText(MenuActivity.this, "Data RAA Actual Belum Tersedia.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    return null;
                }
            }.execute();
            }
        });
    }
}
