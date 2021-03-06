package com.example.bbqueue;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
// ...

public class ResFragment extends DialogFragment {
    private FirebaseAuth mAuth;
    private EditText mEditText;
    private String id;
    private DatabaseReference databaseRes;
    private DatabaseReference custRes;
    private TextView resName;
    private TextView waitTime;
    private TextView address;
    private TextView resNo;

    public ResFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ResFragment newInstance(String id) {
        ResFragment frag = new ResFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.res_fragment, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txtPartySize);
        mAuth = FirebaseAuth.getInstance();
        resName = (TextView) view.findViewById(R.id.txtResName);
        waitTime = (TextView) view.findViewById(R.id.txtQuery);
        address = (TextView) view.findViewById(R.id.txtAddress);
        resNo = (TextView) view.findViewById(R.id.txtPhone);
        final Button resQueue = (Button) view.findViewById(R.id.btnResQueue);
        id = getArguments().getString("id");
        databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants").child(id);
        custRes = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Party Size");
        getDialog().setTitle(title);
        resQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Queue q = new Queue();
                q.execute();
            }
        });
        fillInfo fi = new fillInfo();
        fi.execute();
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    //Queue up
    private class Queue extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            custRes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Customer c = dataSnapshot.getValue(Customer.class);
                    if (!c.isQueueStatus() && !c.isFrontOfQueue()) {
                        if(!mEditText.getText().toString().isEmpty() && Integer.parseInt(mEditText.getText().toString()) > 0) {
                            c.setPartySize(Integer.parseInt(mEditText.getText().toString()));
                            c.setQueueStatus(true);
                            c.setCurRes(id);
                            c.setTimeEnteredQueue(Calendar.getInstance().getTime());
                            custRes.setValue(c);
                            final DatabaseReference waitList = databaseRes.child("waitList");
                            waitList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<Customer> list = new ArrayList<>();
                                    for (DataSnapshot c : dataSnapshot.getChildren()) {
                                        list.add(c.getValue(Customer.class));
                                    }
                                    list.add(c);
                                    waitList.setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(getContext(), InQueue.class);
                                            intent.putExtra("resID", id);
                                            startActivity(intent);
                                            getDialog().dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "Please Enter a valid number",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Taken to previous queue",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), InQueue.class);
                        intent.putExtra("resID", c.getCurRes());
                        startActivity(intent);
                        getDialog().dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    //Fills fragment with data
    private class fillInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            databaseRes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Restaurant res = dataSnapshot.getValue(Restaurant.class);
                    resName.setText(res.getName());
                    waitTime.setText(Integer.toString(res.getAvgwait()));
                    address.setText(res.getAddress());
                    resNo.setText(res.getPhoneNumber());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}