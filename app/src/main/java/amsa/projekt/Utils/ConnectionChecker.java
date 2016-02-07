package amsa.projekt.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChecker {
    private static Context _context;
    public static boolean check(Context context){
        _context = context;
        return (isConnectedMobile()||isConnectedWifi());
    }


    private static boolean isConnectedWifi(){
        ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        } catch (Exception e){
            return false;   //urządzenie bez WIFI
        }
    }

    private static boolean isConnectedMobile(){
        ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED);
        } catch (Exception e){
            return false;   //urządzenie bez GSM
        }
    }
}
