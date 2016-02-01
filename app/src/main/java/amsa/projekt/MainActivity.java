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
    GPSAdapter gpsAdapter;
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
                startActivity(i);
            }
        });

        allStations = (Button)findViewById(R.id.buttonAllStations);
        allStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        closestStation = (Button)findViewById(R.id.buttonClosest);
        closestStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastKnownLocation();
            }
        });

        stationsMap = (Button)findViewById(R.id.buttonMap);
        stationsMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setText(String t){
        tv.setText(t);
    }

    public Location getLastKnownLocation() {
        Location lastKnownLocation_byGps = gpsAdapter.getLastKnownLocation();

        if (lastKnownLocation_byGps == null) {
            setText("GPS nie dostępny");
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("GPS wyłączony, nie można ustalić lokalizacji");
            dlgAlert.setTitle("Usługa GPS");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return null;
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

    private Intent intentFactory(Class clz){
        return new Intent(MainActivity.this,clz);
    }
}
