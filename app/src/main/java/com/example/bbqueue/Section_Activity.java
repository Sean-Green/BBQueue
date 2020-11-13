package com.example.bbqueue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Section_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private ListView lvTables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(mAuth.getCurrentUser().getUid());

        mAuth = FirebaseAuth.getInstance();
        lvTables = findViewById(R.id.tableListView);
    }



    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
    }

    //old code for layout inflater, may reuse for table editing
    //public void openHoursDialog(View view) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setTitle(R.string.hoobtn);
//        LayoutInflater inflater = getLayoutInflater();
//
//        final View dialogView = inflater.inflate(R.layout.hours_op_dialog, null);
//        dialogBuilder.setView(dialogView);
//        final AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }
}