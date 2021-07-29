package com.luiza.luizacryptotracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {
    private static final String TAG = "LikedActivity";
    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptoModels;
    private CryptoAdapter cryptoAdapter;
    private ImageButton ibFavorite;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        rv = findViewById(R.id.rvFavorite);
        cryptoModels = new ArrayList<>();
        ibFavorite = findViewById(R.id.ibFavorite);
        toolbar = findViewById(R.id.mainToolbar);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        // initializing the adapter class
        cryptoAdapter = new CryptoAdapter(cryptoModels, this);

        rv.setHasFixedSize(true);

        // setting adapter to recycler view
        rv.setAdapter(cryptoAdapter);


        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView
        // as well as determining the policy for when to recycle item views that are no longer visible to the use
        rv.setLayoutManager(new LinearLayoutManager(this));

        // heart section
        ibFavorite.setOnClickListener(new View.OnClickListener() {
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

    public void createObject() {
        ParseObject entity = new ParseObject("Favorite");

        entity.put("name", "A string");
        entity.put("symbol", "A string");
        entity.put("logoURL", "A string");
        entity.put("price", 1);
        entity.put("oneHour", 1);
        entity.put("twentyFourHour", 1);
        entity.put("oneWeek", 1);
        entity.put("user", ParseUser.getCurrentUser());
        entity.put("favStatus", "A string");

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(e -> {
            if (e==null){
                // save was done
            }else{
                //Something went wrong
                Toast.makeText(LikedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        //Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        //Something went wrong while deleting the Object
                        Toast.makeText(this, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //Something went wrong
                Toast.makeText(LikedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
