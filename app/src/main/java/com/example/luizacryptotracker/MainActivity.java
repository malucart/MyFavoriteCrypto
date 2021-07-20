/*
    Feed page
*/

package com.example.luizacryptotracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ArrayList<CryptoModel> cryptoModels;
    private CryptoAdapter cryptoAdapter;
    private ProgressBar pbLoading;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rvCrypto);
        cryptoModels = new ArrayList<>();
        pbLoading = findViewById(R.id.pbLoading);

        // initializing the adapter class
        cryptoAdapter = new CryptoAdapter(cryptoModels, this);

        // setting layout manager to recycler view
        // LayoutManager is responsible for measuring and positioning item views within a RecyclerView as well as determining the policy for when to recycle item views that are no longer visible to the use
        rv.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view
        rv.setAdapter(cryptoAdapter);

        // calling get data method to get data from API
        getDataFromAPI();

        // logout section
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutButton(); // navigate backwards to Login screen
            }
        });
    }

    private void onLogoutButton() {
        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void getDataFromAPI() {
        String latest_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        // RequestQueue -> all the requests are queued up that has to be executed
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, latest_url, null, new Response.Listener<JSONObject>() {
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
                        String symbol = dataObject.getString("symbol");
                        String name = dataObject.getString("name");
                        // we need to get quote and usd because price is inside of an array of the json array
                        JSONObject quote = dataObject.getJSONObject("quote");
                        JSONObject usd = quote.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        // adding all data to our array list
                        cryptoModels.add(new CryptoModel(name, symbol, price));
                    }
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
}