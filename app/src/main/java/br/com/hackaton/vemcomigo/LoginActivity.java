package br.com.hackaton.vemcomigo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.hackaton.vemcomigo.R.*;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        ButterKnife.bind(this);

        if (Profile.getCurrentProfile() != null) {
            checkRide();
        }

        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, getCallbackForLogin());
        LoginManager.getInstance().registerCallback(callbackManager, getCallbackForLogin());

    }

    private void checkRide() {

        db.collection("rides")
                .get()
                .addOnCompleteListener(getOnCompleteListener());

    }

    public void handleLogin() {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

        boolean hasRide = UserUtils.getInstance().hasRide();

        if (loggedIn) {

            if (!hasRide) {
                Intent intent = new Intent(this, StartPinActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ConfirmationActivity.class);
                startActivity(intent);
            }
        }
    }

    @NonNull
    private OnCompleteListener<QuerySnapshot> getOnCompleteListener() {
        return new OnCompleteListener<QuerySnapshot>() {
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
                        String loggedUserId = Profile.getCurrentProfile().getFirstName();
                        String userId = (String) data.get("userId");

                        if (loggedUserId.equals(userId) && !company.isEmpty()) {
                            UserUtils.getInstance().setHasRide(true);
                            handleLogin();
                            Log.w("TEST", "user is" + company);
                            return;
                        }
                    }
                    handleLogin();

                } else {
                    handleLogin();
                    Log.w("DB", "Error getting documents.", task.getException());
                }
            }
        };
    }


    private FacebookCallback<LoginResult> getCallbackForLogin() {
        final AppCompatActivity activity = this;

        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(activity, StartPinActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
