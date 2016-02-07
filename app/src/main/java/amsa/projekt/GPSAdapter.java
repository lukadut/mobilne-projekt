package amsa.projekt;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amsa.projekt.Utils.ConnectionChecker;

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

    public GPSLocation getGPSLocation(double latitude, double longitude){
        GPSLocation GPSL = new GPSLocation();
        Geocoder geocoder = new Geocoder(context);

        GPSL.setLat(latitude);
        if(latitude>=0){
            GPSL.setUnsignedLatitude(latitude+"");
            GPSL.setNs("N");
        }
        else{
            GPSL.setUnsignedLatitude((-1) * latitude+"");
            GPSL.setNs("S");
        }

        GPSL.setLon(longitude);
        if(longitude>=0){
            GPSL.setUnsignedLongitude(longitude + "");
            GPSL.setEw("E");
        }
        else{
            GPSL.setUnsignedLongitude((-1) * longitude + "");
            GPSL.setEw("W");
        }

        try {
            if(ConnectionChecker.check(context)==false){
                throw new Exception("Brak połączenia z internetem");
            }
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.size()>0) {
                Address address = addresses.get(0);
                GPSL.setCity(address.getLocality());
                GPSL.setAddress(address.getThoroughfare() + (address.getThoroughfare().equals(address.getFeatureName())? "" : ", " + address.getFeatureName()));
            }
        } catch (Exception e) {
            throw e;
        }
        finally {
            GPSL.debug();
            return GPSL;
        }

    }

    public GPSLocation getGPSLocation() throws Exception {
        requestLocationUpdate();
        Location location = getLastKnownLocation();
        if (location == null){
            throw new Exception("GPS nie dostępny");
        }

        return getGPSLocation(location.getLatitude(),location.getLongitude());

    }



    public void requestLocationUpdate() {
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 10, locListener);

    }

    static public double distanceBetweenPoints(Location start, Location end){
        double distance = start.distanceTo(end);
        Log.d("distance" , "distance " + distance);
        return distance;
    }

    @Nullable
    public Location getLastKnownLocation(){
        Location locationGPS = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }


}
