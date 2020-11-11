package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SplashLogin extends AppCompatActivity {
    private  FirebaseAuth mAuth;
    private boolean isCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash_login);
    }

    public void login(View view) throws InterruptedException {
        EditText Email = findViewById(R.id.LoginEmail);
        String E = Email.getText().toString().trim();
        EditText Password = findViewById(R.id.LoginPassword);
        String P = Password.getText().toString().trim();
        signIn(E, P);
        TimeUnit.SECONDS.sleep(1);
        getUserCust();
        AccountTypeCheck();
    }

    public void loginStore(View view) throws InterruptedException {
        EditText Email = findViewById(R.id.LoginEmail);
        String E = Email.getText().toString().trim();
        EditText Password = findViewById(R.id.LoginPassword);
        String P = Password.getText().toString().trim();
        signIn(E, P);
        TimeUnit.SECONDS.sleep(1);
        getUserStore();
        TimeUnit.SECONDS.sleep(1);
        Intent intent = new Intent(this, Store_Activity.class);
        intent.putExtra("mAuth", mAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SplashLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    public void AccountTypeCheck(){
       DatabaseReference databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants");

        databaseRes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                    Restaurant r = resSnapshot.getValue(Restaurant.class);
                    isCustomer = true;
                    if(mAuth.getCurrentUser().getUid().equals(r.getResID())){
                        isCustomer = false;
                    }
                }
                if(isCustomer) {
                    Intent intent = new Intent(SplashLogin.this, ResListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashLogin.this, Store_Activity.class);
                    startActivity(intent);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }



    private void getUserCust() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef = myRef.child(mAuth.getCurrentUser().getUid());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Customer value = dataSnapshot.getValue(Customer.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void getUserStore() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef = myRef.child(mAuth.getCurrentUser().getUid());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Restaurant value = dataSnapshot.getValue(Restaurant.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }




    public void register(View view) {
        Intent intent = new Intent(this, CreateAccountSubMenu.class);
        startActivity(intent);
    }

    public void navFrontOfQueue(View view) {
        Intent intent = new Intent(this, Front_Queue.class);
        startActivity(intent);
    }

    public void navResList(View view) {
        Intent intent = new Intent(this, ResListActivity.class);
        startActivity(intent);
    }
    public void navStoreFront(View view) {
        Intent intent = new Intent(this, Store_Activity.class);
        startActivity(intent);
    }
    public void navQueue(View view) {
        Intent intent = new Intent(this, InQueue.class);
        startActivity(intent);
    }
}