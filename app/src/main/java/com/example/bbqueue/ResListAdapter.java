package com.example.bbqueue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class ResListAdapter extends ArrayAdapter<Restaurant> {
    private Activity context;
    private List<Restaurant> reslist;

    public ResListAdapter(Activity context, List<Restaurant> reslist) {
        super(context, R.layout.activity_res_list, reslist);
        this.context = context;
        this.reslist = reslist;
    }

    public ResListAdapter(Context context, int resource, List<Restaurant> objects, Activity context1, List<Restaurant> reslist) {
        super(context, resource, objects);
        this.context = context1;
        this.reslist = reslist;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_res_list, null, true);



        return listViewItem;
    }

}
