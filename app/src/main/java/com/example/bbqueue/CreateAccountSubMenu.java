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
        startActivity(intent);
    }

    public void CreateRes(View view) {
        String toastText = getString(R.string.rgstToast);
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

}