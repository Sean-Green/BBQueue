package com.example.bbqueue;

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

public class SectionAdapter extends ArrayAdapter<Section> {
    public SectionAdapter(@NonNull Context context, ArrayList<Section> resource) {
        super(context, 0, resource);
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        Section s = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.section_list_layout, parent, false);
        }

        Button txtSecName = convertView.findViewById(R.id.tvSectionName);
        TextView txtTableCount = convertView.findViewById(R.id.tvTables);

        txtSecName.setText(s.getId());
        txtTableCount.setText(s.getOpenTableString());

//        txtSecName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), Section_Activity.class);
//                i.putExtra("index", position);
//                getContext().startActivity(i);
//            }
//        });
        return convertView;
    }

}
