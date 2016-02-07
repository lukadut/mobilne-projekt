package amsa.projekt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    ImageButton gpsRefresh;
    static GPSAdapter gpsAdapter;
    Button addNewStation, closestStation, allStations, stationsMap;


    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView);

        gpsAdapter = new GPSAdapter(this);

        gpsRefresh = (ImageButton)findViewById(R.id.imageButton);
        gpsRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    gpsAdapter.requestLocationUpdate();
                setText(gpsAdapter.getLocationInfo());
            }
        });

        addNewStation = (Button)findViewById(R.id.buttonNewStation);
        addNewStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = intentFactory(NewStation.class);
//                i.putExtra("id",0);
                startActivity(i);
            }
        });

        allStations = (Button)findViewById(R.id.buttonAllStations);
        allStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = intentFactory(ListActivity.class);
                startActivity(i);
            }
        });

        closestStation = (Button)findViewById(R.id.buttonClosest);
        closestStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location location = getLastKnownLocation();
                    Intent i = intentFactory(ListActivity.class);
                    i.putExtra("lat", location.getLatitude());
                    i.putExtra("lon", location.getLongitude());
                    startActivity(i);
                } catch (Exception e){
                    alertDialog(e);
                }
            }
        });

        stationsMap = (Button)findViewById(R.id.buttonMap);
        stationsMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = intentFactory(MapsActivity.class);
                startActivity(i);
            }
        });
    }

    public void setText(String t){
        tv.setText(t);
    }

    public Location getLastKnownLocation() throws Exception{
        Location lastKnownLocation_byGps = gpsAdapter.getLastKnownLocation();

        if (lastKnownLocation_byGps == null) {
            throw new Exception("GPS wyłączony, nie można ustalić lokalizacji");
        }
        return lastKnownLocation_byGps;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText(gpsAdapter.getLocationInfo());
//        Intent i = new Intent(MainActivity.this, MapsActivity.class);
//        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void alertDialog(Exception e){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(e.getMessage());
        dlgAlert.setTitle("Błąd");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private Intent intentFactory(Class clz){
        return new Intent(MainActivity.this,clz);
    }
}
