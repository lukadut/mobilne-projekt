package amsa.projekt;

import android.app.Application;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class GPSAdapter {
    private Context context;
    private LocationManager locManager;
    private LocationListener locListener;
    //private Location currentLocation;
    private final String gpsLocationProvider = LocationManager.GPS_PROVIDER;

    public GPSAdapter(Context context) {
        this.context = context;

        locManager = (LocationManager) context.getSystemService(Application.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {

            }
        };
    }
    public String getLocationInfo(){
        Location location = getLastKnownLocation();
        if(location == null){
            return "GPS niedostępny";
        }
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        long timeInMillis = location.getTime();
        Date date = new Date(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Ostatnia aktualizacja ").append(sdf.format(date)).append("\r\n")
                .append(lat>=0? "N":"S").append(Math.abs(lat)).append(", ").append(lon>=0? "E":"W").append(Math.abs(lon));
        return sb.toString();

    }

    public void requestLocationUpdate() {
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*60*1000, 10, locListener);

    }

    @Nullable
    public Location getLastKnownLocation(){
        return locManager.getLastKnownLocation(gpsLocationProvider);
    }
}
