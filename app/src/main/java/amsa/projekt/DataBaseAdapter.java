package amsa.projekt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import amsa.projekt.DataBase.Fuel;
import amsa.projekt.DataBase.GasStation;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class DataBaseAdapter {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    public DataBaseAdapter(Context context) {
        this.context = context;
    }


    public DataBaseAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close(){
        try{
            dbHelper.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(GasStation.CREATE_GASSTATION_TABLE);
            db.execSQL(Fuel.CREATE_FUEL_TABLE);
            Log.d("DataBaseAdapter", "Tworzenie bazy");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Fuel.DROP_FUEL_TABLE);
            db.execSQL(GasStation.DROP_GASSTATION_TABLE);
            Log.d("DataBaseAdapter", "Aktualizowanie bazy");
            onCreate(db);
        }
    }
}