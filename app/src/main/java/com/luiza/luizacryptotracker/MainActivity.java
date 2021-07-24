/*
    Feed page
*/

package com.luiza.luizacryptotracker;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String generalUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"; // url works to get name, symbol, price, 1h ago, 24h ago and 7d ago
    private static String infoUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol="; // url works to get the logo png
    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptoModels;
    private CryptoAdapter cryptoAdapter;
    private ProgressBar pbLoading;
    private Button btnLogout;
    private ImageView ivLogo;
    private ImageView ivEmptyHeart;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rvCrypto);
        cryptoModels = new ArrayList<>();
        pbLoading = findViewById(R.id.pbLoading);
        ivLogo = findViewById(R.id.ivLogo);
        btnLogout = findViewById(R.id.iLogout);
        toolbar = findViewById(R.id.mainToolbar);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        // initializing the adapter class
        cryptoAdapter = new CryptoAdapter(cryptoModels, this);

        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView
        // as well as determining the policy for when to recycle item views that are no longer visible to the use
        rv.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view
        rv.setAdapter(cryptoAdapter);

        // calling get data method to get data from API
        getDataFromAPI();

        // heart section
        /*
        ivEmptyHeart = findViewById(R.id.ivEmptyHeart);
        ivEmptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeartButton(); // goes to the liked page
            }
        });*/
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
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void onLogoutButton() {
        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void getDataFromAPI() {
        // RequestQueue -> all the requests are queued up that has to be executed
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, generalUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // extracting data from response and passing it to array list
                // bar visibility to gone
                pbLoading.setVisibility(View.GONE);
                try {
                    String symbolUrl = "";
                    // extracting data from json
                    JSONArray dataArray = response.getJSONArray("data");
                    List<String> symbolsList = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String name = dataObject.getString("name");
                        String symbol = dataObject.getString("symbol");
                        // we need to get quote and usd because price is inside of an array of the json array
                        JSONObject quote = dataObject.getJSONObject("quote");
                        JSONObject usd = quote.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        double oneHour = usd.getDouble("percent_change_1h");
                        double twentyFourHour = usd.getDouble("percent_change_24h");
                        double oneWeek = usd.getDouble("percent_change_7d");

                        symbolsList.add(symbol);
                        cryptoModels.add(new CryptoModel(name,symbol,price, oneHour, twentyFourHour, oneWeek));
                        symbolUrl += dataObject.getString("symbol") + ","; // storing the symbols
                    }

                   //  symbolUrl += String.join(",", symbolsList);

                    if (symbolUrl.length() > 0) {
                        sendRequestToTheSecondUrl(symbolUrl.substring(0, symbolUrl.length() - 1)); // delete the "," to send the request
                    }
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

    private void sendRequestToTheSecondUrl(String symbolUrl) {
        String logPNG = infoUrl + symbolUrl;
        // RequestQueue -> all the requests are queued up that has to be executed
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from the API's second url
        JsonObjectRequest jsonObjectRequestPNG = new JsonObjectRequest(Request.Method.GET, logPNG, null, new Response.Listener<JSONObject>() {
            List<String> logoList = new ArrayList<>();
            @Override
            public void onResponse(JSONObject response) {
                // bar visibility to gone
                pbLoading.setVisibility(View.GONE);
                try {
                    // extracting data from json
                    JSONObject object = new JSONObject("data");
                    String logo = object.getString("logo");
                    logoList.add(logo);
                    //cryptoModels.get().setLogoURL(logo);
                    Glide.with(MainActivity.this).load(logo).into(ivLogo);

                    // notifying adapter on data change
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
       queue.add(jsonObjectRequestPNG);
    }
}