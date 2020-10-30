package com.example.bbqueue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        // Get the data item for this position
        Restaurant res = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reslist_row_layout, parent, false);
        }

        TextView txtResName = convertView.findViewById(R.id.txtResName);
        TextView txtQuery = convertView.findViewById(R.id.txtQuery);
        txtResName.setText(res.getName());
        txtQuery.setText(Integer.toString(res.getWait_time()));
        // Return the completed view to render on screen

        return convertView;
    }

}
