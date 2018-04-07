package br.com.hackaton.vemcomigo;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Coordinate {

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Coordinate(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public static Coordinate fromLatLng(LatLng obj) {
        return new Coordinate(String.valueOf(obj.latitude), String.valueOf(obj.longitude));
    }
}
