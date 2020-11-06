package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

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
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_list);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        searchView = findViewById(R.id.searchView);
        lvRes = findViewById(R.id.lvRes);
        reslist = new ArrayList<Restaurant>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants");
                if(query.isEmpty()){
                    refreshResList();
                    return false;
                }
                databaseRes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reslist.clear();
                        for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                            Restaurant r = resSnapshot.getValue(Restaurant.class);
                            System.out.println(r.getName() + query + (r.getName() == query));
                            if(r.getName().equalsIgnoreCase(query)) {
                                reslist.add(r);
                            }
                        }
                        ResListAdapter adapter = new ResListAdapter(ResListActivity.this, reslist);
                        lvRes.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    public void toQueue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Party Size");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_text = input.getText().toString();
                Intent intent = new Intent(getApplicationContext(), InQueue.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
        refreshResList();
    }
    private void refreshResList(){
        databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants");
        databaseRes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reslist.clear();
                for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                    Restaurant r = resSnapshot.getValue(Restaurant.class);
                    System.out.println(resSnapshot.toString());
                    System.out.println(r.toString());
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
