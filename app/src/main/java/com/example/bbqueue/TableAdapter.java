package com.example.bbqueue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

//import static androidx.core.graphics.drawable.IconCompat.getResources;

public class TableAdapter extends ArrayAdapter<Table> {
    private Activity context;
    private ArrayList<Table> tableList;

    public TableAdapter(Activity context, ArrayList<Table> resource) {
        super(context, R.layout.table_list_layout, resource);
        this.context = context;
        tableList = resource;
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int position, View convertView, ViewGroup parent){

        Table t = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.table_list_layout, parent, false);
        }

        TextView tableName = convertView.findViewById(R.id.tableName);
        TextView tableSize = convertView.findViewById(R.id.tableSize);
        TextView tableStatus = convertView.findViewById(R.id.tableStatus);

        tableName.setText(t.getTableID());
        tableSize.setText(t.getSizeString());
        if (t.isOpen()) {
            tableStatus.setText(R.string.opencaps);
            tableStatus.setBackgroundColor(getContext().getResources().getColor(R.color.Green));
        } else {
            tableStatus.setText(R.string.occuCaps);
            tableStatus.setBackgroundColor(getContext().getResources().getColor(R.color.FireBrick));
        }
        getContext();
        return convertView;
    }
}
