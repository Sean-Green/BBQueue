package com.example.bbqueue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
//testa@test.ca
//tester
public class Store_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Restaurant curRes;
    private TextView tvName;
    private TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_);
        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getUid();
            getUserStore();
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            // Check if user's email is verified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            }
    }

    public void editStoreClick(View view) {
        Intent intent = new Intent(this, Edit_Store_Details_Activity.class);
        startActivity(intent);
    }

    public void viewQueueClick(View view) {
        Intent intent = new Intent(this, CustQueue.class);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
    }
    private void getUserStore() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants");
        myRef = myRef.child(mAuth.getCurrentUser().getUid());
        try {
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
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    Restaurant value = dataSnapshot.getValue(Restaurant.class);
//                    Log.d("DATA", "Value is: " + value.getName());
//                    setTitle(value.getName());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                }
//            });
        } catch (Exception e) {
            Log.e("DBread", e.toString());
        }


    }

}