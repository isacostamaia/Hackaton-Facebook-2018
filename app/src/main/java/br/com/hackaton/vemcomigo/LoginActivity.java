package br.com.hackaton.vemcomigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.hackaton.vemcomigo.R.*;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        ButterKnife.bind(this);

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

        if (loggedIn) {
            Intent intent = new Intent(this, StartPin.class);
            startActivity(intent);
        }

        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, getCallbackForLogin());
        LoginManager.getInstance().registerCallback(callbackManager, getCallbackForLogin());

    }

    private FacebookCallback<LoginResult> getCallbackForLogin() {
        final AppCompatActivity activity = this;

        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(activity, StartPin.class);
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
