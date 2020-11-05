package com.example.bbqueue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class QueueAdapter extends ArrayAdapter<Customer> {
    Context context;
    public QueueAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        final Activity activity = (Activity) context;
//        // Get the data item for this position
//        Customer article = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_layout, parent, false);
//        }
//
//        // Lookup view for data population
//        TextView tvtitle = convertView.findViewById(R.id.title);
//        TextView tvauthor = convertView.findViewById(R.id.author);
//        TextView tvdate = convertView.findViewById(R.id.date);
//
//        // Populate the data into the template view using the data object
//        tvtitle.setText(String.format("%s", article.getTitle()));
//        tvauthor.setText("By " + article.getAuthor());
//        tvdate.setText("Published: " + article.getPublishDate());
//
////        ImageView imgOnePhoto = convertView.findViewById(R.id.thumbImage);
////        if (article.getPictureUrl() != null) {
////            Log.e("ARTICLE URL: ", article.getPictureUrl());
////            new ImageDownloaderTask(imgOnePhoto).execute(article.getPictureUrl());
////    }
//
//        // Return the completed view to render on screen
        return convertView;
    }
}
