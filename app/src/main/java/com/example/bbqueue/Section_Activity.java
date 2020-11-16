package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.xml.validation.Validator;

public class Section_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    int sectionIndex;
    private ListView lvTables;
    private ArrayList<Table> tList;

    EditText tblID;
    EditText tblSeat;
    Button addTbl;
    //TODO Delete section


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);



        sectionIndex = getIntent().getExtras().getInt("index");

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(mAuth.getCurrentUser().getUid()).child("sections/" + sectionIndex);

        lvTables = findViewById(R.id.tableListView);
        lvTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Section_Activity.this, "POS: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        tblID = findViewById(R.id.editTableID);
        tblSeat = findViewById(R.id.editTableSeat);

        addTbl = findViewById(R.id.btnAddTable);
        addTbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTable();
            }
        });

    }

    private void addTable() {
        new AddTable().execute();
    }

    public void onStart() {
        super.onStart();
        GetSectionDetails run = new GetSectionDetails();
        run.execute();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
    }


    private class GetSectionDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0){
            // Read from the database
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Section value = dataSnapshot.getValue(Section.class);
                    Log.d("DATA", "Value is: " + value.getId());
                    setTitle(value.getId());
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
                    Section value = dataSnapshot.getValue(Section.class);
                    tList = value.getTables();
                    TableAdapter adapter = new TableAdapter(Section_Activity.this, tList);
                    lvTables.setAdapter(adapter);
                    lvTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(getApplicationContext(), Section_Activity.class);
                            i.putExtra("index", position);
                            getApplicationContext().startActivity(i);
                            Toast.makeText(Section_Activity.this, "POS: " + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
            return null;
        }
    }

    private class AddTable extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (tblID.getText().toString().trim().length() == 0 || tblSeat.getText().toString().trim().length() == 0){

                return null;
            } else {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Section value = dataSnapshot.getValue(Section.class);
                        value.addTable(new Table(tblID.getText().toString(),
                                Integer.parseInt(tblSeat.getText().toString())));
                        myRef.setValue(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return null;
            }

        }
    }
    //    old code for layout inflater, may reuse for table editing
//    public void openHoursDialog(View view) {
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