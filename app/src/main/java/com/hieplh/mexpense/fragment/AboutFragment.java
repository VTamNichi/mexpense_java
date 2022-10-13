package com.hieplh.mexpense.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hieplh.mexpense.R;
import com.hieplh.mexpense.daos.UserDAO;

public class AboutFragment extends Fragment {

    UserDAO userDAO = new UserDAO();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        TextView mUsername = view.findViewById(R.id.user_name);
        mUsername.setText(userDAO.getUser().getUser_name());

        TextView mEmail = view.findViewById(R.id.user_email);
        mEmail.setText(userDAO.getUser().getUser_email());

        TextView mPhone = view.findViewById(R.id.user_phone);
        mPhone.setText("(" + userDAO.getUser().getCountry_code() + ") " + userDAO.getUser().getUser_phone().substring(1, 3) + " " + userDAO.getUser().getUser_phone().substring(3, 7) + " " + userDAO.getUser().getUser_phone().substring(7));

        return view;
    }
}
