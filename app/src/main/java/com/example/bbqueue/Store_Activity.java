package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Store_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_);
    }

    public void editStoreClick(View view) {
        Intent intent = new Intent(this, Edit_Store_Details_Activity.class);
        startActivity(intent);
    }

    public void viewQueueClick(View view) {
        Intent intent = new Intent(this, CustQueue.class);
        startActivity(intent);
    }
}