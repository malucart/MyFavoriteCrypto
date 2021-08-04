package com.luiza.luizacryptotracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luiza.luizacryptotracker.adapter.FavoriteAdapter;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {

    private static final String TAG = "LikedActivity";

    public LikedActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        RecyclerView rv = findViewById(R.id.rvFavoriteModelLike);
        ImageButton ibFavoriteModel = findViewById(R.id.ibFavoriteModelLike);
        Toolbar toolbar = findViewById(R.id.mainToolbarLike);

        DatabaseHandler favDB = new DatabaseHandler(this);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        ArrayList<CryptoModel> cryptFavList = favDB.getFavListFromDatabase();

        // initializing the adapter class
        com.luiza.luizacryptotracker.adapter.FavoriteAdapter favoriteAdapter = new FavoriteAdapter(cryptFavList, this);

        rv.setHasFixedSize(true);

        // setting adapter to recycler view
        rv.setAdapter(favoriteAdapter);
          /*
           setting layout manager to recycler view
           LayoutManager is responsible for measuring and positioning item views within a RecyclerView
           as well as determining the policy for when to recycle item views that are no longer visible to the use
          */
        rv.setLayoutManager(new LinearLayoutManager(this));

        // heart section
        ibFavoriteModel.setOnClickListener(v -> {
            Intent i = new Intent(LikedActivity.this, MainActivity.class);
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
