package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.dtos.Trip;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        Bundle bundle = new Bundle();

        EditText text_date = view.findViewById(R.id.text_date);
        text_date.setOnClickListener(v -> {
            bundle.putCharSequence("1", "date");
            getFragmentManager().setFragmentResult("102", bundle);
        });

        Button createBtn = view.findViewById(R.id.btn_create);
        createBtn.setOnClickListener(v -> {
            EditText text_trip_name = view.findViewById(R.id.text_trip_name);
            EditText text_destination = view.findViewById(R.id.text_destination);
            EditText text_description = view.findViewById(R.id.text_description);

            Switch switch_btn = view.findViewById(R.id.switch_btn);
            int risk_assessment = 0;
            if(switch_btn.isChecked()) {
                risk_assessment = 1;
            }
            EditText text_location = view.findViewById(R.id.text_location);
            EditText text_status = view.findViewById(R.id.text_status);

            Trip trip = new Trip();
            boolean flag = false;
            if(text_trip_name.getText().length() == 0) {
                text_trip_name.setError("Please fill out this field");
            } else if (text_destination.getText().length() == 0) {
                text_destination.setError("Please fill out this field");
            } else if (text_date.getText().length() == 0) {
                text_date.setError("Please fill out this field");
            } else {
                trip.setTripName(text_trip_name.getText().toString());
                trip.setDestination(text_destination.getText().toString());
                trip.setTripDate(text_date.getText().toString());
                trip.setDescription(text_description.getText().toString());
                trip.setRiskAssessment(risk_assessment);
                trip.setLocation(text_location.getText().toString());
                trip.setStatus(text_status.getText().toString());
                flag = true;
            }

            if(flag) {
                bundle.putCharSequence("1", trip.toString());
            } else {
                bundle.putCharSequence("1", "");
            }
            getFragmentManager().setFragmentResult("102", bundle);
        });

        return view;
    }
}
