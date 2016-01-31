package amsa.projekt;

import android.support.annotation.Nullable;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class GasStation {

    private String name;
    @Nullable
    private String city;
    @Nullable
    private String address;
    private Double latitude;
    private Double longitude;

    public GasStation() {
    }

    public GasStation(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GasStation(String name, String city, String address, Double latitude, Double longitude) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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
