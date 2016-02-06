package amsa.projekt;

import android.util.Log;

public class GPSLocation{
    private String city, address, unsignedLatitude, unsignedLongitude, ns, ew;
    private double lat,lon;

    public GPSLocation() {
    }


    public void debug() {
        Log.d("GPS LOCATION", "<--- start --->");
        Log.d("GPS LOCATION","city : " + city);
        Log.d("GPS LOCATION","address : " + address);
        Log.d("GPS LOCATION","unsignedLatitude : " + unsignedLatitude);
        Log.d("GPS LOCATION","unsignedLongitude : " + unsignedLongitude);
        Log.d("GPS LOCATION","ns : " + ns);
        Log.d("GPS LOCATION","ew : " + ew);
        Log.d("GPS LOCATION","lat : " + lat);
        Log.d("GPS LOCATION","lon : " + lon);
        Log.d("GPS LOCATION","<--- stop --->");
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnsignedLatitude() {
        return unsignedLatitude;
    }

    public void setUnsignedLatitude(String unsignedLatitude) {
        this.unsignedLatitude = unsignedLatitude;
    }

    public String getUnsignedLongitude() {
        return unsignedLongitude;
    }

    public void setUnsignedLongitude(String unsignedLongitude) {
        this.unsignedLongitude = unsignedLongitude;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getEw() {
        return ew;
    }

    public void setEw(String ew) {
        this.ew = ew;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
