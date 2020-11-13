package com.example.bbqueue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
    private Restaurant curRes;
    private TextView tvName;
    private TextView tvAddress;
    private ListView lvSections;
    private Button btnAddSec;

    FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser currentUser;
    ArrayList<Section> slist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_);
        btnAddSec = findViewById(R.id.btnAddSection);
        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        lvSections = findViewById(R.id.lvSections);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid());
        btnAddSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSection as = new AddSection();
                as.execute();
            }
        });
    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        GetDetails run = new GetDetails();
        run.execute();
    }


    public void editStoreClick(View view) {
        Intent intent = new Intent(this, Section_Activity.class);
        startActivity(intent);
    }

    //back button in title
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return false;
    }

    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0){
            getUserStore();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {}
    }


    private void getUserStore() {
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
    }

    private class AddSection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0){
            addSection();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {}
    }

    public void addSection() {
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
    }
}