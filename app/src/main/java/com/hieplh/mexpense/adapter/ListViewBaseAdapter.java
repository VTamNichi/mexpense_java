package com.hieplh.mexpense.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.dtos.Trip;

import java.util.List;

public class ListViewBaseAdapter extends BaseAdapter {

    Context context;
    List<Trip> listTrip;
    LayoutInflater layoutInflater;

    public ListViewBaseAdapter(Context context, List<Trip> listTrip) {
        this.context = context;
        this.listTrip = listTrip;
    }

    @Override
    public int getCount() {
        return listTrip.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0) {
            return null;
        }
        return listTrip.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTrip.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.from(context).inflate(R.layout.list_view, null);
        }

        TextView tripNameView = convertView.findViewById(R.id.trip_name_list_view);
        tripNameView.setText(listTrip.get(position).getTripName());

        TextView destinationView = convertView.findViewById(R.id.destination_list_view);
        destinationView.setText(listTrip.get(position).getDestination());

        TextView tripDateView = convertView.findViewById(R.id.date_list_view);
        tripDateView.setText(listTrip.get(position).getTripDate());

        return convertView;
    }
}
