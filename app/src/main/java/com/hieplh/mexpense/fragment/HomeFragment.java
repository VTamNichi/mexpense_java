package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        Bundle bundle = new Bundle();
        AppCompatButton newTripBtn = view.findViewById(R.id.new_trip_btn);
        newTripBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "new_trip");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        AppCompatButton tripListBtn = view.findViewById(R.id.trip_list_btn);
        tripListBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "trip_list");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        AppCompatButton aboutBtn = view.findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "about");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        AppCompatButton resetBtn = view.findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "reset");
            getFragmentManager().setFragmentResult("101", bundle);
        });

        AppCompatButton backupBtn = view.findViewById(R.id.backup_btn);
        backupBtn.setOnClickListener(v -> {
            bundle.putCharSequence("1", "backup");
            getFragmentManager().setFragmentResult("101", bundle);
        });
        return view;
    }
}
