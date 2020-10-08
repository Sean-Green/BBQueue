package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class InQueue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inqueue);

        new CountDownTimer(30000, 1000) {
            TextView mTextField = findViewById(R.id.textView);
            TextView bTextField = findViewById(R.id.textView2);
            public void onTick(long millisUntilFinished) {
                mTextField.setText("Time remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
                bTextField.setText("");
            }
        }.start();

    }
}