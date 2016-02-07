package amsa.projekt;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import amsa.projekt.DataBase.*;
import amsa.projekt.DataBase.GasStation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataBaseAdapter db;
    private List<MarkerOptions> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DataBaseAdapter(getApplicationContext());
        stations=new ArrayList<MarkerOptions>();
        db.open();
        Cursor cursor = db.select(GasStation.DB_GASSTATION_TABLE,new String[]{GasStation.NAME,GasStation.LATITUDE,GasStation.LONGITUDE});
        Log.d("MAPS","CURSOR " + cursor.getCount());
        db.close();
        if(cursor !=null && cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Log.d("MAP","NAME : " + cursor.getString(0));
                Log.d("MAP","LATITUDE : " + cursor.getDouble(1));
                Log.d("MAP","LONGITUDE : " + cursor.getDouble(2));
                stations.add(new MarkerOptions().position(new LatLng(cursor.getDouble(1),cursor.getDouble(2))).title(cursor.getString(0)));
                cursor.moveToNext();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(MarkerOptions markerOptions:stations){
            mMap.addMarker(markerOptions);
        }



        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

    }
}
