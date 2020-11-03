package com.example.bbqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CreateAccountSubMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_sub_menu);
    }

    public void CreateCust(View view) {
        Intent intent = new Intent(this, CustomerAccountCreate.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    public void CreateRes(View view) {
        Intent intent = new Intent(this, CustomerAccountCreate.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

}