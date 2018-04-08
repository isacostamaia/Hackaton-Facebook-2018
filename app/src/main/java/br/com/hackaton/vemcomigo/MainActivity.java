package br.com.hackaton.vemcomigo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

        private static final String DEBUG_TAG = "Gestures";
        private GestureDetectorCompat mDetector;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        @BindView(R.id.saudation)
        TextView saudationTextView;

        @BindView(R.id.rides_list)
        RecyclerView ridesList;

        private List<Ride> rides = new ArrayList<>();
        private RidesAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private Ride currentRide;

        // Called when the activity is first created.
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            // Instantiate the gesture detector with the
            // application context and an implementation of
            // GestureDetector.OnGestureListener
            mDetector = new GestureDetectorCompat(this, this);
            // Set the gesture detector as the double tap
            // listener.
            mDetector.setOnDoubleTapListener(this);

            setTitle("Quem pode te acompanhar");

            Intent intent = getIntent();
            if (intent != null && intent.getStringExtra("currentRide") != null) {
                String json = intent.getStringExtra("currentRide");
                JsonParser parser = new JsonParser();
                JsonObject rideJsonObject = parser.parse(json).getAsJsonObject();
                this.currentRide = Ride.createFromJson(rideJsonObject);
            }

            ridesList.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            ridesList.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new RidesAdapter(this, rides, currentRide);
            ridesList.setAdapter(mAdapter);


            final Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                saudationTextView.setText("Olá, " + profile.getFirstName() + "! Escolha a sua companhia na lista abaixo");
            }

            db.collection("rides")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Ride> rides = new ArrayList<>();
                                Gson gson = new Gson();

                                for (DocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> data = document.getData();
                                    Coordinate startPoint = gson.fromJson((String) data.get("startPoint"), Coordinate.class);
                                    Coordinate endPoint = gson.fromJson((String) data.get("endPoint"), Coordinate.class);
                                    String company = (String) data.get("company");
                                    String loggedUserId = profile.getFirstName();
                                    String userId = (String) data.get("userId");

                                    if (!loggedUserId.equals(userId) && startPoint.getLatitude() != null && endPoint.getLatitude() != null) {
                                        rides.add(new Ride(userId, startPoint, endPoint, company));
                                    }

                                }

                                mAdapter.setRides(rides);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.w("DB", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
}

