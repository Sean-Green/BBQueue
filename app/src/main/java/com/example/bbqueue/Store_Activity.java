package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

//testa@test.ca
//tester
public class Store_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ListView lvSections;
    private Button btnAddSec;
    private Button editInfo;

    private int secInd;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser currentUser;
    ArrayList<Section> slist;
    TextView storeName;
    TextView storeAddress;
    TextView curQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_);

        //Get DB ref
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid());

        // We could have removed this button, but it really throws off the feng shui of our app
        editInfo = findViewById(R.id.editInfo);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.ftpend), Toast.LENGTH_SHORT).show();
            }
        });

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
        lvSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secInd = position;
                new UpdateSection().execute();
            }
        });
        GetSectionDetails gsd = new GetSectionDetails();
        gsd.execute();

    }

    private class UpdateSection extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            myRef.child("sections").child(Integer.toString(secInd)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Section section = dataSnapshot.getValue(Section.class);
                    showUpdateDialog(section, secInd);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("DATA", "Couldn't read from DB");
                    // Failed to read value
                }
            });
            return null;
        }
    }

    private void showUpdateDialog(final Section section, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText name = dialogView.findViewById(R.id.editTextSectionId);


        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Update Section");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sys = name.getText().toString().trim();

                if (TextUtils.isEmpty(sys)) {
                    name.setError("First Name is required");
                    return;
                }
                updateReading(section, sys, position);
                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReading(position);
                alertDialog.dismiss();
            }
        });

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
                    slist = value.getSections();
                    storeName = findViewById(R.id.storeName);
                    storeName.setText(value.getName());
                    storeAddress = findViewById(R.id.storeAddress);
                    storeAddress.setText(value.getAddress());
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

                    curQueue = findViewById(R.id.custNum);
                    int i = value.getWaitList().size() - 1;
                    curQueue.setText(Integer.toString(i));
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





    private void updateReading(Section section, String name, int position) {

        DatabaseReference updateDbRef = myRef.child("sections").child(Integer.toString(position));
        section.setId(name);
        updateDbRef.setValue(section);

    }

    private void deleteReading(final int position) {
        final ArrayList<Section> newSections = new ArrayList<>();

        final DatabaseReference deleteDbRef =myRef.child("sections");

        deleteDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int count = 0;
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Section test = d.getValue(Section.class);
                    count++;
                    newSections.add(test);

                }
                if(count > 1) {
                    newSections.remove(position);
                    deleteDbRef.setValue(newSections);
                } else {
                    Toast.makeText(getApplicationContext(), "Restaurants must have atleast 1 section", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("DATA", "Couldn't read from DB");
                // Failed to read value
            }
        });

    }
}