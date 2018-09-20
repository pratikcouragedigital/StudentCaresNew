package com.studentcares.spps.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.studentcares.spps.R;

public class PopupAdapter implements InfoWindowAdapter {

    private View popup = null;
    private LayoutInflater inflater = null;

    public PopupAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = inflater.inflate(R.layout.gps_marker_popup, null);
        }

        TextView txtSnippet = (TextView) popup.findViewById(R.id.snippet);
        TextView txtTitle = (TextView) popup.findViewById(R.id.title);
        View line = (View) popup.findViewById(R.id.view);


        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        txtTitle.setText(title);

        if(title.equals("Home") || title.equals("School")){

            line.setVisibility(View.GONE);
            txtSnippet.setVisibility(View.GONE);
        }
        else{

            txtSnippet.setText(snippet);
            line.setVisibility(View.VISIBLE);
            txtSnippet.setVisibility(View.VISIBLE);
        }

        return (popup);
    }
}