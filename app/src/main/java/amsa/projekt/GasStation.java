package amsa.projekt;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class GasStation implements Comparable<GasStation> {
    private Long id;
    private String name;
    @Nullable
    private String city;
    @Nullable
    private String address;
    private Double latitude;
    private Double longitude;

    @Nullable
    private Double distanceTo;

    public GasStation() {
    }

    public GasStation(Long id,String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public GasStation(Long id, String name, String city, String address, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compareTo(GasStation another) {
        if(this.distanceTo == null && another.distanceTo==null) {
            Log.d("compare", "this.distanceTo == null && another.distanceTo==null");
            return 0;
        }
        if(this.distanceTo==null){
            Log.d("compare", "this.distanceTo==null");
            return -1;
        }
        if(another.distanceTo==null){
            Log.d("compare", "another.distanceTo==null");
            return 1;
        }
        if(distanceTo> another.distanceTo){
            Log.d("compare", "distanceTo> another.distanceTo");
            return 1;
        }
        if(distanceTo<another.distanceTo) {
            Log.d("compare", "distanceTo<another.distanceTo");
            return -1;
        }
        Log.d("compare", "");
        return 0;
    }
    public Double getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(Double distanceTo) {
        this.distanceTo = distanceTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getLatitudeToString(){
        String prefix = "";
        if(getLatitude()>0){
            prefix = "N";
        }
        if(getLatitude()<0){
            prefix="S";
        }
        return prefix + getLatitude().toString();
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLongitudeToString(){
        String prefix = "";
        if(getLatitude()>0){
            prefix = "E";
        }
        if(getLatitude()<0){
            prefix="W";
        }
        return prefix + getLongitude().toString();
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
