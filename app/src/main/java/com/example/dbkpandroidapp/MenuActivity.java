package com.example.dbkpandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private String login;
    private String passwordHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        login = getIntent().getExtras().getString("Login");
        passwordHash = getIntent().getExtras().getString("Password");
    }

    public void onClickUsersButton(View view) {
        Intent myIntent = new Intent(this, UsersActivity.class);
        myIntent.putExtra("Login", login);
        myIntent.putExtra("Password", passwordHash);
        startActivity(myIntent);
    }

    public void onClickStatsButton(View view) {
        Intent myIntent = new Intent(this, StatsActivity.class);
        myIntent.putExtra("Login", login);
        myIntent.putExtra("Password", passwordHash);
        startActivity(myIntent);
    }
}