package amsa.projekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amsa.projekt.Utils.ActivityUtils;
import amsa.projekt.Utils.InputFilterMinMax;
import amsa.projekt.DataBase.GasStation;

public class StationActivity extends AppCompatActivity {

    private Button buttonAdd, buttonEdit, buttonDelete, buttonGetPosition, buttonGetAddress, buttonNS, buttonEW;
    private EditText editTextStationName,editTextCity,editTextAddress,editTextLat,editTextLon;
    private GPSLocation gpsLocation;
    private amsa.projekt.GasStation gasStation;
    private DataBaseAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_station);
        buttonAdd           = (Button)findViewById(R.id.buttonAdd);
        buttonEdit          = (Button)findViewById(R.id.buttonEdit);
        buttonDelete        = (Button)findViewById(R.id.buttonDelete);
        buttonGetPosition   = (Button)findViewById(R.id.buttonGetPosition);
        buttonGetAddress    = (Button)findViewById(R.id.buttonGetAddress);
        buttonNS            = (Button)findViewById(R.id.buttonNS);
        buttonEW            = (Button)findViewById(R.id.buttonEW);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setLocationCoordinates();
                    setLocationAddress();
                    db = new DataBaseAdapter(getApplicationContext());
                    db.open();
                    long insertID = db.insert(GasStation.DB_GASSTATION_TABLE,GasStation.createContext(editTextStationName.getText().toString(),gpsLocation));
                    Toast.makeText(getApplicationContext(), "Dodano nową stację " + insertID, Toast.LENGTH_LONG).show();
                    db.close();
                    finish();
                    Intent i = new Intent(StationActivity.this,StationActivity.class);
                    i.putExtra("id",insertID);
                    startActivity(i);
                } catch (Exception e){
                    ActivityUtils.alertDialog(getApplicationContext(),e);
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setLocationCoordinates();
                    setLocationAddress();
                    db = new DataBaseAdapter(getApplicationContext());
                    db.open();
                    if(db.update(GasStation.DB_GASSTATION_TABLE, GasStation.createContext(editTextStationName.getText().toString(), gpsLocation),"id = " + gasStation.getId())) {
                        Toast.makeText(getApplicationContext(), "Zedytowano wpis", Toast.LENGTH_LONG).show();
                    }
                    db.close();
                } catch (Exception e){
                    ActivityUtils.alertDialog(getApplicationContext(),e);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.confirmDialog(StationActivity.this, "Usuń", "Na pewno chcesz usunąć?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = new DataBaseAdapter(getApplicationContext());
                        db.open();
                        if (db.delete(GasStation.DB_GASSTATION_TABLE,"id = " + gasStation.getId())){
                            Toast.makeText(getApplicationContext(), "Usunięto stację", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        db.close();
                    }
                });
            }
        });

        buttonGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGPSAddress();
            }
        });
        buttonGetPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gpsLocation = getGPSLocation(MainActivity.gpsAdapter.getGPSLocation());
                } catch (Exception e) {
                    ActivityUtils.alertDialog(getApplicationContext(), e);
                }
            }
        });
        buttonNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNS.setText(buttonNS.getText().equals("N") ? "S" : "N");
            }
        });
        buttonEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEW.setText(buttonEW.getText().equals("E")?"W":"E");
            }
        });

        editTextStationName = (EditText)findViewById(R.id.editTextStationName);
        editTextCity        = (EditText)findViewById(R.id.editTextCity);
        editTextAddress     = (EditText)findViewById(R.id.editTextAddress);
        editTextLat         = (EditText)findViewById(R.id.editTextLat);
        editTextLon         = (EditText)findViewById(R.id.editTextLon );

        editTextLat.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        editTextLon.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "180")});
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsLocation = new GPSLocation();
        if(getIntent().hasExtra("id")){
            buttonAdd.setVisibility(View.GONE);
            buttonEdit.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
            db = new DataBaseAdapter(getApplicationContext());
            db.open();
            try{
                gasStation= getGasStation(getIntent().getLongExtra("id", -1));
                getGPSLocation(gasStation.getGPSLocation());
                editTextStationName.setText(gasStation.getName());

            } catch (Exception e){
                ActivityUtils.alertDialog(this,e);
            } finally {
                db.close();
            }
        }
        else{
            buttonAdd.setVisibility(View.VISIBLE);
            buttonEdit.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }
    }

    private void getGPSAddress(){
        try {
            setLocationCoordinates();
            if(gpsLocation.getCity() == null){
                throw new Exception("Nie udało się odnaleźć lokalizacji");
            }
            editTextCity.setText(gpsLocation.getCity());
            editTextAddress.setText(gpsLocation.getAddress());
        } catch (Exception e){
            ActivityUtils.alertDialog(StationActivity.this,e);
        }
    }

    private void setLocationAddress(){
        gpsLocation.setCity(editTextCity.getText().toString());
        gpsLocation.setAddress(editTextAddress.getText().toString());
    }

    private void setLocationCoordinates() throws Exception{
        if(editTextLon.getText().toString().equals("") || editTextLat.getText().toString().equals("")){
            throw new Exception("Współrzędne geograficzne nie mogą być puste");
        }
        double longitude = Double.parseDouble(editTextLon.getText().toString()) * (buttonEW.getText().toString().equals("E") ? 1 : -1);
        double latitude = Double.parseDouble(editTextLat.getText().toString()) * (buttonNS.getText().toString().equals("N") ? 1 : -1);
        gpsLocation.setLon(longitude);
        gpsLocation.setLat(latitude) ;
        gpsLocation.setNs(buttonNS.getText().toString());
        gpsLocation.setEw(buttonEW.getText().toString());
        gpsLocation = MainActivity.gpsAdapter.getGPSLocation(latitude,longitude);
    }


    private GPSLocation getGPSLocation(GPSLocation location){
        try {
            editTextCity.setText(location.getCity());
            editTextAddress.setText(location.getAddress());
            editTextLat.setText(location.getUnsignedLatitude());
            editTextLon.setText(location.getUnsignedLongitude());
            buttonNS.setText(location.getNs());
            buttonEW.setText(location.getEw());
            return location;
        } catch (Exception e) {
            ActivityUtils.alertDialog(getApplicationContext(),e);
            return new GPSLocation();
        }
    }

    private amsa.projekt.GasStation getGasStation(long id) throws Exception{
        if(id == -1) {
            finish();
            throw new Exception("Błędne id wpisu");
        }
        Cursor c = db.select(GasStation.DB_GASSTATION_TABLE,GasStation.GASSTATION_COLUMNS,"id = " + id);
        amsa.projekt.GasStation gs = new amsa.projekt.GasStation();
        if(c.moveToFirst()){
            gs.setId(id);
            gs.setName(c.getString(GasStation.NAME_COLUMN));
            gs.setCity(c.getString(GasStation.CITY_COLUMN));
            gs.setAddress(c.getString(GasStation.ADDRESS_COLUMN));
            gs.setLongitude(c.getDouble(GasStation.LONGITUDE_COLUMN));
            gs.setLatitude(c.getDouble(GasStation.LATITUDE_COLUMN));
        }
        return gs;
    }
}
