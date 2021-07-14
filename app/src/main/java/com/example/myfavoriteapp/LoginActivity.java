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
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btnSignUp;

    // Getters
    public Button getbtnLogin() {
        return this.btnLogin;
    }
    public Button getbtnSignUp() {
        return this.btnSignUp;
    }

    // Setters
    public void setbtnLogin(Button newbtnLogin) {
        this.btnLogin = newbtnLogin;
    }
    public void setbtnSignUp(Button newbtnSignUp) {
        this.btnSignUp = newbtnSignUp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Login with Facebook
        btnLogin.setOnClickListener(v -> {
            // Current user
            ParseUser currentUser = ParseUser.getCurrentUser();
            // Enable Local Datastore
            //Parse.enableLocalDatastore(this);
            // Register any ParseObject subclass. Must be done before calling Parse.initialize()
            //ParseObject.registerSubclass(ParseObject.class);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Please, wait just a moment...");
            dialog.setMessage("Logging in...");
            dialog.show();
            Collection<String> permissions = Arrays.asList("public_profile", "email");
            //ParseFacebookUtils.initialize(this);
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
                    getUserInfo();
                } else {
                    Toast.makeText(this, "User logged in using their Facebook account.", Toast.LENGTH_LONG).show();
                    Log.d("FacebookLoginExample", "User logged in using their Facebook account...");
                    AlertMessages("Oh, you!", "Welcome back!");
                }
            });
        });
    }

    // Get user info by his Facebook credentials
    private void getUserInfo() {
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
    private void AlertMessages(String s, String s1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(this, MainActivity.class);
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