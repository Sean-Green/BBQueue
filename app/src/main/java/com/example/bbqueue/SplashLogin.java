package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SplashLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);
    }

    public void login(View view) {
        Intent intent = new Intent(this, Store_Activity.class);
        startActivity(intent);
    }

    public void register(View view) {
        String toastText = getString(R.string.rgstToast);
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    public void navFrontOfQueue(View view) {
        Intent intent = new Intent(this, Front_Queue.class);
        startActivity(intent);
    }

    public void navResList(View view) {
        Intent intent = new Intent(this, ResListActivity.class);
        startActivity(intent);
    }

    public void navQueue(View view) {
        Intent intent = new Intent(this, InQueue.class);
        startActivity(intent);
    }
}