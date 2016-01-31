package amsa.projekt;

import android.app.Application;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class GPSAdapter {
    private Context context;
    private LocationManager locManager;
    private LocationListener locListener;
    private Location currentLocation;

    public GPSAdapter(Context context) {
        this.context = context;
    }

    public void getCurrentLocation() {

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
                // TODO Auto-generated method stub
                currentLocation = location;
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
        //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }
}
