package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
        txtResName = findViewById(R.id.txtResName);
        btnAbandon = findViewById(R.id.btnCancel);
        btnContact = findViewById(R.id.btnContact);
        btnAbandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Abandon ab = new Abandon();
                ab.execute();
            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact();
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
                        btnAbandon.setText(R.string.InQueueBack);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }

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

    private class Back extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            cusRes.child("queueStatus").setValue(false);
            cusRes.child("partySize").setValue(0);
            cusRes.child("timeEnteredQueue").setValue(new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(getApplicationContext(), ResListActivity.class);
                    startActivity(intent);
                }
            });
            return null;
        }
    }

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

    public void contact(){
        resRes.child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue(String.class);
                AlertDialog alertDialog = new AlertDialog.Builder(InQueue.this).create();
                alertDialog.setTitle("Phone No.");
                alertDialog.setMessage(s);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}