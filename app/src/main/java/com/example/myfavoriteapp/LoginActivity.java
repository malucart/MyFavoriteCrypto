package com.example.myfavoriteapp;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.parse.ParseUser;

import org.json.JSONException;
import java.util.Arrays;
import java.util.Collection;

import android.view.View;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private Button btnLogin;

    // Getters
    public Button getbtnLogin() {
        return this.btnLogin;
    }

    // Setters
    public void setbtnLogin(Button newbtnLogin) {
        this.btnLogin = newbtnLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide(); // hide action bar
        // changing color on status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.orange));
        }

        // animation on textview
        final TypeWriter tw = (TypeWriter) findViewById(R.id.tvTitle);
        tw.setText("");
        tw.setCharacterDelay(150);
        tw.animateText("Join the Community");

        btnLogin = findViewById(R.id.btnLogin);

        // Login with Facebook
        btnLogin.setOnClickListener(v -> {
            // Current user
            ParseUser currentUser = ParseUser.getCurrentUser();
            // Enable Local Datastore
            // Parse.enableLocalDatastore(this);
            // Register any ParseObject subclass. Must be done before calling Parse.initialize()
            // ParseObject.registerSubclass(ParseObject.class);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Please, wait just a moment...");
            dialog.setMessage("Logging in...");
            dialog.show();
            Collection<String> permissions = Arrays.asList("public_profile", "email");
            // ParseFacebookUtils.initialize(this);
            Parse.initialize(new Parse.Configuration.Builder(this) .applicationId("asUgLz8pMENiW2p9rgH3JiaNu7Rzda5OeDCepcvs").clientKey("EYMqCijfTEkqiXmLbE6YztigcvjI5fgiAsRQKsOQ").server("https://parseapi.back4app.com").enableLocalDataStore() .build());
            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, (user, err) -> {
                dialog.dismiss();
                if (err != null) {
                    Log.e("FacebookLoginExample", "done: ", err);
                    Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG).show();
                } else if (user == null) {
                    Toast.makeText(this, "Facebook login was cancelled.", Toast.LENGTH_LONG).show();
                    Log.d("FacebookLoginExample", "Facebook login was cancelled...");
                } else if (user.isNew()) {
                    Toast.makeText(this, "User signed up and logged in through Facebook.", Toast.LENGTH_LONG).show();
                    Log.d("FacebookLoginExample", "User signed up and logged in through Facebook...");
                    getUserDetailFromFB();
                } else {
                    Toast.makeText(this, "User logged in using their Facebook account.", Toast.LENGTH_LONG).show();
                    Log.d("FacebookLoginExample", "User logged in using their Facebook account...");
                    // AlertMessages("Oh, you!", "Welcome back!");
                    getUserDetailFromParse();
                }
            });
        });
    }

    private void getUserDetailFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        String title = "Welcome!";
        String message = "User:  " + user.getUsername() + "\n" + "Login email: " + user.getEmail();
        AlertMessages(title, message);
    }

    // Get user info by his Facebook credentials
    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            ParseUser user = ParseUser.getCurrentUser();
            try {
                if (object.has("name"))
                    user.setUsername(object.getString("name"));
                if (object.has("email"))
                    user.setEmail(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.saveInBackground(e -> {
                if (e == null) {
                    AlertMessages("Welcome!", "We know you would be here!");
                } else
                    AlertMessages("Error!", e.getMessage());
            });
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Alert messages
    private void AlertMessages(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}