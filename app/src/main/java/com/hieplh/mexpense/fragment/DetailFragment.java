package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.adapter.ListViewExpenseBaseAdapter;
import com.hieplh.mexpense.daos.ExpenseDAO;
import com.hieplh.mexpense.dtos.Expense;
import com.hieplh.mexpense.dtos.Trip;

import java.util.List;

public class DetailFragment extends Fragment {

    Trip trip;
    public DetailFragment(Trip trip) {
        this.trip = trip;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        Bundle bundle = new Bundle();

        ExpenseDAO expenseDAO = new ExpenseDAO(view.getContext());

        List<Expense> listExpense = expenseDAO.getListExpense(trip.getId());
        if(listExpense.size() > 0) {
            String dateHeader = listExpense.get(0).getDate();
            listExpense.get(0).setHeader(true);
            for (int i = 1; i < listExpense.size(); i++) {
                if(listExpense.get(i).getDate().equals(dateHeader)) {
                    listExpense.get(i).setHeader(false);
                } else {
                    dateHeader = listExpense.get(i).getDate();
                    listExpense.get(i).setHeader(true);
                }
            }
        }

        ListView listViewExpense = view.findViewById(R.id.list_view_expense);
        ListViewExpenseBaseAdapter listViewExpenseBaseAdapter = new ListViewExpenseBaseAdapter(view.getContext(), listExpense);
        listViewExpense.setAdapter(listViewExpenseBaseAdapter);

        TextView id_detail = view.findViewById(R.id.id_detail);
        id_detail.setText(String.valueOf(trip.getId()));

        TextView trip_name_detail = view.findViewById(R.id.trip_name_detail);
        trip_name_detail.setText(trip.getTripName());

        TextView destination_detail = view.findViewById(R.id.destination_detail);
        destination_detail.setText(trip.getDestination());

        TextView date_detail = view.findViewById(R.id.date_detail);
        date_detail.setText(trip.getTripDate());

        TextView description_detail = view.findViewById(R.id.description_detail);
        description_detail.setText(trip.getDescription());

        TextView risk_assessment_detail = view.findViewById(R.id.risk_assessment_detail);
        String riskA = "No";
        if(trip.getRiskAssessment() == 1) {
            riskA = "Yes";
        }
        risk_assessment_detail.setText(riskA);

        TextView location_detail = view.findViewById(R.id.location_detail);
        location_detail.setText(trip.getLocation());

        TextView status_detail = view.findViewById(R.id.status_detail);
        status_detail.setText(trip.getStatus());

        Button btn_add_expense = view.findViewById(R.id.btn_add_expense);
        btn_add_expense.setOnClickListener(v -> {
            bundle.putCharSequence("1", id_detail.getText().toString());
            getFragmentManager().setFragmentResult("104", bundle);
        });
        LinearLayout llExpense = view.findViewById(R.id.show_list_expense);
        TextView show_list_expense_btn = view.findViewById(R.id.show_list_expense_btn);
        show_list_expense_btn.setOnClickListener(v -> {
            if(llExpense.getVisibility() == View.VISIBLE) {
                llExpense.setVisibility(View.INVISIBLE);
            } else {
                llExpense.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
