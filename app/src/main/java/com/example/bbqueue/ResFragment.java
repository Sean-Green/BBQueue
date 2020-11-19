package com.example.bbqueue;

import android.content.Intent;
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
        final TextView resName = (TextView) view.findViewById(R.id.txtResName);
        final TextView waitTime = (TextView) view.findViewById(R.id.txtQuery);
        final TextView address = (TextView) view.findViewById(R.id.txtAddress);
        final TextView resNo = (TextView) view.findViewById(R.id.txtPhone);
        final Button resQueue = (Button) view.findViewById(R.id.btnResQueue);
        final String id = getArguments().getString("id");
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Party Size");
        getDialog().setTitle(title);
        final DatabaseReference databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants").child(id);

        resQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wow");
                databaseRes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Restaurant res = dataSnapshot.getValue(Restaurant.class);
                        final String uid = mAuth.getCurrentUser().getUid();
                        System.out.println(uid);
                        final DatabaseReference userRes = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                        userRes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Customer cust = dataSnapshot.getValue(Customer.class);
                                Date currentTime = Calendar.getInstance().getTime();
                                cust.setTimeEnteredQueue(currentTime);
                                int partySize = Integer.parseInt(mEditText.getText().toString());
                                cust.setPartySize(partySize);
                                cust.setQueueStatus(true);
                                userRes.setValue(cust).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ArrayList<Customer> cList = res.getWaitList();
                                        cList.add(cust);
                                        databaseRes.child("waitList").setValue(cList);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                Intent intent = new Intent(getContext(), InQueue.class);
//                intent.putExtra("id",id);
//                startActivity(intent);

            }
        });

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


        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}