package br.com.hackaton.vemcomigo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @BindView(R.id.saudation)
    TextView saudationTextView;

    @BindView(R.id.rides_list)
    RecyclerView ridesList;

    private List<Ride> rides = new ArrayList<>();
    private RidesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Ride currentRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
            saudationTextView.setText("Olá, " + profile.getFirstName()+"! Escolha a sua companhia na lista abaixo");
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

                                String loggedUserId = profile.getFirstName();
                                String userId = (String) data.get("userId");

                                if (!loggedUserId.equals(userId) && startPoint.getLatitude() != null && endPoint.getLatitude() != null) {
                                    rides.add(new Ride(userId, startPoint, endPoint));
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
}

