package com.example.arbeitszeitenerfassung;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ListView_Spaltenadapter extends ArrayAdapter<Arbeitszeit>{

    private ArrayList<Arbeitszeit> arbeitszeitArrayList;
    private LayoutInflater mInflater;
    private int mViewResourceId;
    private Context mContext;


    public ListView_Spaltenadapter( Context context, int textViewResourceId, ArrayList<Arbeitszeit> arbeitszeitArrayList) {
        super(context, textViewResourceId, arbeitszeitArrayList);
        this.mContext = context;
        this.arbeitszeitArrayList = arbeitszeitArrayList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);
        Arbeitszeit arbeitszeit_Object = arbeitszeitArrayList.get(position);

        if (position % 2 == 0) {
            int graugruen = ContextCompat.getColor(mContext, R.color.graugruen);

            convertView.setBackgroundColor(graugruen);
        } else {
            //convertView.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_yellow));
        }

        if (arbeitszeit_Object != null) {
            TextView arbeitszeitTag_txt = (TextView) convertView.findViewById(R.id.day_TextView);
            TextView arbeitszeitBeginn_txt = (TextView) convertView.findViewById(R.id.arbeitenvon_TextView);
            TextView arbeitszeitStart_txt = (TextView) convertView.findViewById(R.id.arbeitenbis_TextView);
            TextView arbeitszeitTagDauer_txt = (TextView) convertView.findViewById(R.id.arbeitsdauer_TextView);

            if (arbeitszeitTag_txt != null) {
                arbeitszeitTag_txt.setText(arbeitszeit_Object.getDatum().toString());
            }
            if (arbeitszeitBeginn_txt != null) {
                arbeitszeitBeginn_txt.setText(arbeitszeit_Object.getArbeitszeitstart().toString());
            }
            if (arbeitszeitStart_txt != null) {
                arbeitszeitStart_txt.setText(arbeitszeit_Object.getArbeitszeitende().toString());
            }
            if (arbeitszeitTagDauer_txt != null) {
                arbeitszeitTagDauer_txt.setText(arbeitszeit_Object.getTagesarbeitDauer().toString());
            }
        }
        //arbeitszeitArrayList.add(arbeitszeit_Object);

        return convertView;
    }
}
