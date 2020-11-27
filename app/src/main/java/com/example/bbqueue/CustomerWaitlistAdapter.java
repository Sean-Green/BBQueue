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
    private int tableLimit;
    public CustomerWaitlistAdapter(Activity context, ArrayList<Customer> customers, int tableLimit) {
        super(context, R.layout.seating_list_item, customers);
        this.context = context;
        custList = customers;
        this.tableLimit = tableLimit;
    }
    public View getView(final int position, View convertView, ViewGroup parents){
        Customer c = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seating_list_item, parents, false);
        }

            TextView tvPartyName = convertView.findViewById(R.id.partyName);
            TextView tvPhoneNum = convertView.findViewById(R.id.partyNumber);
            TextView tvPartySize = convertView.findViewById(R.id.partySize);

            tvPartyName.setText(c.getName());
            tvPartySize.setText((c.getPartySize() + ""));
            tvPhoneNum.setText((c.getPhoneNumber()));
//        this sets everything invisible for some reason, including customers that dont trigger it
//        if (c.getPartySize() > tableLimit) {
//            convertView.setVisibility(View.INVISIBLE);
//            convertView.setClickable(false);
//            convertView.setMinimumHeight(0);
//        }

        return convertView;
    }
}
