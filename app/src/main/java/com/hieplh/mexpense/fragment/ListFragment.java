package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.adapter.ListViewBaseAdapter;
import com.hieplh.mexpense.daos.TripDAO;
import com.hieplh.mexpense.dtos.Trip;

import java.util.List;

public class ListFragment extends Fragment {

    String tripName;
    String destination;
    String tripDate;
    String location;
    String status;
    boolean textFilter;

    public ListFragment(String tripName, String destination, String tripDate, String location, String status, boolean textFilter) {
        this.tripName = tripName;
        this.destination = destination;
        this.tripDate = tripDate;
        this.location = location;
        this.status = status;
        this.textFilter = textFilter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Bundle bundle = new Bundle();

        TripDAO tripDAO = new TripDAO(view.getContext());

        List<Trip> listTrip = tripDAO.getListTrip(tripName, destination, tripDate, location, status, textFilter);

        LinearLayout ll = view.findViewById(R.id.no_list_view);
        if(listTrip.size() == 0) {
            ll.setVisibility(View.VISIBLE);
        } else {
            ll.setVisibility(View.GONE);
        }

        ListView listView = view.findViewById(R.id.list_view);
        ListViewBaseAdapter listViewBaseAdapter = new ListViewBaseAdapter(view.getContext(), listTrip);
        listView.setAdapter(listViewBaseAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            bundle.putCharSequence("1", listTrip.get(position).toString());
            getFragmentManager().setFragmentResult("103", bundle);
        });

        EditText text_filter = view.findViewById(R.id.text_filter);
        text_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Trip> listTripChange = tripDAO.getListTrip(s.toString(), s.toString(), s.toString(), s.toString(), s.toString(), true);
                ListViewBaseAdapter listViewBaseAdapterChange = new ListViewBaseAdapter(view.getContext(), listTripChange);
                listView.setAdapter(listViewBaseAdapterChange);
            }
        });

        Button btn_filter = view.findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(v -> {
            bundle.putCharSequence("1", "filter");
            getFragmentManager().setFragmentResult("103", bundle);
        });

        Button btn_refresh = view.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(v -> {
            bundle.putCharSequence("1", "refresh");
            getFragmentManager().setFragmentResult("103", bundle);
        });

        return view;
    }
}
