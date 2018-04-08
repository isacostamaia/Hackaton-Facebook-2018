package br.com.hackaton.vemcomigo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coordinate {

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Coordinate(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getDistance(Coordinate end) {
        float[] results = new float[3];
        Location.distanceBetween(Double.parseDouble(this.getLatitude()),Double.parseDouble(this.getLongitude()),
                Double.parseDouble(end.getLatitude()), Double.parseDouble(end.getLongitude()), results);
        return results[0];
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
