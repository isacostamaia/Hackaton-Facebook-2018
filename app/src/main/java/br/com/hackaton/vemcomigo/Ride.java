package br.com.hackaton.vemcomigo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ride {

    @SerializedName("userId")
    String userId;

    @SerializedName("startPoint")
    Coordinate startPoint;

    @SerializedName("endPoint")
    Coordinate endPoint;

    @SerializedName("photo")
    String photoUrl;

    @SerializedName("company")
    String company;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Ride(String userId, Coordinate startPoint, Coordinate endPoint, String company){
        this.userId=userId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public String getUserId() {
        return userId;
    }

    public Coordinate getStartPoint() {
        return startPoint;
    }

    public LatLng getStartPointAsLatLng() {
        return new LatLng(Double.valueOf(this.startPoint.getLatitude()), Double.valueOf(this.startPoint.getLongitude()));
    }

    public Coordinate getEndPoint() {
        return endPoint;
    }

    public LatLng getEndPointAsLatLng() {
        return new LatLng(Double.valueOf(this.endPoint.getLatitude()), Double.valueOf(this.endPoint.getLongitude()));
    }

    public float getStartDistance(Ride ride2) {
        return this.getStartPoint().getDistance(ride2.getStartPoint());
    }

    public float getEndDistance(Ride ride2) {
        return this.getEndPoint().getDistance(ride2.getEndPoint());
    }

    public String getAsJson() {
        Gson gson = new Gson();
        Map<String, Object> newRide = new HashMap<>();
        newRide.put("startPoint", gson.toJson(this.getStartPoint()));
        newRide.put("endPoint", gson.toJson(this.getEndPoint()));
        newRide.put("userId", this.getUserId());
        newRide.put("company", this.getCompany());
        return gson.toJson(newRide);
    }

    public static Ride createFromJson(JsonObject json) {
        JsonParser parser = new JsonParser();
        JsonObject startPointJson = parser.parse(json.get("startPoint").getAsString()).getAsJsonObject();
        JsonObject endPointJson = parser.parse(json.get("endPoint").getAsString()).getAsJsonObject();
        Coordinate startPoint = new Coordinate(startPointJson.get("latitude").getAsString(), startPointJson.get("longitude").getAsString());
        Coordinate endPoint = new Coordinate(endPointJson.get("latitude").getAsString(), endPointJson.get("longitude").getAsString());
        return new Ride(json.get("userId").getAsString(),
                startPoint, endPoint,
                json.get("company").getAsString());

    }

    public void saveRideToDatabase() {
        Gson gson = new Gson();
        Map<String, Object> newRide = new HashMap<>();
        newRide.put("startPoint", gson.toJson(this.getStartPoint()));
        newRide.put("endPoint", gson.toJson(this.getEndPoint()));
        newRide.put("userId", this.getUserId());
        newRide.put("company", this.getCompany());

        db.collection("rides").add(newRide).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DB", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB", "Error adding document", e);
                    }
                });
    }
}
