package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResListActivity extends AppCompatActivity {
    ListView lvRes;
    List<Restaurant> reslist;
    DatabaseReference databaseRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_list);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        lvRes = findViewById(R.id.lvRes);
        reslist = new ArrayList<Restaurant>();

    }
    public void navQueue(View view) {
        Intent intent = new Intent(this, InQueue.class);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseRes = FirebaseDatabase.getInstance().getReference("restaurants");
        databaseRes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reslist.clear();
                for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                    String resName = resSnapshot.child("name").getValue(String.class);
                    int wait_time = resSnapshot.child("wait_time").getValue(Integer.class);
                    Restaurant r = new Restaurant(resName, "", "", wait_time);
                    reslist.add(r);
                }
                ResListAdapter adapter = new ResListAdapter(ResListActivity.this, reslist);
                lvRes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
