package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Section_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference restRef;
    private DatabaseReference sectionRef;
    private DatabaseReference custRef;

    private int tableLimit;
    private int sectionIndex;
    private ListView lvTables;
    private ListView lvSeats;
    private ArrayList<Table> tList;
    private ArrayList<Customer> waitList;

    EditText tblID;
    EditText tblSeat;
    Button addTbl;
    //TODO Delete section

    ValueEventListener updDetails =  new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            tList = new ArrayList<>();
            Section value = dataSnapshot.getValue(Section.class);
            tList = value.getTables();
            TableAdapter adapter = new TableAdapter(Section_Activity.this, tList);
            lvTables.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.e(getString(R.string.updTableTag), "onCancelled: Unable to update tables", null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        sectionIndex = getIntent().getExtras().getInt(getString(R.string.index));
        tableLimit = 1;

        mAuth = FirebaseAuth.getInstance();
        restRef = FirebaseDatabase.getInstance().getReference(getString(R.string.r_path))
                .child(mAuth.getCurrentUser().getUid());
        sectionRef = FirebaseDatabase.getInstance().getReference(getString(R.string.r_path)).child(
                mAuth.getCurrentUser().getUid()).child(getString(R.string.s_path) + sectionIndex);
        custRef = FirebaseDatabase.getInstance().getReference(getString(R.string.u_path));


        lvTables = findViewById(R.id.tableListView);
        tblID = findViewById(R.id.editTableID);
        tblSeat = findViewById(R.id.editTableSeat);

        addTbl = findViewById(R.id.btnAddTable);
        addTbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTable().execute();
            }
        });

        lvTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //TODO Seat customers
                if(tList.get(position).isOpen()){
                    seatTableDialog(position);
                } else {
                    AlertDialog.Builder crisis = new AlertDialog.Builder(Section_Activity.this);
                    crisis.setIcon(R.mipmap.ic_launcher);
                    crisis.setTitle(R.string.tableAlertHint);
                    crisis.setMessage(R.string.openTblWarning);

                    crisis.setPositiveButton(getString(R.string.open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new OpenTable(position).execute();
                            dialog.dismiss();
                        }
                    });
                    crisis.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    crisis.show();
                }
            }

            private void openTableDialog() {

            }
        });

        lvTables.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editTableDialog(position);
                return true;
            }
        });

    }

    public void onStart() {
        super.onStart();
        GetSectionDetails run = new GetSectionDetails();
        run.execute();
    }

    @Override
    public void onBackPressed() {
        sectionRef.removeEventListener(updDetails);
        finish();
    }

    private class GetSectionDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0){
            sectionRef.addValueEventListener(updDetails);

            restRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    waitList = new ArrayList<Customer>();
                    Restaurant value = dataSnapshot.getValue(Restaurant.class);
                    waitList = value.getWaitList();
                    waitList.remove(0);
                    if (lvSeats != null) {
                        final CustomerWaitlistAdapter wlAdapt = new CustomerWaitlistAdapter(Section_Activity.this, waitList);
                        lvSeats.setAdapter(wlAdapt);
                    }
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
                sectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Section value = dataSnapshot.getValue(Section.class);
                        value.addTable(new Table(tblID.getText().toString(),
                                Integer.parseInt(tblSeat.getText().toString())));
                        sectionRef.setValue(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return null;
            }

        }
    }

    private class RemoveTable extends AsyncTask<Void, Void, Void> {
        int position;
        public RemoveTable(int i){
            super();
            position = i;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            sectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Section thisSection = dataSnapshot.getValue(Section.class);
                    if (thisSection.removeTableAtIndex(position)) {
                        Task remove = sectionRef.setValue(thisSection);
                        remove.addOnSuccessListener(new OnSuccessListener() {

                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(Section_Activity.this, "Table deleted.", Toast.LENGTH_LONG).show();

                            }
                        });
                        remove.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Section_Activity.this,
                                        "something went wrong, please try again.\n" + e.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(Section_Activity.this,
                                "Cannot delete last table in a section",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }


    private void seatTableDialog(int i) {
        final int position = i;
        tableLimit = tList.get(position).getSize();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.seating_list_layout, null);
        dialogBuilder.setView(dialogView);

        lvSeats = dialogView.findViewById(R.id.lvSeatingList);

        final AlertDialog waitDialog = dialogBuilder.create();
        waitDialog.show();

        ArrayList<Customer> tempCustList = waitList;
        final CustomerWaitlistAdapter wlAdapt = new CustomerWaitlistAdapter(Section_Activity.this, tempCustList);
        lvSeats.setAdapter(wlAdapt);
        lvSeats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                Table t = tList.get(position);
                t.setCustomer(waitList.get(p).getId());
                t.setOpen(false);
                new SeatTableRemoveCustomer(t, position, (p+1)).execute();
                waitDialog.dismiss();
            }
        });
    }

    public void editTableDialog(int i) {
        final int position = i;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.edit_table);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.edit_table_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText etCurrentName = dialogView.findViewById(R.id.currentName);
        etCurrentName.setHint(tList.get(i).getTableID());

        final EditText etCurrentSeats =dialogView.findViewById(R.id.currentSeats);
        etCurrentSeats.setHint(tList.get(i).getSizeString());

        final AlertDialog alertDialog = dialogBuilder.create();

        final Button cancel = dialogView.findViewById(R.id.editTblBtnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        final Button delete = dialogView.findViewById(R.id.editTblBtnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OpenTable(position).execute();
                new RemoveTable(position).execute();
                alertDialog.dismiss();
            }
        });

        final Button update = dialogView.findViewById(R.id.editTblBtnSave);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean f1 = false, f2 = false;
                String newTableName = etCurrentName.getText().toString().trim();
                if (newTableName.length() < 1) {
                    newTableName = etCurrentName.getHint().toString();
                    f1 = true;
                    return;
                }
                int newSeats;
                String newTableSeats = etCurrentSeats.getText().toString().trim();
                if (newTableSeats.length() < 1) {
                    newSeats = Integer.parseInt("" + etCurrentSeats.getHint().toString().charAt(0));
                    f2 = true;
                } else {
                    newSeats = Integer.parseInt(newTableSeats);
                }
                if (f1 && f2) {
                    Toast.makeText(getApplicationContext(), "No new information", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new UpdateTable(position, newTableName, newSeats).execute();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private class UpdateTable extends AsyncTask<Void, Void, Void>{
        int position;
        String name;
        int seats;
        public UpdateTable(int i, String n, int s){
            super();
            position = i;
            name = n;
            seats = s;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Table upd = new Table(name, seats);
            Task setTable = sectionRef.child("tables/"+ position).setValue(upd);
            setTable.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("UPDATING", "onSuccess: updated");
                }
            });
            setTable.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("UPDATING", "onFailure: failed");
                }
            });
            return null;
        }
    }

    private class SeatTableRemoveCustomer extends AsyncTask<Void, Void, Void>{
        Table t;
        int tPos;
        int cPos;
        public SeatTableRemoveCustomer(Table table, int tablePos, int custPos){
            super();
            t = table;
            tPos = tablePos;
            cPos = custPos;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Task setTable = sectionRef.child("tables/"+ tPos).setValue(t);
            setTable.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("UPDATING", "onSuccess: table seated");
                }
            });
            setTable.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("UPDATING", "onFailure: table not seated");
                }
            });
            String custId = waitList.get(cPos - 1).getId();
            Task frontOfQueue = custRef.child(custId + "/frontOfQueue").setValue(true);
            Task setQueueStatus = custRef.child(custId + "/queueStatus").setValue(false);
            waitList.add(new Customer("Default customer, every list has one"));
            waitList.sort(null);
            waitList.remove(cPos);
            Task removeCustomer = restRef.child("waitList").setValue(waitList);
            removeCustomer.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("UPDATING", "onSuccess: customer removed");
                }
            });
            removeCustomer.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("UPDATING", "onFailure: customer not removed");
                }
            });

            return null;
        }
    }

    private class OpenTable extends AsyncTask<Void, Void, Void>{
        private int tPos;
        public OpenTable(int position) {
            super();
            tPos = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Table t = tList.get(tPos);
            String custId = t.getCustomer();
            Task remove = custRef.child(custId + "/frontOfQueue").setValue(false);
            Task setQueueStatus = custRef.child(custId + "/queueStatus").setValue(false);
            t.setOpen(true);
            t.setCustomer(null);
            Task openTable = sectionRef.child("tables/" + tPos).setValue(t);
            return null;
        }
    }
}