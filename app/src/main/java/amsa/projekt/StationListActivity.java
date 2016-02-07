package amsa.projekt;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import amsa.projekt.DataBase.*;
import amsa.projekt.DataBase.Fuel;

public class StationListActivity extends AppCompatActivity {

    private DataBaseAdapter db;
    private ArrayList<GasStation> stations;
    private GasStation selectedStation;
    private ListView listView;
    private Location lastKnownLocation;
    private Boolean sortByDistance = false;
    private Button stationInfo, fuels;
    private TextView stationID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent i = getIntent();
        if(i.hasExtra("lon") && i.hasExtra("lat")){
            sortByDistance = true;
            lastKnownLocation = new Location("");
            lastKnownLocation.setLatitude(i.getDoubleExtra("lat", 0));
            lastKnownLocation.setLongitude(i.getDoubleExtra("lon", 0));
        }
        listView    = (ListView)findViewById(R.id.listView);
        stationInfo = (Button)findViewById(R.id.buttonStation);
        fuels       = (Button)findViewById(R.id.buttonFuel);
        stationID   = (TextView)findViewById(R.id.textViewSelectedStation);

        stationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStation!=null) {
                    Intent i = new Intent(StationListActivity.this, StationActivity.class);
                    i.putExtra("id", selectedStation.getId());
                    startActivity(i);
                }
            }
        });

        fuels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStation!=null) {
                    Intent i = new Intent(StationListActivity.this, FuelListActivity.class);
                    i.putExtra("id", selectedStation.getId());
                    startActivity(i);
                }
            }
        });

        db = new DataBaseAdapter(this);
        db.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addStationsToList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void addStationsToList(){
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        listView = (ListView) findViewById(R.id.listView);
        adapter.clear();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stations.get(position);
                stationID.setText("Id: " + selectedStation.getId() + " " + selectedStation.getName());
            }
        });
        getStationsList();
        if(sortByDistance){
            Log.d("Sort","sortuje dane");
            Collections.sort(stations, new Comparator<GasStation>() {
                @Override
                public int compare(GasStation lhs, GasStation rhs) {
                    return lhs.compareTo(rhs);
                    //return lhs.getDistanceTo().compareTo(rhs.getDistanceTo());
                }
            });
        }
        for(GasStation gs: stations){
            if(sortByDistance){
                adapter.add(gs.getId() + ". " + gs.getName() + "\r\n" + gs.getCity() + "\r\n" + gs.getAddress() + "\r\n" +
                        (gs.getDistanceTo() > 2000 ? new BigDecimal(gs.getDistanceTo()).divide(new BigDecimal(1000),3,BigDecimal.ROUND_CEILING)  + "km": gs.getDistanceTo().longValue() + "m"));
            }
            else {
                adapter.add(gs.getId() + ". " + gs.getName() + "\r\n" + gs.getCity() + "\r\n" + gs.getAddress());
            }
        }
    }

    private void getStationsList(){
        Cursor cursor = db.select(amsa.projekt.DataBase.GasStation.DB_GASSTATION_TABLE, amsa.projekt.DataBase.GasStation.GASSTATION_COLUMNS);
        stations = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
            while(cursor.isAfterLast()==false){
                GasStation gs = new GasStation();
                gs.setId(cursor.getLong(amsa.projekt.DataBase.GasStation.ID_COLUMN));
                gs.setAddress(cursor.getString(amsa.projekt.DataBase.GasStation.ADDRESS_COLUMN));
                gs.setCity(cursor.getString(amsa.projekt.DataBase.GasStation.CITY_COLUMN));
                gs.setName(cursor.getString(amsa.projekt.DataBase.GasStation.NAME_COLUMN));
                gs.setLatitude(cursor.getDouble(amsa.projekt.DataBase.GasStation.LATITUDE_COLUMN));
                gs.setLongitude(cursor.getDouble(amsa.projekt.DataBase.GasStation.LONGITUDE_COLUMN));
                if(sortByDistance) {
                    Location stationLocation = new Location("");
                    stationLocation.setLongitude(gs.getLongitude());
                    stationLocation.setLatitude(gs.getLatitude());
                    gs.setDistanceTo(GPSAdapter.distanceBetweenPoints(lastKnownLocation, stationLocation));
                }
                stations.add(gs);
                cursor.moveToNext();
            }
        }
    }
}
