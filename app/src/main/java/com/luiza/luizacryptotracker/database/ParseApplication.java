/*
    Class that makes the connection with the database black4app
 */

package com.luiza.luizacryptotracker.database;

import android.app.Application;

import com.luiza.luizacryptotracker.R;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.facebook.ParseFacebookUtils;

public class ParseApplication extends Application {

    @Override
    public void onCreate () {
        super.onCreate();
        // Parse models
        // ParseObject.registerSubclass(FavoriteModel.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
    }
}