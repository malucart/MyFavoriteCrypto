package com.luiza.luizacryptotracker;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/*
public class RequestAPI extends MainActivity {

    private static String apiURL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static final String TAG = "RequestAPI";
    private MainActivity mainActivity = new MainActivity();

    void getDataFromAPI() {
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
                        // Glide.with(MainActivity.this).load(logo).into(ivLogo);
                        cryptoModels.add(new CryptoModel(name, symbol, logo, price, oneHour, twentyFourHour, oneWeek));
                    }

                    cryptoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // handling json exception
                    e.printStackTrace();
                    Toast.makeText(RequestAPI.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            // displaying error response when received any error while json object request to fetch data from API!!
            Toast.makeText(RequestAPI.this, getString(R.string.missing), Toast.LENGTH_SHORT).show();
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
*/