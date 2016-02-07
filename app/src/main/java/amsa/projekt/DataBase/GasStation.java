package amsa.projekt.DataBase;

import android.content.ContentValues;

import amsa.projekt.GPSLocation;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class GasStation {
    public static final String DB_GASSTATION_TABLE = "gasstation";

    public static final String ID = "id";
    public static final int ID_COLUMN = 0;
    public static final String NAME = "name";
    public static final int NAME_COLUMN = 1;
    public static final String CITY = "city";
    public static final int CITY_COLUMN = 2;
    public static final String ADDRESS = "address";
    public static final int ADDRESS_COLUMN = 3;
    public static final String LATITUDE = "latitude";
    public static final int LATITUDE_COLUMN = 4;
    public static final String LONGITUDE = "longitude";
    public static final int LONGITUDE_COLUMN = 5;

    public static final String DROP_GASSTATION_TABLE =
            "DROP TABLE IF EXISTS " + DB_GASSTATION_TABLE;

    final static public String CREATE_GASSTATION_TABLE = "CREATE TABLE ["+DB_GASSTATION_TABLE+ "] (\n" +
            "["+ID+"] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "["+NAME+"] VARCHAR(25)  NOT NULL,\n" +
            "["+CITY+"] vARCHAR(25)  NULL,\n" +
            "["+ADDRESS+"] vaRCHAR(60)  NULL,\n" +
            "["+LATITUDE+"] FLOAT  NOT NULL,\n" +
            "["+LONGITUDE+"] fLOAT  NOT NULL\n" +
            ")";

    static public ContentValues createContext(String name, GPSLocation gpsLocation){
        ContentValues newValues = new ContentValues();
        newValues.put(NAME,name);
        newValues.put(CITY,gpsLocation.getCity());
        newValues.put(ADDRESS,gpsLocation.getAddress());
        newValues.put(LATITUDE, gpsLocation.getLat());
        newValues.put(LONGITUDE, gpsLocation.getLon());
        return newValues;
    }

    static public String[] GASSTATION_COLUMNS = new String[]{ID,NAME,CITY,ADDRESS,LATITUDE,LONGITUDE};

}
