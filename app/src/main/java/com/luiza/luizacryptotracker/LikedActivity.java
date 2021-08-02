package com.luiza.luizacryptotracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luiza.luizacryptotracker.adapter.FavoriteAdapter;
import com.luiza.luizacryptotracker.database.DatabaseHandler;
import com.luiza.luizacryptotracker.model.CryptoModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {

    private static final String TAG = "LikedActivity";
    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptFavList = new ArrayList<CryptoModel>();
    private com.luiza.luizacryptotracker.adapter.FavoriteAdapter FavoriteAdapter;
    private ImageButton ibFavoriteModel;
    private ImageView ibLike;
    private Toolbar toolbar;
    private DatabaseHandler favDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        rv = findViewById(R.id.rvFavoriteModel);
        ibFavoriteModel = findViewById(R.id.ibFavoriteModel);
        ibLike = findViewById(R.id.ibLike);
        toolbar = findViewById(R.id.mainToolbar);

        favDB = new DatabaseHandler(this);


        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteModel");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("updateAt");
        // The query will search for a ParseObject, given its objectId
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
            } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        */

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        //CryptoModel node = new CryptoModel();

        // READ FROM LOCAL DATABASE HERE

        // ASSUMING WE HAVE ALREADY DOWNLOADED THE REMOTE DATA (DO IT LATER).
        /*
        node.setName("XD");
        node.setSymbol("BTC");
        node.setLogoURL("https://www.lotus-qa.com/wp-content/uploads/2020/02/testing.jpg");
        node.setPrice(1.0);
        node.setOneHour(1.0);
        node.setTwentyFourHour(0.0);
        node.setOneWeek(0.0);
        node.setFavStatus(true);



        cryptFavList = favDB.getFavListFromDatabase();
        cryptFavList.add(node); */
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

    public void createObject(String name, String symbol, String logoURL, Double price, Double oneHour, Double twentyFourHour, Double oneWeek, Boolean favStatus) {
        ParseObject entity = new ParseObject("FavoriteModel");

        entity.put("user", ParseUser.getCurrentUser().getUsername());
        entity.put("name", name);
        entity.put("symbol", symbol);
        entity.put("logoURL", logoURL);
        entity.put("price", price);
        entity.put("oneHour", oneHour);
        entity.put("twentyFourHour", twentyFourHour);
        entity.put("oneWeek", oneWeek);
        entity.put("user", ParseUser.getCurrentUser());
        entity.put("favStatus", favStatus);

        // saves the new objects
        entity.saveInBackground(e -> {
            if (e == null){
                // save was done
                Toast.makeText(LikedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LikedActivity.this, LikedActivity.class);
                finish();
                startActivity(intent);

            } else {
                //Something went wrong
                Toast.makeText(LikedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteModel");

        // Retrieve the object by id
        query.getInBackground("objectId", (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        //Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                    } else{
                        //Something went wrong while deleting the Object
                        Toast.makeText(this, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                //Something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
