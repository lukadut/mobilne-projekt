package amsa.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import amsa.projekt.Utils.InputFilterMinMax;
import amsa.projekt.DataBase.GasStation;

public class NewStation extends AppCompatActivity {

    private Button buttonAdd, buttonEdit, buttonDelete, buttonGetPosition, buttonGetAddress, buttonNS, buttonEW;
    private EditText editTextStationName,editTextCity,editTextAddress,editTextLat,editTextLon;
    private GPSLocation gpsLocation;
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
                    gpsLocation.debug();
                    db = new DataBaseAdapter(getApplicationContext());
                    db.open();
                    long insertID = db.insert(GasStation.DB_GASSTATION_TABLE,GasStation.insert(editTextStationName.getText().toString(),gpsLocation));
                    Toast.makeText(getApplicationContext(), "Dodano nową stację " + insertID, Toast.LENGTH_LONG).show();
                    db.close();
                } catch (Exception e){
                    alertDialog(e);
                }
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
                gpsLocation = getGPSLocation();
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
            alertDialog(e);
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
        Log.d("GPSLOCATION", "is null + " + (gpsLocation==null));
        gpsLocation.setLon(longitude);
        gpsLocation.setLat(latitude) ;
        gpsLocation.setNs(buttonNS.getText().toString());
        gpsLocation.setEw(buttonEW.getText().toString());
        gpsLocation = MainActivity.gpsAdapter.getGPSLocation(latitude,longitude);
    }


    private GPSLocation getGPSLocation(){
        try {
            GPSLocation location =  MainActivity.gpsAdapter.getGPSLocation();
            editTextCity.setText(location.getCity());
            editTextAddress.setText(location.getAddress());
            editTextLat.setText(location.getUnsignedLatitude());
            editTextLon.setText(location.getUnsignedLongitude());
            buttonNS.setText(location.getNs());
            buttonEW.setText(location.getEw());
            return location;
        } catch (Exception e) {
            alertDialog(e);
            return new GPSLocation();
        }
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
}
