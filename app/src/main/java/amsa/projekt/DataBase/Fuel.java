package amsa.projekt.DataBase;

import android.content.ContentValues;
import android.util.Log;

import amsa.projekt.GPSLocation;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class Fuel {
    public static final String DB_FUEL_TABLE = "fuel";

    public static final String ID = "id";
    public static final int ID_COLUMN = 0;
    public static final String GASSTATION = "gasstation";
    public static final int GASSTATION_COLUMN = 1;
    public static final String FUEL = "fuel";
    public static final int FUEL_COLUMN = 2;
    public static final String PRICE = "price";
    public static final int PRICE_COLUMN = 3;
    public static final String DATE = "date";
    public static final int DATE_COLUMN = 4;
    public static final String ACTIVE = "active";
    public static final int ACTIVE_COLUMN = 5;



    public static final String DROP_FUEL_TABLE =
            "DROP TABLE IF EXISTS " + DB_FUEL_TABLE;

    final static public String CREATE_FUEL_TABLE = "CREATE TABLE ["+DB_FUEL_TABLE+ "] (\n" +
            "["+ID+"] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "["+GASSTATION+"] inTEGER  NOT NULL,\n" +
            "["+FUEL+"] vARCHAR(10)  NOT NULL,\n" +
            "["+PRICE+"] flOAT  NOT NULL,\n" +
            "["+DATE+"] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
            "["+ACTIVE+"] INTEGER DEFAULT '1' NOT NULL" +
            ")" ;

    static public ContentValues createContext(amsa.projekt.Fuel fuel){
        ContentValues newValues = new ContentValues();
        newValues.put(GASSTATION,fuel.getStation());
        newValues.put(FUEL,fuel.getName());
        newValues.put(PRICE,fuel.getPrice());
        newValues.put(DATE, fuel.getTime());
        newValues.put(ACTIVE, fuel.getActive());
        return newValues;
    }

    static public String getLatestPrices(long stationId){
        String query = "select * from (select * from (select "+ID+","+ GASSTATION+","+FUEL+","+PRICE+","+DATE+ ","+ACTIVE+"\n" +
                "          from "+ DB_FUEL_TABLE +"\n" +
                "          where "+GASSTATION+" = " + stationId + "\n" +
                "          order by "+DATE+") as internal\n" +
                "group by "+FUEL+") as d\n" +
                "order by "+FUEL;
        return query;
    }
    static public String disactive(String fuelName){
        String query = "update " + DB_FUEL_TABLE  + " SET " + ACTIVE + " = 0 WHERE " + FUEL + " = \"" + fuelName+"\"";
        return query;
    }

    static  public  String[] FUEL_COLUMNS= new String[]{ID,GASSTATION,FUEL,PRICE,DATE,ACTIVE};
}
