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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Ride(String userId, Coordinate startPoint, Coordinate endPoint){
        this.userId=userId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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


    public void saveRideToDatabase() {
        Gson gson = new Gson();
        Map<String, Object> newRide = new HashMap<>();
        newRide.put("startPoint", gson.toJson(this.getStartPoint()));
        newRide.put("endPoint", gson.toJson(this.getEndPoint()));
        newRide.put("userId", this.getUserId());

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
