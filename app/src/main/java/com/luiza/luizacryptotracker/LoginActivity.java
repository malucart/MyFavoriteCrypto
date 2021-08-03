/*
    Login page
 */

package com.luiza.luizacryptotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.luiza.luizacryptotracker.animation.TypeWriter;
import com.parse.Parse;
import com.parse.facebook.ParseFacebookUtils;
import com.parse.ParseUser;

import com.facebook.GraphRequest;
import com.facebook.AccessToken;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Collection;

public class  LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String TAG_FB = "FacebookLoginluiza";

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

        // dont need to login again if the user closes the app
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

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
        tw.animateText(getString(R.string.joinTheCommunity));

        btnLogin = findViewById(R.id.btnLogin);

        // Login with Facebook
        btnLogin.setOnClickListener(v -> {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.title));
            dialog.setMessage(getString(R.string.message));
            dialog.show();
            Collection<String> permissions = Arrays.asList(getString(R.string.publicProfile), getString(R.string.email));
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(getString(R.string.back4app_app_id))
                    .clientKey(getString(R.string.back4app_client_key))
                    .server(getString(R.string.back4app_server_url))
                    .enableLocalDataStore()
                    .build());
            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, (user, callback) -> {
                dialog.dismiss();
                if (callback != null) {
                    Log.e(TAG_FB, getString(R.string.done), callback);
                    Toast.makeText(this, callback.getMessage(), Toast.LENGTH_LONG).show();
                } else if (user == null) {
                    Toast.makeText(this, getString(R.string.loginCancelled), Toast.LENGTH_LONG).show();
                    Log.d(TAG_FB, getString(R.string.loginCancelled));
                } else if (user.isNew()) {
                    Toast.makeText(this, getString(R.string.signedUp), Toast.LENGTH_LONG).show();
                    Log.d(TAG_FB, getString(R.string.signedUp));
                    getUserDetailFromFB();
                } else {
                    Toast.makeText(this, getString(R.string.loggedIn), Toast.LENGTH_LONG).show();
                    Log.d(TAG_FB, getString(R.string.loggedIn));
                    getUserDetailFromParse();
                }
            });
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void getUserDetailFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        String title = getString(R.string.welcome);
        String message = getString(R.string.user) + " " + user.getUsername() + "\n" + getString(R.string.loginEmail) + " " + user.getEmail();
        AlertMessages(title, message);
    }

    // Get user info by their Facebook credentials
    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            ParseUser user = ParseUser.getCurrentUser();
            try {
                if (object.has(getString(R.string.user)))
                    user.setUsername(object.getString(getString(R.string.user)));
                if (object.has(getString(R.string.loginEmail)))
                    user.setEmail(object.getString(getString(R.string.loginEmail)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.saveInBackground(e -> {
                if (e == null) {
                    AlertMessages(getString(R.string.welcome), getString(R.string.beingHere));
                } else
                    AlertMessages(getString(R.string.error), e.getMessage());
            });
        });

        Bundle parameters = new Bundle();
        parameters.putString(getString(R.string.fields), getString(R.string.user) + getString(R.string.loginEmail));
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Alert messages
    private void AlertMessages(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
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
