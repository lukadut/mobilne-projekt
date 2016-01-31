package amsa.projekt.DataBase;

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


    public static final String DROP_FUEL_TABLE =
            "DROP TABLE IF EXISTS " + DB_FUEL_TABLE;

    final static public String CREATE_FUEL_TABLE = "CREATE TABLE ["+DB_FUEL_TABLE+ "] (\n" +
            "["+ID+"] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "["+GASSTATION+"] inTEGER  NOT NULL,\n" +
            "["+FUEL+"] vARCHAR(10)  NOT NULL,\n" +
            "["+PRICE+"] flOAT  NOT NULL,\n" +
            "["+DATE+"] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL\n" +
            ")" ;
}
