package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        Bundle bundle = new Bundle();
        CardView newTripBtn = view.findViewById(R.id.new_trip_btn);
        newTripBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "new_trip");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        CardView tripListBtn = view.findViewById(R.id.trip_list_btn);
        tripListBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "trip_list");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        CardView aboutBtn = view.findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "about");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        CardView resetBtn = view.findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "reset");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        CardView backupBtn = view.findViewById(R.id.backup_btn);
        backupBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "backup");
            getFragmentManager().setFragmentResult("101", bundle);
        });
        return view;
    }
}
