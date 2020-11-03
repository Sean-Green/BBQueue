package com.example.bbqueue;

import android.widget.ArrayAdapter;

public class Queue {
    ArrayAdapter<Customer> customers;

    public ArrayAdapter<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayAdapter<Customer> customers) {
        this.customers = customers;
    }

}
