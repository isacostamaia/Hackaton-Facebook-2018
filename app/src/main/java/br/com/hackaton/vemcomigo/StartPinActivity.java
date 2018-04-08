package br.com.hackaton.vemcomigo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class StartPinActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.send_location)
    Button sendLocationButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private static final int MY_PERMISSIONS_FINE_LOCATION=1;
    private LatLng startPoint;
    private LatLng endPoint;


    public void getCurrentLocation (){
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        startPoint = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(startPoint).draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint,16));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_pin);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        final Profile profile = Profile.getCurrentProfile();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        final Activity activity = this;
        sendLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endPoint!=null && startPoint!=null) {
                    Ride ride = new Ride(profile.getFirstName(), Coordinate.fromLatLng(startPoint), Coordinate.fromLatLng(endPoint));
                    ride.saveRideToDatabase();
                    Intent mainIntent = new Intent(activity, MainActivity.class);

                    mainIntent.putExtra("currentRide", ride.getAsJson());
                    activity.startActivity(mainIntent);
                }

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapClickedManager.getManagerInstance().setClicked(false);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_FINE_LOCATION);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (!MapClickedManager.getManagerInstance().isClicked()) {
                    mMap.addMarker(new MarkerOptions().position(point));
                    endPoint=point;
                    MapClickedManager.getManagerInstance().setClicked(true);
                    sendLocationButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
