/*
    Feed page
*/

package com.example.luizacryptotracker;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // initializing the adapter class
        cryptoAdapter = new CryptoAdapter(cryptoModels, this);

        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView as well as determining the policy for when to recycle item views that are no longer visible to the use
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
        // to get the data
        String latest_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"; // <- url works to get name, symbol, price, 1h ago, 24h ago and 7d ago
        // to get the image data
        String info_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol"; // <- url that have logo.png of the cryptos
        String image_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol="; // <- that's how the url above gets the logo.png

        ArrayList<String> arrayName = new ArrayList<String>();
        ArrayList<String> arraySymbol = new ArrayList<String>();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();
        ArrayList<Double> arrayOneHour = new ArrayList<Double>();
        ArrayList<Double> arrayTwentyFourHour = new ArrayList<Double>();
        ArrayList<Double> arrayOneWeek = new ArrayList<Double>();

        // RequestQueue -> all the requests are queued up that has to be executed
        RequestQueue queue = Volley.newRequestQueue(this);
        // Symbol for all crypto, so we can request the image
        // ArrayList<String> symbolList = new ArrayList<String>();
        // making a json object request to fetch data from API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, latest_url, null, new Response.Listener<JSONObject>() {
            String image_crypto = image_url;
            @Override
            public void onResponse(JSONObject response) {
                // extracting data from response and passing it to array list
                // bar visibility to gone
                pbLoading.setVisibility(View.GONE);
                try {
                    // extracting data from json
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < 100; i++) {
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

                        arrayName.add(name);
                        arraySymbol.add(symbol);
                        arrayPrice.add(price);
                        arrayOneHour.add(oneHour);
                        arrayTwentyFourHour.add(twentyFourHour);
                        arrayOneWeek.add(oneWeek);

                        // call the API to get the image
                        if (i < 100) {
                            image_crypto = image_crypto + dataObject.getString("symbol" + ",");

                        } else {
                            image_crypto = image_crypto + dataObject.getString("symbol"); // if i == 100 means that i completed all the symbols on the url
                            // getting the logo string
                            ArrayList logo = sendRequestToTheSecondUrl(image_crypto);
                            cryptoModels.add(new CryptoModel(logo, arrayName, arraySymbol, arrayPrice, arrayOneHour, arrayTwentyFourHour, arrayOneWeek));
                        }
                    }
                    Log.i(TAG, "TEST: " + cryptoModels);
                    // notifying adapter on data change
                    cryptoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                        // handling json exception
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // displaying error response when received any error while json object request to fetch data from API!!
                    Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
                }
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

    private ArrayList sendRequestToTheSecondUrl(String image_crypto) {
        ArrayList<String> imagePNG = new ArrayList<String>();
        JsonObjectRequest jsonObjectRequest_image = new JsonObjectRequest(Request.Method.GET, image_crypto, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response_image) {
                String info_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol=";
                // bar visibility to gone
                pbLoading.setVisibility(View.GONE);
                try {
                    // extracting data from json
                    JSONArray dataArrayImage = response_image.getJSONArray("data");
                    for (int i = 0; i < 100; i++) {
                        JSONObject dataObjectImage = dataArrayImage.getJSONObject(i);
                        String logo = dataObjectImage.getString("logo");
                        Glide.with(MainActivity.this).load(logo).into(ivLogo);
                        imagePNG.add(logo);
                    }
                } catch (JSONException e) {
                    // handling json exception
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // displaying error response when received any error while json object request to fetch data from API!!
                Toast.makeText(MainActivity.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
            }
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
        // notifying adapter on data change
        cryptoAdapter.notifyDataSetChanged();
        return imagePNG;
    }
}