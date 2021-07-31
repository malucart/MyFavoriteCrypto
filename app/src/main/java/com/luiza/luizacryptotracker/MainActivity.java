/*
    Feed page
*/

package com.luiza.luizacryptotracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private ImageButton ibEmptyHeart;
    private ImageButton ibLike;
    private Toolbar toolbar;

    private ArrayList<CryptoModel> cryptoModels;
    private CryptoAdapter cryptoAdapter;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rvCrypto);
        cryptoModels = new ArrayList<>();
        pbLoading = findViewById(R.id.pbLoading);
        ibEmptyHeart = findViewById(R.id.ibEmptyHeart);
        ibLike = findViewById(R.id.ibLike);
        toolbar = findViewById(R.id.mainToolbar);

        // queryFavoriteModel();

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

        // calling get data method to get data from API
        // getDataFromAPI();
        // RequestAPI requestAPI = new RequestAPI();
        // requestAPI.getDataFromAPI(MainActivity.this, pbLoading, cryptoModels, cryptoAdapter);
        new RequestAPI().getDataFromAPI(this, pbLoading, cryptoModels, cryptoAdapter);

        // heart section
        ibEmptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LikedActivity.class);
                startActivity(i);
            }
        });
    }

    private void queryFavoriteModel() {
        // Specify which class to query
        ParseQuery<FavoriteModel> query = ParseQuery.getQuery(FavoriteModel.class);
        query.include(FavoriteModel.KEY_USER);
        // Specify the object id
        query.getInBackground(String.valueOf(new FindCallback<FavoriteModel>() {
            @Override
            public void done(List<FavoriteModel> favoriteModels, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting FavoriteModels!", e);
                    return;
                }

                for (FavoriteModel FavoriteModel : favoriteModels) {
                    Log.i(TAG, "FavoriteModel: " + FavoriteModel.getSymbol() + ", user: " + FavoriteModel.getUser());
                }
            }
        }));
    }

    // allows menu on actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // allows click on an item on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "See you soon!", Toast.LENGTH_SHORT).show();
        onLogoutButton();
        return super.onOptionsItemSelected(item);
    }

    private void onLogoutButton() {
        // navigate backwards to Login screen
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}