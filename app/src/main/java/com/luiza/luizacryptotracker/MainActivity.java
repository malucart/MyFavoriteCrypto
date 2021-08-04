/*
    This is the feed page
*/

package com.luiza.luizacryptotracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.luiza.luizacryptotracker.adapter.CryptoAdapter;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<CryptoModel> cryptoModels;

    private CryptoAdapter cryptoAdapter;

    private DatabaseHandler favDB;
    // we *only* need to pull the database on the first time we initialize the application
    private static boolean isFirstInit = true;
    // database update timer
    private Timer databaseUpdateTimer;

    public void updateCryptoModelInRemoteDatabase(CryptoModel model) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteModel");

        try {
            ParseObject object = query.get(model.getObjectId());
            object.put("name", model.getName());
            object.put("symbol", model.getSymbol());
            object.put("logoURL", model.getLogoURL());
            object.put("price", model.getPrice());
            object.put("oneHour", model.getOneHour());
            object.put("twentyFourHour", model.getTwentyFourHour());
            object.put("oneWeek", model.getOneWeek());
            object.put("favStatus", true);
            object.put("user", ParseUser.getCurrentUser());

            try {
                object.save();
            } catch(Exception x) {
                Log.d("updateCryptRDB", x.getMessage());
            }
        } catch (Exception e) {
            Log.d("updateCryptRDB", e.getMessage());
        }

    }

    public void createCryptoModelInRemoteDatabase(CryptoModel model) {
        ParseObject entity = new ParseObject("FavoriteModel");

        entity.put("user", ParseUser.getCurrentUser().getUsername());
        entity.put("name", model.getName());
        entity.put("symbol", model.getSymbol());
        entity.put("logoURL", model.getLogoURL());
        entity.put("price", model.getPrice());
        entity.put("oneHour", model.getOneHour());
        entity.put("twentyFourHour", model.getTwentyFourHour());
        entity.put("oneWeek", model.getOneWeek());
        entity.put("user", ParseUser.getCurrentUser());

        // saves the new objects
        try {
            entity.save();
        } catch (Exception e) {
            Log.d("createCMRemoteDB", e.getMessage());
        }
    }

    // pull the remove database and cache it locally so we don't need to query it every time
    public void pullDataFromExternalDatabase() {
        ParseQuery<ParseObject> query = new ParseQuery<>("FavoriteModel");

        query.findInBackground((list, e) -> {
            for(ParseObject p : list){
                CryptoModel aux = new CryptoModel();
                aux.setName((String)p.get("name"));
                aux.setSymbol((String)p.get("symbol"));
                aux.setLogoURL((String)p.get("logoURL"));
                aux.setPrice((Double)p.get("price"));
                aux.setOneHour((Double)p.get("oneHour"));
                aux.setTwentyFourHour((Double)p.get("twentyFourHour"));
                aux.setOneWeek((Double)p.get("oneWeek"));
                aux.setObjectId(p.getObjectId());
                favDB.insertDataIntoDatabase(aux);
            }
        });
    }

    public void updateRemoteDatabase()
    {
        // timer will keep calling this function every X ms
        // if we have no new updates, just ignore (save bandwidth)
        if (!cryptoAdapter.getIsUpdateRemoteDatabase()) {
            return;
        }

        // set it to false, otherwise we will keep updating the database non stop
        cryptoAdapter.setIsRemoteUpdateDatabase(false);

        ArrayList<CryptoModel> favList = favDB.getFavListFromDatabase();
         /*
          there are two situations: we are updating information regarding a crypto
          that is already in the database, so we just need to update the values
          and the situation that is something new, we can figure it out based
          on the "objectId" field
         */
        for (int i = 0; i < favList.size(); i++) {
            CryptoModel aux = favList.get(i);

            if (aux.getObjectId().equals("")) {
                // new object, just insert into the database
                createCryptoModelInRemoteDatabase(aux);
            }
            else {
                /*
                 for update is a little more complicated, favList that is in the SQLite database
                 is the "cached" version of the remote database, so we need to find the symbol
                 inside the CryptoModel and use it as base (and update its objectId).
                */
                CryptoModel targetModel;
                for (int j = 0; j < cryptoModels.size(); j++) {
                    if (cryptoModels.get(j).getSymbol().equals(aux.getSymbol())) {
                        targetModel = cryptoModels.get(j);
                        targetModel.setObjectId((aux.getObjectId()));
                        updateCryptoModelInRemoteDatabase(targetModel);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rvCrypto);
        cryptoModels = new ArrayList<>();
        ProgressBar pbLoading = findViewById(R.id.pbLoading);
        ImageButton ibEmptyHeart = findViewById(R.id.ibEmptyHeart);
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        favDB = new DatabaseHandler(this);

        pbLoading.setVisibility(ProgressBar.VISIBLE);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        // initializing the adapter class
        cryptoAdapter = new CryptoAdapter(cryptoModels, this);

        // setting adapter to recycler view
        rv.setAdapter(cryptoAdapter);

        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView
        // as well as determining the policy for when to recycle item views that are no longer visible to the use
        rv.setLayoutManager(new LinearLayoutManager(this));

        // we only need to pull data remotely if is the first time we are starting the app
        // if we are moving between intent, this isn't necessary
        if (isFirstInit) {
            pullDataFromExternalDatabase();
            isFirstInit = false;
        }

        // calling get data method to get data from API
        new com.luiza.luizacryptotracker.api.RequestAPI().getDataFromAPI(this, pbLoading, cryptoModels, cryptoAdapter);

        // necessary every time the cryptoAdapter is updated
        cryptoAdapter.notifyDataSetChanged();

        // create a task to execute at fixed time so we can save our local database
        // if we have any changes
        databaseUpdateTimer = new Timer();
        // Time in MS how often we will need to update the database
        int updateDatabaseMS = 5000;
        databaseUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                updateRemoteDatabase();
            }
        }, 0, updateDatabaseMS);

        // heart section
        ibEmptyHeart.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LikedActivity.class);
                startActivity(i);
        });
    }

    // allows menu on actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // allows click on an item on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "See you soon!", Toast.LENGTH_SHORT).show();
        // stop the database timer (to avoid issues)
        databaseUpdateTimer.cancel();
        // tell that we need to update the database remotely
        cryptoAdapter.setIsRemoteUpdateDatabase(true);
        // update the database and logout
        updateRemoteDatabase();
        onLogoutButton();
        return super.onOptionsItemSelected(item);
    }

    // user logged out the session
    public void onLogoutButton() {
        // navigate backwards to Login screen
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}