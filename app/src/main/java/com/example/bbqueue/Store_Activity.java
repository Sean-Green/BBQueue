package com.example.bbqueue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//testa@test.ca
//tester
public class Store_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ListView lvSections;
    private Button btnAddSec;

    private int secInd;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser currentUser;
    ArrayList<Section> slist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_);

        //Get DB ref
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid());

        //Add back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create event listeners
        btnAddSec = findViewById(R.id.btnAddSection);
        btnAddSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSection as = new AddSection();
                as.execute();
            }
        });

        lvSections = findViewById(R.id.lvSections);
//        lvSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                secInd = position;
//                myRef.child("sections/" + secInd).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        Section section = dataSnapshot.getValue(Section.class);
//                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
//                        dialogBuilder.setTitle(R.string.hoobtn);
//                        LayoutInflater inflater = getLayoutInflater();
//
//                        final View dialogView = inflater.inflate(R.layout.hours_op_dialog, null);
//                        dialogBuilder.setView(dialogView);
//                        final AlertDialog alertDialog = dialogBuilder.create();
//                        alertDialog.show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        Log.e("DATA", "Couldn't read from DB");
//                        // Failed to read value
//                    }
//                });
//                GetSectionDetails gsd = new GetSectionDetails();
//                gsd.execute();
//            }
//        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return false;
    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        // Get store details from database and build the section list
        GetDetails run = new GetDetails();
        run.execute();
    }

    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0){
            // Read from the database
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Restaurant value = dataSnapshot.getValue(Restaurant.class);
                    Log.d("DATA", "Value is: " + value.getName());
                    setTitle(value.getName());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("DATA", "Couldn't read from DB");
                    // Failed to read value
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Restaurant value = dataSnapshot.getValue(Restaurant.class);
                    slist = value.getSections();
                    SectionAdapter adapter = new SectionAdapter(Store_Activity.this, slist);
                    lvSections.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {}
    }

    private class GetSectionDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0){

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {}
    }

    private class AddSection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0){
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Restaurant value = dataSnapshot.getValue(Restaurant.class);
                    Log.d("DATA", "Value is: " + value.getName());
                    value.addSection();
                    myRef.setValue(value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("DATA", "Couldn't read from DB");
                    // Failed to read value
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {}
    }

}