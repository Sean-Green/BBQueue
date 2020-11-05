package com.example.bbqueue;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class WaitList {

    private String id = "queue";
    private ArrayList<Customer> queue;

    public WaitList() {
        queue = new ArrayList<Customer>();
        queue.add(new Customer());
    }

    public void addCustomer(Customer c){
        queue.add(c);
        Log.e("Queue", "Customer " + c.getId() + " added to queue");

    }

    // this probably won't work as is, but we'll cross that bridge when we come to it
    public void removeCustomer(Customer c){
        Iterator<Customer> cit = queue.iterator();
        while (cit.hasNext()){
            if (cit.next().getId() == c.getId()){
                cit.remove();
                Log.e("Queue", "Customer " + c.getId() + " removed from queue");

                return;
            }
        }
        Log.e("Queue", "Customer " + c.getId() + " not in queue");
    }
}
