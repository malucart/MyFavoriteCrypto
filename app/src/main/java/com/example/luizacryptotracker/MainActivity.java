/*
    Feed page
*/

package com.example.luizacryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luizacryptotracker.Adapter.CryptoAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String NOW_PLAYING_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=10&convert=USD";

    private List<CryptoModel> items = new ArrayList<>();
    private CryptoAdapter adapter;
    private RecyclerView rv;
    private OkHttpClient client;
    private Request request;
    private Button btnLogout;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // logout section
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutButton(); // navigate backwards to Login screen
            }
        });

        // swipe section
        swipeRefreshLayout = findViewById(R.id.mainLayout);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadCryptos(0);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                loadCryptos(0);
                setupAdapter();
            }
        });

        rv = findViewById(R.id.cryptoList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new CryptoAdapter(rv, MainActivity.this, items);
        rv.setAdapter(adapter);
        adapter.setInterfaceLoading(new Interface() {
            @Override
            public void onInterface() {
                if (items.size() <= 100) {  // Max size is 100 cryptos
                    loadNextCryptos(items.size());
                } else {
                    Toast.makeText(MainActivity.this, "Maximum of items is 100", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadNextCryptos(int size) {
        client = new OkHttpClient();
        request = new Request.Builder().url(String.format(NOW_PLAYING_URL, size)).build();
        swipeRefreshLayout.setRefreshing(true);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().toString();
                Gson gson = new Gson();
                List<CryptoModel> newItems = gson.fromJson(body, new TypeToken<List<CryptoModel>>(){}.getType());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.addAll(newItems);
                        adapter.setLoaded();
                        adapter.updateData(items);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void loadCryptos(int size) {
        client = new OkHttpClient();
        request = new Request.Builder().url(String.format(NOW_PLAYING_URL, size)).build();
        swipeRefreshLayout.setRefreshing(true);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(items);
                    }
                });
            }
        });

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onLogoutButton() {
        // forget who's logged in
        client = null;
        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}