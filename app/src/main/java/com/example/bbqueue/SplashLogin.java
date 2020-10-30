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

public class SplashLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash_login);
    }

    public void login(View view) {
        EditText Email = findViewById(R.id.LoginEmail);
        String E = Email.getText().toString().trim();
        EditText Password = findViewById(R.id.LoginPassword);
        String P = Password.getText().toString().trim();
        signIn(E, P);
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
                            String email = user.getEmail();
                            getToast(user);
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

    private void getToast(FirebaseUser user) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef = myRef.child(mAuth.getCurrentUser().getUid());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                Toast.makeText(SplashLogin.this, value.firstName,
                        Toast.LENGTH_SHORT).show();
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