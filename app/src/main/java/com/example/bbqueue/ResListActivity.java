package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ResListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_list);
    }
    public void navQueue(View view) {
        Intent intent = new Intent(this, InQueue.class);
        startActivity(intent);
    }
}
