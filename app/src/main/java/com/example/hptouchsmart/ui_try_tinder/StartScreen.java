package com.example.hptouchsmart.ui_try_tinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class StartScreen extends AppCompatActivity {

    Button nextAct;
    public static final String PUBLIC_KEY = "key";
    EditText getString;
    LoginButton loginButton;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_start_screen);

        nextAct = (Button) findViewById(R.id.button);
        getString = (EditText) findViewById(R.id.et);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("user_friends", "email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                Toast.makeText(StartScreen.this, profile.getName(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

            ///////////////


        });

        nextAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passString = getString.getText().toString();
                if (!passString.isEmpty()) {
                    Intent i = new Intent(StartScreen.this, MainActivity.class);
                    i.putExtra(PUBLIC_KEY, passString);
                    startActivity(i);
                } else {
                    Toast.makeText(StartScreen.this, "no text inserted !!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode , resultCode, data);
    }
}
