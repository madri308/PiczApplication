package com.example.estebanmadrigal.piczapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Log.d("Test","hola");

    }
    public void goToFeed(View view){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }
}
