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
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;

public class LikedActivity extends AppCompatActivity {
    private static final String TAG = "LikedActivity";
    private Toolbar toolbar;
    private ImageButton ibFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        toolbar = findViewById(R.id.mainToolbar);
        ibFavorite = findViewById(R.id.ibFavorite);

        // sets the toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

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
}
