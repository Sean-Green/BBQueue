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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class CustomerAccountCreate extends AppCompatActivity {
   private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle i = getIntent().getExtras();
        int type = i.getInt(getString(R.string.type));
        if(type == 1) {
            setContentView(R.layout.activity_customer_account_create);
        } else {
            setContentView(R.layout.res_account_create);
        }
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void OnCreateAccount(View view) throws InterruptedException {
        EditText Email = findViewById(R.id.CustEmail);
        String E = Email.getText().toString().trim();
        EditText Password = findViewById(R.id.CustPassword);
        String P = Password.getText().toString().trim();
        CreateAccount(E, P);
        TimeUnit.SECONDS.sleep(1);
        RTDCreate();
        Intent intent = new Intent(this, SplashLogin.class);
        startActivity(intent);
    }


    public void CreateAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CustomerAccountCreate.this, getString(R.string.verisent), Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CustomerAccountCreate.this, getString(R.string.veriNotSent), Toast.LENGTH_LONG).show();
                                }
                            });


                            Toast.makeText(CustomerAccountCreate.this, getString(R.string.auth_succ),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CustomerAccountCreate.this, getString(R.string.auth_fail),
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    private void RTDCreate() {
        EditText Email = findViewById(R.id.CustEmail);
        String E = Email.getText().toString().trim();
        EditText FirstName = findViewById(R.id.CustFirstName);
        String FN = FirstName.getText().toString();
        EditText Address = findViewById(R.id.CustAddress);
        String Add = Address.getText().toString();
        EditText Phone = findViewById(R.id.CustPhone);
        String PhoneNum = Phone.getText().toString();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        Customer u = new Customer(mAuth.getCurrentUser().getUid(), FN, PhoneNum, E, Add);

        myRef.child(getString(R.string.users)).child(mAuth.getCurrentUser().getUid()).setValue(u);
    }

    public void OnCreateAccountRes(View view) throws InterruptedException {
        EditText Email = findViewById(R.id.ResEmail);
        String E = Email.getText().toString().trim();
        EditText Password = findViewById(R.id.ResPassword);
        String P = Password.getText().toString().trim();
        CreateAccount(E, P);
        TimeUnit.SECONDS.sleep(1);
        RTDCreateRes();
        Intent intent = new Intent(this, SplashLogin.class);
        startActivity(intent);
    }

    private void RTDCreateRes() throws InterruptedException {
        EditText Email = findViewById(R.id.ResEmail);
        String E = Email.getText().toString().trim();
        EditText FirstName = findViewById(R.id.ResName);
        String FN = FirstName.getText().toString();
        EditText Address = findViewById(R.id.ResAddress);
        String Add = Address.getText().toString();
        EditText Phone = findViewById(R.id.ResPhone);
        String PhoneNum = Phone.getText().toString();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Restaurant u = new Restaurant(FN, Add, PhoneNum, mAuth.getCurrentUser().getUid());
        Log.e(getString(R.string.resInfo), u.name);
        TimeUnit.SECONDS.sleep(1);
        myRef.child(getString(R.string.r_path)).child(mAuth.getCurrentUser().getUid()).setValue(u);
    }

}