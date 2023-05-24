package com.example.firedrive;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapter extends ArrayAdapter<ListCommande> {
    public MyAdapter(Context context, List<ListCommande> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        ListCommande currentItem = getItem(position);

        if (currentItem != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(currentItem.getChaineCommande());
            int color;
            if (currentItem.getFinished() == 1) {
                color = Color.rgb(153, 255, 153); // Vert
            } else if(currentItem.getFinished() == 0){
                color = Color.rgb(255, 102, 102); // Rouge
            }else{
                color = Color.rgb(200, 200, 200); // gris
            }
            int radiusInDp = 20; // Rayon des coins arrondis en dp
            int radiusInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusInDp, convertView.getResources().getDisplayMetrics());
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(color);
            shape.setCornerRadius(radiusInPx);
            convertView.setBackground(shape);

        }

        return convertView;
    }
}
