package com.example.bbqueue;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class SectionAdapter extends ArrayAdapter<SectionAdapter> {
    private Context context;
    public SectionAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }
}
