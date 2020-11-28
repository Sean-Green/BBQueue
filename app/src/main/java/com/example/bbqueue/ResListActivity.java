package com.example.bbqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_res_list);
        lvRes = findViewById(R.id.lvRes);
        reslist = new ArrayList<Restaurant>();
        databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants");
    }

    public void showEditDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ResFragment resFragment = ResFragment.newInstance((String) view.getTag());
        resFragment.show(fm, "res_fragment");
    }

    @Override
    protected void onStart() {
        super.onStart();
        RefreshList rs = new RefreshList();
        rs.execute();
    }



    private class RefreshList extends AsyncTask <Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
                databaseRes = FirebaseDatabase.getInstance().getReference("Restaurants");
                databaseRes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reslist.clear();
                        for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                            Restaurant r = resSnapshot.getValue(Restaurant.class);
                            reslist.add(r);
                        }
                        ResListAdapter adapter = new ResListAdapter(ResListActivity.this, reslist);
                        lvRes.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            return null;
        }
    }

    private class SearchList extends AsyncTask <String, Void, Void>{
        @Override
        protected Void doInBackground(final String... strings) {
            databaseRes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reslist.clear();
                    for (DataSnapshot resSnapshot : dataSnapshot.getChildren()) {
                        Restaurant r = resSnapshot.getValue(Restaurant.class);
                        if(r.getName().toLowerCase().contains(strings[0].toLowerCase())) {
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
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if(searchView.isFocused() && TextUtils.isEmpty(newText)){
                    RefreshList rs = new RefreshList();
                    rs.execute();
                }
                SearchList sl = new SearchList();
                sl.execute(newText);
                return true;
            }
        });
        return true;
    }
}
