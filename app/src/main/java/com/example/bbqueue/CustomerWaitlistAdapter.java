package com.example.bbqueue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomerWaitlistAdapter extends ArrayAdapter<Customer> {
    private Activity context;
    private ArrayList<Customer> custList;
    public CustomerWaitlistAdapter(Activity context, ArrayList<Customer> customers) {
        super(context, R.layout.seating_list_item, customers);
        this.context = context;
        custList = customers;
    }
    public View getView(final int position, View convertView, ViewGroup parents){
        Customer c = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seating_list_item, parents, false);
        }

        TextView tvPartyName = convertView.findViewById(R.id.partyName);
        TextView tvPartySize = convertView.findViewById(R.id.partySize);

        tvPartyName.setText(c.getName());
        tvPartySize.setText(c.getPartySize());

        return convertView;
    }
}
