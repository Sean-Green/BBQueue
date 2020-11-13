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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        String id = getArguments().getString("id");
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Party Size");
        getDialog().setTitle(title);

        final DatabaseReference databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants").child(id);
        databaseRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant res = dataSnapshot.getValue(Restaurant.class);
                resName.setText(res.getName());
                waitTime.setText(Integer.toString(res.getAvgwait()));
                address.setText(res.getAddress());
                resNo.setText(res.getPhoneNumber());
                resQueue.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        databaseRes.child("waitList").push().setValue(mAuth.getCurrentUser().getUid());
                        Intent intent = new Intent(getContext(), InQueue.class);
                        intent.putExtra("id", getArguments().getString("id"));
                        startActivity(intent);
                    }
                });
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