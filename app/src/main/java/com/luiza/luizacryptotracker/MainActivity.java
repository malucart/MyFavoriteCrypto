/*
    Feed page
*/

package com.luiza.luizacryptotracker;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String apiURL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptoModels;
    private CryptoAdapter cryptoAdapter;
    private ProgressBar pbLoading;
    private ImageView ibEmptyHeart;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rvCrypto);
        cryptoModels = new ArrayList<>();
        pbLoading = findViewById(R.id.pbLoading);
        ibEmptyHeart = findViewById(R.id.ibEmptyHeart);
        toolbar = findViewById(R.id.mainToolbar);

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
        getDataFromAPI();

        // heart section
        ibEmptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeartButton(); // goes to the liked page
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

    private void onHeartButton() {
        Intent i = new Intent(this, LikedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void onLogoutButton() {
        // navigate backwards to Login screen
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void getDataFromAPI() {
        // RequestQueue -> all the requests are queued up that has to be executed
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // extracting data from response and passing it to array list
                // bar visibility to gone
                pbLoading.setVisibility(View.GONE);
                try {
                    // extracting data from json
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String name = dataObject.getString("name");
                        String id = dataObject.getString("id");
                        String symbol = dataObject.getString("symbol");
                        // we need to get quote and usd because price is inside of an array of the json array
                        JSONObject quote = dataObject.getJSONObject("quote");
                        JSONObject usd = quote.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        double oneHour = usd.getDouble("percent_change_1h");
                        double twentyFourHour = usd.getDouble("percent_change_24h");
                        double oneWeek = usd.getDouble("percent_change_7d");

                        String logo = "https://s2.coinmarketcap.com/static/img/coins/128x128/" + id + ".png";
                        //Glide.with(MainActivity.this).load(logo).into(ivLogo);
                        cryptoModels.add(new CryptoModel(name, symbol, logo, price, oneHour, twentyFourHour, oneWeek));
                    }

                    cryptoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                        // handling json exception
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
                    }
                }
            }, error -> {
                // displaying error response when received any error while json object request to fetch data from API!!
                Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
            }) {
                @Override
                // map is interface and hashmap is a class that implements map
                public Map<String, String> getHeaders() {
                    // passing headers as key along with API keys
                    // we want to associate a key with a value so hashmap is the best option
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(getString(R.string.pro_api_key), getString(R.string.coin_api_key));
                    return headers;
                }
            };
        // add all the json object data we request from the API to the queue
        queue.add(jsonObjectRequest);
    }
}