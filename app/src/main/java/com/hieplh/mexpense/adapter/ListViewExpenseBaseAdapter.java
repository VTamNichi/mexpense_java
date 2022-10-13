package com.hieplh.mexpense.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.dtos.Expense;

import java.util.List;

public class ListViewExpenseBaseAdapter extends BaseAdapter {

    Context context;
    List<Expense> listExpense;
    LayoutInflater layoutInflater;

    public ListViewExpenseBaseAdapter(Context context, List<Expense> listExpense) {
        this.context = context;
        this.listExpense = listExpense;
    }

    @Override
    public int getCount() {
        return listExpense.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0) {
            return null;
        }
        return listExpense.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listExpense.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.from(context).inflate(R.layout.list_view_expense, null);
        TextView expense_type = convertView.findViewById(R.id.expense_type_list_view);
        expense_type.setText(listExpense.get(position).getExpenseType() + ": " + listExpense.get(position).getAmount() + " VNĐ");

        TextView expense_time = convertView.findViewById(R.id.expense_time_list_view);
        expense_time.setText(listExpense.get(position).getTime());

        TextView expense_date = convertView.findViewById(R.id.expense_date_list_view);
        expense_date.setText(listExpense.get(position).getDate());
        if(!listExpense.get(position).isHeader()) {
            expense_date.setVisibility(View.GONE);
        }

        return convertView;
    }
}
