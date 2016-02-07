package amsa.projekt;

import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amsa.projekt.DataBase.*;
import amsa.projekt.Utils.ActivityUtils;

public class FuelListActivity extends AppCompatActivity {

    private DataBaseAdapter db;
    private EditText fuelName, fuelPrice;
    private List<Fuel> fuels;
    private Fuel selectedFuel;
    private ListView listView;
    private Long stationID;
    private Button addFuel, deleteFuel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_list);

        if(!getIntent().hasExtra("id")){
            finish();
        }
        stationID   = getIntent().getLongExtra("id",-1);
        listView    = (ListView)findViewById(R.id.listView);
        addFuel     = (Button)findViewById(R.id.buttonFuelAdd);
        deleteFuel  = (Button)findViewById(R.id.buttonFuelDelete);
        fuelName    = (EditText)findViewById(R.id.editTextFuelName);
        fuelPrice   = (EditText)findViewById(R.id.editTextFuelPrice);

        addFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fuelName.getText().toString().equals("") || fuelPrice.getText().toString().equals(""))
                    return;
                selectedFuel = new Fuel();
                selectedFuel.setTime((new Date()).getTime());
                selectedFuel.setPrice(Double.parseDouble(fuelPrice.getText().toString()));
                selectedFuel.setName(fuelName.getText().toString());
                selectedFuel.setActive(1);
                selectedFuel.setStation(stationID);
                Log.d("fuel list" , "date " + selectedFuel.getTime());
                try{
                    long insertedID = db.insert(amsa.projekt.DataBase.Fuel.DB_FUEL_TABLE, amsa.projekt.DataBase.Fuel.createContext(selectedFuel));
                    Toast.makeText(getApplicationContext(), "Dodano nową cenę paliwa " + insertedID, Toast.LENGTH_LONG).show();
                    addFuelsToList();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        deleteFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.confirmDialog(FuelListActivity.this, "Usuń", "Na pewno chcesz usunąć?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.query(amsa.projekt.DataBase.Fuel.disactive(selectedFuel.getName()));
                        fuelPrice.setText("");
                        fuelName.setText("");
                        addFuelsToList();
                    }
                });

            }
        });
        db = new DataBaseAdapter(this);
        db.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addFuelsToList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void addFuelsToList(){
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        listView = (ListView) findViewById(R.id.listView);
        adapter.clear();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFuel = fuels.get(position);
                fuelName.setText(selectedFuel.getName());
                fuelPrice.setText(selectedFuel.getPrice()+"");

            }
        });
        getFuelsList(stationID);

        for(Fuel f: fuels){
            adapter.add(f.getName() + "\r\nCena: " + String.format("%.2f", f.getPrice()) + "\r\naktualizowane " + ActivityUtils.milisToDate(f.getTime()));
        }
    }

    private void getFuelsList(long stationID){
        Cursor cursor = db.rawQuery(amsa.projekt.DataBase.Fuel.getLatestPrices(stationID));
        fuels = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
            while(cursor.isAfterLast()==false){
                Fuel f = new Fuel();
                f.setId(cursor.getLong(amsa.projekt.DataBase.Fuel.ID_COLUMN));
                f.setName(cursor.getString(amsa.projekt.DataBase.Fuel.FUEL_COLUMN));
                f.setActive(cursor.getInt(amsa.projekt.DataBase.Fuel.ACTIVE_COLUMN));
                f.setPrice(cursor.getDouble(amsa.projekt.DataBase.Fuel.PRICE_COLUMN));
                f.setStation(cursor.getLong(amsa.projekt.DataBase.Fuel.GASSTATION_COLUMN));
                f.setTime(cursor.getLong(amsa.projekt.DataBase.Fuel.DATE_COLUMN));
                Log.d("fuellist", "cena " + f.getPrice() + " data " + (new SimpleDateFormat("HH:mm:ss").format(new Date(f.getTime()))));
                if(f.getActive() != 0) {
                    fuels.add(f);
                }
                cursor.moveToNext();
            }
        }
    }
}
