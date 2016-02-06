package amsa.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import amsa.projekt.Utils.InputFilterMinMax;

public class NewStation extends AppCompatActivity {

    private Button buttonAdd, buttonEdit, buttonDelete, buttonGetPosition, buttonGetAddress, buttonNS, buttonEW;
    private EditText editTextStationName,editTextCity,editTextAddress,editTextLat,editTextLon;
    private GPSLocation gpsLocation;
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

        buttonGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
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

    private void getAddress(){
        if(editTextLon.getText().toString().equals("") || editTextLat.getText().toString().equals("")){
            try {
                throw new Exception("Współrzędne geograficzne nie mogą być puste");
            } catch (Exception e){
                alertDialog(e);
            }
            return;
        }
        double longitude = Double.parseDouble(editTextLon.getText().toString()) * (buttonEW.getText().toString().equals("E") ? 1 : -1);
        double latitude = Double.parseDouble(editTextLat.getText().toString()) * (buttonNS.getText().toString().equals("N") ? 1 : -1);
        gpsLocation.setLon(longitude);
        gpsLocation.setLat(latitude) ;
        gpsLocation.setNs(buttonNS.getText().toString());
        gpsLocation.setEw(buttonEW.getText().toString());
        gpsLocation = MainActivity.gpsAdapter.getGPSLocation(latitude,longitude);

        editTextCity.setText(gpsLocation.getCity());
        editTextAddress.setText(gpsLocation.getAddress());


//        gpsLocation.set

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
        }
        return null;    //nieosiągalny stan, ale bez tego się nie kompiluje
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
