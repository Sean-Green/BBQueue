package com.example.bbqueue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//import static androidx.core.graphics.drawable.IconCompat.getResources;

public class TableAdapter extends ArrayAdapter<Table> {
    private Activity context;
    private ArrayList<Table> tableList;

    public TableAdapter(Activity context, ArrayList<Table> resource) {
        super(context, R.layout.table_list_item, resource);
        this.context = context;
        tableList = resource;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @SuppressLint("SetTextI18n")
    public View getView(final int position, View convertView, ViewGroup parent){

        Table t = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.table_list_item, parent, false);
        }

        LinearLayout table = convertView.findViewById(R.id.table);
        TextView tableName = convertView.findViewById(R.id.tableName);
        TextView tableSize = convertView.findViewById(R.id.tableSize);
        TextView tableStatus = convertView.findViewById(R.id.tableStatus);

        tableName.setText(t.getTableID());
        tableSize.setText(t.getSizeString());
        if (t.isOpen()) {
            tableStatus.setText(R.string.seatTable);
            table.setBackgroundColor(getContext().getResources().getColor(R.color.openTable));
        } else {
            tableStatus.setText(R.string.openTable);
            table.setBackgroundColor(getContext().getResources().getColor(R.color.satTable));
        }
        getContext();
        return convertView;
    }
}
