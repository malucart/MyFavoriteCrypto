/*
    Logout details
 */

package com.example.luizacryptotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class LogoutActivity extends AppCompatActivity {

    private Button btnLogout;

    // Getters
    public Button getbtnLogout() {
        return this.btnLogout;
    }

    // Setters
    public void setbtnLogout(Button newbtnLogin) {
        this.btnLogout = newbtnLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_logout);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                alertDisplayer(getString(R.string.goingBack), getString(R.string.bye));
            }
        });
    }

    private void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogoutActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
