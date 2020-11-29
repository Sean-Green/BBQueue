package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class InQueue extends AppCompatActivity {
TextView txtTimeRem;
TextView txtFront;
TextView txtResName;
TextView txtInQueue;
DatabaseReference resRes;
DatabaseReference cusRes;
String uid;
Button btnAbandon;
Button btnContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inqueue);
        txtTimeRem = findViewById(R.id.resTimeRem);
        txtFront = findViewById(R.id.txtFront);
        txtInQueue = findViewById(R.id.txtInQueue);
        txtResName = findViewById(R.id.txtResName);
        btnAbandon = findViewById(R.id.btnCancel);
        btnContact = findViewById(R.id.btnContact);
        btnAbandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(InQueue.this).create();
                alertDialog.setTitle("Abandon Queue?");
                alertDialog.setMessage("You will be abandoning your spot in the current queue");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Abandon ab = new Abandon();
                                ab.execute();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Contact().execute();
            }
        });
        uid = FirebaseAuth.getInstance().getUid();
        String resID = getIntent().getExtras().getString("resID");
        resRes = FirebaseDatabase.getInstance().getReference("Restaurants").child(resID);
        cusRes = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        FillInfo upd = new FillInfo();
        upd.execute();
        Listen listen = new Listen();
        listen.execute();
    }

    //Listens for frontOfQueue value to change to true
    private class Listen extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            cusRes.child("frontOfQueue").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean front = snapshot.getValue(Boolean.class);
                    if(front) {
                        txtTimeRem.setText(R.string.InQueueDone);
                        txtFront.setText("You will be removed from the queue, please speak to the hostess to be seated");
                        txtInQueue.setText(R.string.InQueueFrontOfQueue);
                        btnAbandon.setText(R.string.InQueueBack);
                        btnAbandon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Back back = new Back();
                                back.execute();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }

    //Abandons queue
    private class Abandon extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            cusRes.child("queueStatus").setValue(false);
            cusRes.child("partySize").setValue(0);
            cusRes.child("timeEnteredQueue").setValue(new Date());
            resRes.child("waitList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Customer> cusList = new ArrayList();
                    for(DataSnapshot wait : dataSnapshot.getChildren()){
                        Customer c = wait.getValue(Customer.class);
                        if(!c.getId().equals(uid)){
                            cusList.add(c);
                        }
                    }
                    resRes.child("waitList").setValue(cusList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), ResListActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    //Backs out from queue / position not abandoned
    private class Back extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            cusRes.child("queueStatus").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    cusRes.child("partySize").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            cusRes.child("timeEnteredQueue").setValue(new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getApplicationContext(), ResListActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                }
            });

            return null;
        }
    }

    //Fills page with necessary info of restaurant
    private class FillInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            resRes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Restaurant r = dataSnapshot.getValue(Restaurant.class);
                    txtResName.setText(r.getName());
                    txtTimeRem.setText(getString(R.string.InQueueAvgWait, r.getAvgwait()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    //Contacts the restaurant
    private class Contact extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            resRes.child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String s = snapshot.getValue(String.class);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED){

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + s));
                        startActivity(callIntent);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(InQueue.this).create();
                        alertDialog.setTitle("Phone No: ");
                        alertDialog.setMessage(s + "\n Enable phone in app privileges to have us call for you. :)");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }
}

