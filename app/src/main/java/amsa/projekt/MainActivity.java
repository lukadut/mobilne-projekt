package amsa.projekt;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import amsa.projekt.Utils.ActivityUtils;

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
                tv.setText(gpsAdapter.getLocationInfo());
            }
        });

        addNewStation = (Button)findViewById(R.id.buttonNewStation);
        addNewStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = intentFactory(StationActivity.class);
//                i.putExtra("id",0);
                startActivity(i);
            }
        });

        allStations = (Button)findViewById(R.id.buttonAllStations);
        allStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = intentFactory(StationListActivity.class);
                startActivity(i);
            }
        });

        closestStation = (Button)findViewById(R.id.buttonClosest);
        closestStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location location = gpsAdapter.getLastKnownLocation();
                    if(location == null){
                        throw new Exception("GPS wyłączony, nie można ustalić lokalizacji");
                    }
                    Intent i = intentFactory(StationListActivity.class);
                    i.putExtra("lat", location.getLatitude());
                    i.putExtra("lon", location.getLongitude());
                    startActivity(i);
                } catch (Exception e){
                    ActivityUtils.alertDialog(getApplicationContext(),e);
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

    @Override
    protected void onResume() {
        super.onResume();
        tv.setText(gpsAdapter.getLocationInfo());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private Intent intentFactory(Class clz){
        return new Intent(MainActivity.this,clz);
    }
}
