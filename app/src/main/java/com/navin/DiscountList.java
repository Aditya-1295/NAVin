package com.navin;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.navin.R;

public class DiscountList extends ArrayAdapter {
    private Integer[] imageid;
    private Activity context;

    public DiscountList(Activity context, String[] countryNames, String[] capitalNames, Integer[] imageid) {
        super(context, R.layout.row_item, countryNames);
        this.context = context;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.image);
        
        imageFlag.setImageResource(imageid[position]);
        return  row;
    }
}
