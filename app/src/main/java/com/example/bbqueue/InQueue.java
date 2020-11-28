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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inqueue);
        txtTimeRem = findViewById(R.id.resTimeRem);
        txtFront = findViewById(R.id.txtFront);
        txtResName = findViewById(R.id.txtResName);
        final Button btnAbandon = findViewById(R.id.btnCancel);
        Button btnContact = findViewById(R.id.btnContact);
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
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimeRem.setText(getString(R.string.InQueueTimeRemaining, (millisUntilFinished/1000)));
            }
            public void onFinish() {
                txtTimeRem.setText(R.string.InQueueDone);
                txtFront.setText("You will be removed from the queue, please speak to the hostess to be seated");
                btnAbandon.setText(R.string.InQueueBack);
            }
        }.start();

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
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
            resRes.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s = dataSnapshot.getValue(String.class);
                    txtResName.setText(s);
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