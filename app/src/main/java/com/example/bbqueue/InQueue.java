package com.example.bbqueue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.TextView;

public class InQueue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inqueue);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

        new CountDownTimer(10000, 1000) {
            TextView mTextField = findViewById(R.id.textView);
            TextView bTextField = findViewById(R.id.textView2);
            public void onTick(long millisUntilFinished) {
                mTextField.setText(getString(R.string.InQueueTimeRemaining, (millisUntilFinished/1000)));
            }

            public void onFinish() {
                mTextField.setText(R.string.InQueueDone);
                bTextField.setText("");
                Intent intent = new Intent(getApplicationContext(),Front_Queue.class);
                startActivity(intent);
            }
        }.start();

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
    }
}