package com.luiza.luizacryptotracker;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.luiza.luizacryptotracker.adapter.FavoriteAdapter;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {

    private static final String TAG = "LikedActivity";
    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptFavList = new ArrayList<CryptoModel>();
    private com.luiza.luizacryptotracker.adapter.FavoriteAdapter FavoriteAdapter;
    private Button bReddit;
    private ImageButton ibFavoriteModel;
    private ImageView ibLike;
    private Toolbar toolbar;
    private DatabaseHandler favDB;
    private GraphView gvGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        rv = findViewById(R.id.rvFavoriteModel);
        ibFavoriteModel = findViewById(R.id.ibFavoriteModel);
        toolbar = findViewById(R.id.mainToolbar);
        ibLike = findViewById(R.id.ibLike);
        // bReddit = (Button)findViewById(R.id.bReddit);
        // gvGraph = findViewById(R.id.gvGraph);

        favDB = new DatabaseHandler(this);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        cryptFavList = favDB.getFavListFromDatabase();

        // initializing the adapter class
        FavoriteAdapter = new FavoriteAdapter(cryptFavList, this);

        rv.setHasFixedSize(true);

        // setting adapter to recycler view
        rv.setAdapter(FavoriteAdapter);

        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView
        // as well as determining the policy for when to recycle item views that are no longer visible to the use
        rv.setLayoutManager(new LinearLayoutManager(this));

        // heart section
        ibFavoriteModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LikedActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
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
