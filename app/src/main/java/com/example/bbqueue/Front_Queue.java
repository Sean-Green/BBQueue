package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Front_Queue extends AppCompatActivity {
    EditText minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_queue);
        minutes = findViewById(R.id.minutesET);
    }

    public void queueAgainFromMinutes(View view) {
        String toastText = minutes.getText().toString();
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    public void queueAgainFromGPS(View view) {
        String toastText = getString(R.string.btnReQueueGPS);
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    public void getDirections(View view) {
        String toastText = getString(R.string.btnGetDirections);
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}