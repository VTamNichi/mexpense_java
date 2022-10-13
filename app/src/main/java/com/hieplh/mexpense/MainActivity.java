package com.hieplh.mexpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hieplh.mexpense.daos.ExpenseDAO;
import com.hieplh.mexpense.daos.TripDAO;
import com.hieplh.mexpense.daos.UserDAO;
import com.hieplh.mexpense.dtos.Expense;
import com.hieplh.mexpense.dtos.Trip;
import com.hieplh.mexpense.fragment.AboutFragment;
import com.hieplh.mexpense.fragment.AddFragment;
import com.hieplh.mexpense.fragment.DetailFragment;
import com.hieplh.mexpense.fragment.HomeFragment;
import com.hieplh.mexpense.fragment.ListFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ADD = 1;
    private static final int FRAGMENT_LIST = 2;
    private static final int FRAGMENT_ABOUT = 3;

    private boolean fragment_detail = false;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigationView;
    private NavigationView navigationView;

    private int width = 0;
    UserDAO userDAO = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        Toolbar toolbar_detail = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar_detail);

        TextView mUsername = findViewById(R.id.user_name);
        mUsername.setText(userDAO.getUser().getUser_name());

        TextView mEmail = findViewById(R.id.user_email);
        mEmail.setText(userDAO.getUser().getUser_email());

        mBottomNavigationView = findViewById(R.id.bottom_navbar);

        mDrawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
        mBottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
        mBottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            toolbar.setVisibility(View.VISIBLE);
            toolbar_detail.setVisibility(View.GONE);
            replaceFragmetAll(id);
            fragment_detail = false;
            return true;
        });

        toolbar_detail.setNavigationOnClickListener(v -> {
            toolbar.setVisibility(View.VISIBLE);
            toolbar_detail.setVisibility(View.GONE);
            replaceFragment(new ListFragment("", "", "", "", "", true));
            mCurrentFragment = FRAGMENT_LIST;
            navigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
            mBottomNavigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
        });

        getSupportFragmentManager().setFragmentResultListener("101", this, (requestKey, result) -> {
            switch (result.getString("1")) {
                case "new_trip":
                    replaceFragment(new AddFragment());
                    mCurrentFragment = FRAGMENT_ADD;
                    navigationView.getMenu().findItem(R.id.menu_add).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_add).setChecked(true);
                    break;
                case "trip_list":
                    replaceFragment(new ListFragment("", "", "", "", "", true));
                    mCurrentFragment = FRAGMENT_LIST;
                    navigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                    break;
                case "about":
                    replaceFragment(new AboutFragment());
                    mCurrentFragment = FRAGMENT_ABOUT;
                    navigationView.getMenu().findItem(R.id.menu_about).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_about).setChecked(true);
                    break;
                case "reset":
                    TripDAO tripDAO = new TripDAO(this);
                    tripDAO.deleteAll();
                    ExpenseDAO expenseDAO = new ExpenseDAO(this);
                    expenseDAO.deleteAll();
                    showToast("Reset successfully");
                    break;
                case "backup":
                    showToast("Backup to cloud successfully");
                    break;
            }
        });

        getSupportFragmentManager().setFragmentResultListener("102", this, (requestKey, result) -> {
            String rs = result.getString("1");
            if(rs.length() > 0) {
                if(rs == "date") {
                    openDatePicker(null);
                } else {
                    openConfirmDialog(rs);
                }
            }
        });

        getSupportFragmentManager().setFragmentResultListener("103", this, (requestKey, result) -> {
            String rs = result.getString("1");
            switch (rs) {
                case "filter":
                    openFilterDialog();
                    break;
                case "refresh":
                    replaceFragment(new ListFragment("", "", "", "", "", true));
                    break;
                default:
                    Trip trip = new Trip();
                    String[] attr = rs.split("@!#");
                    trip.setId(Integer.parseInt(attr[0].trim()));
                    trip.setTripName(attr[1].trim());
                    trip.setDestination(attr[2].trim());
                    trip.setTripDate(attr[3].trim());
                    trip.setDescription(attr[4].trim());
                    trip.setRiskAssessment(Integer.parseInt(attr[5].trim()));
                    trip.setLocation(attr[6].trim());
                    trip.setStatus(attr[7].trim());
                    fragment_detail = true;
                    toolbar.setVisibility(View.GONE);
                    toolbar_detail.setVisibility(View.VISIBLE);
                    toolbar_detail.setTitle("");
                    replaceFragment(new DetailFragment(trip));
                    mCurrentFragment = FRAGMENT_LIST;
                    navigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                    break;
            }
        });

        getSupportFragmentManager().setFragmentResultListener("104", this, (requestKey, result) -> {
            openAddExpenseDialog(Integer.parseInt(result.getString("1")));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        TextView id_detail = findViewById(R.id.id_detail);
        TextView trip_name_detail = findViewById(R.id.trip_name_detail);
        TextView destination_detail = findViewById(R.id.destination_detail);
        TextView date_detail = findViewById(R.id.date_detail);
        TextView description_detail = findViewById(R.id.description_detail);
        TextView risk_assessment_detail = findViewById(R.id.risk_assessment_detail);
        TextView location_detail = findViewById(R.id.location_detail);
        TextView status_detail = findViewById(R.id.status_detail);

        Trip trip = new Trip();
        trip.setId(Integer.parseInt(id_detail.getText().toString()));
        trip.setTripName(trip_name_detail.getText().toString());
        trip.setDestination(destination_detail.getText().toString());
        trip.setTripDate(date_detail.getText().toString());
        trip.setDescription(description_detail.getText().toString());
        int riskA = 0;
        if(risk_assessment_detail.getText().toString() == "Yes") {
            riskA = 1;
        }
        trip.setRiskAssessment(riskA);
        trip.setLocation(location_detail.getText().toString());
        trip.setStatus(status_detail.getText().toString());

        if(id == R.id.btn_update) {
            openUpdateDialog(trip);
        } else {
            openDeleteDialog(trip.getId(), trip.getTripName());
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        replaceFragmetAll(id);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if(fragment_detail){
            replaceFragment(new ListFragment("", "", "", "", "", true));
        } else {
            super.onBackPressed();
        }
    }

    private void openUpdateDialog(Trip trip) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        Button btn_cancel_update = dialog.findViewById(R.id.btn_cancel_update);
        btn_cancel_update.setOnClickListener(v -> dialog.dismiss());

        EditText text_trip_name_update = dialog.findViewById(R.id.text_trip_name_update);
        text_trip_name_update.setText(trip.getTripName());

        EditText text_destination_update = dialog.findViewById(R.id.text_destination_update);
        text_destination_update.setText(trip.getDestination());

        EditText text_date_update = dialog.findViewById(R.id.text_date_update);
        text_date_update.setText(trip.getTripDate());
        text_date_update.setOnClickListener(v -> {
            openDatePicker(text_date_update);
        });

        EditText text_description_update = dialog.findViewById(R.id.text_description_update);
        text_description_update.setText(trip.getDescription());

        EditText text_location_update = dialog.findViewById(R.id.text_location_update);
        text_location_update.setText(trip.getLocation());

        EditText text_status_update = dialog.findViewById(R.id.text_status_update);
        text_status_update.setText(trip.getStatus());

        Switch switch_btn_update = dialog.findViewById(R.id.switch_btn_update);
        switch_btn_update.setChecked(false);
        if(trip.getRiskAssessment() == 1) {
            switch_btn_update.setChecked(true);
        }

        Button btn_update = dialog.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(v -> {
            Trip newTrip = new Trip();
            newTrip.setId(trip.getId());
            newTrip.setTripName(text_trip_name_update.getText().toString());
            newTrip.setDestination(text_destination_update.getText().toString());
            newTrip.setTripDate(text_date_update.getText().toString());
            newTrip.setDescription(text_description_update.getText().toString());
            newTrip.setLocation(text_location_update.getText().toString());
            newTrip.setStatus(text_status_update.getText().toString());
            newTrip.setRiskAssessment(0);
            if(switch_btn_update.isChecked()) {
                newTrip.setRiskAssessment(1);
            }
            TripDAO tripDAO = new TripDAO(this);
            try {
                tripDAO.update(newTrip);
                showToast("Updated trip '" + newTrip.getTripName() + "'");
                replaceFragment(new DetailFragment(newTrip));
            } catch (Exception e) {
                showToast("Trip name '" + newTrip.getTripName() + "' has exist");
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDeleteDialog(int id, String name) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        Button btn_cancel_delete = dialog.findViewById(R.id.btn_cancel_delete);
        btn_cancel_delete.setOnClickListener(v -> dialog.dismiss());

        Button btn_delete_confirm = dialog.findViewById(R.id.btn_delete_confirm);
        btn_delete_confirm.setOnClickListener(v -> {
            TripDAO tripDAO = new TripDAO(this);
            tripDAO.delete(id);
            ExpenseDAO expenseDAO = new ExpenseDAO(this);
            expenseDAO.delete(id);
            showToast("Deleted trip '" + name + "'");
            replaceFragment(new ListFragment("", "", "", "", "", true));
            dialog.dismiss();
        });

        EditText text_confirm_delete = dialog.findViewById(R.id.text_confirm_delete);
        text_confirm_delete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(text_confirm_delete.getText().toString().equals("confirm delete")) {
                    btn_delete_confirm.setEnabled(true);
                } else {
                    btn_delete_confirm.setEnabled(false);
                }

            }
        });

        dialog.show();
    }

    private void openAddExpenseDialog(int tripId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_expense_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        Button btn_cancel_add_expense = dialog.findViewById(R.id.btn_cancel_add_expense);
        btn_cancel_add_expense.setOnClickListener(v -> dialog.dismiss());

        TextView text_spinner = dialog.findViewById(R.id.text_spinner);

        Spinner spinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_spinner.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        EditText text_amount_add_expense = dialog.findViewById(R.id.text_amount_add_expense);

        EditText text_date_add_expense = dialog.findViewById(R.id.text_date_add_expense);
        text_date_add_expense.setOnClickListener(v -> {
            openDatePicker(text_date_add_expense);
        });
        EditText text_time_add_expense = dialog.findViewById(R.id.text_time_add_expense);
        text_time_add_expense.setOnClickListener(v -> {
            openTimePickerDialog(text_time_add_expense);
        });

        EditText text_comment_add_expense = dialog.findViewById(R.id.text_comment_add_expense);

        Button btn_add_expense_dialog = dialog.findViewById(R.id.btn_add_expense_dialog);
        btn_add_expense_dialog.setOnClickListener(v -> {
            String amount = text_amount_add_expense.getText().toString();
            String expenseType = text_spinner.getText().toString();
            String date = text_date_add_expense.getText().toString();
            String time = text_time_add_expense.getText().toString();
            String comment = text_comment_add_expense.getText().toString();

            if(amount.length() == 0) {
                text_amount_add_expense.setError("Please fill out this field");
            } else if(date.length() == 0) {
                text_date_add_expense.setError("Please fill out this field");
            } else if(time.length() == 0) {
                text_time_add_expense.setError("Please fill out this field");
            } else {
                ExpenseDAO expenseDAO = new ExpenseDAO(this);
                Expense expense = new Expense();
                expense.setAmount(Integer.parseInt(amount));
                expense.setExpenseType(expenseType);
                expense.setDate(date);
                expense.setTime(time);
                expense.setComment(comment);
                expense.setTripId(tripId);
                expenseDAO.insert(expense);
                TripDAO tripDAO = new TripDAO(this);
                Trip trip = tripDAO.getTripById(tripId);
                replaceFragment(new DetailFragment(trip));
                showToast("Added expense '" + amount + " VNĐ (" + expenseType + ")'");
                dialog.dismiss();
            }


        });

        dialog.show();

    }

    private void openTimePickerDialog(EditText editText) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.time_picker_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        TimePicker timePicker = dialog.findViewById(R.id.time_picker);
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            String hour = String.valueOf(hourOfDay);
            if(hour.length() == 1) {
                hour = "0"+hour;
            }
            String minutes = String.valueOf(minute);
            if(minutes.length() == 1) {
                minutes = "0"+minutes;
            }
            editText.setText(hour + ":" + minutes);
        });


        dialog.show();
    }

    private void openFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        EditText text_trip_name_filter = dialog.findViewById(R.id.text_trip_name_filter);

        EditText text_destination_filter = dialog.findViewById(R.id.text_destination_filter);

        EditText text_date_filter = dialog.findViewById(R.id.text_date_filter);
        text_date_filter.setOnClickListener(v -> {
            openDatePicker(text_date_filter);
        });

        Button search_btn = dialog.findViewById(R.id.btn_search_filter);
        search_btn.setOnClickListener(v -> {
            replaceFragment(new ListFragment(text_trip_name_filter.getText().toString(), text_destination_filter.getText().toString(), text_date_filter.getText().toString(), "", "", false));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar() {
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                if(editText == null) {
                    EditText text_date = findViewById(R.id.text_date);
                    text_date.setText(sdf.format(calendar.getTime()));
                } else {
                    editText.setText(sdf.format(calendar.getTime()));
                }

            }
        };

        new DatePickerDialog(MainActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        .show();

    }

    private void openConfirmDialog(String trips) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        String[] trip = trips.split("@!#");

        TextView text_trip_name_confirm = dialog.findViewById(R.id.text_trip_name_confirm);
        text_trip_name_confirm.setText(trip[1].trim());

        TextView text_destination_confirm = dialog.findViewById(R.id.text_destination_confirm);
        text_destination_confirm.setText(trip[2].trim());

        TextView text_date_confirm = dialog.findViewById(R.id.text_date_confirm);
        text_date_confirm.setText(trip[3].trim());

        TextView text_description_confirm = dialog.findViewById(R.id.text_description_confirm);
        text_description_confirm.setText(trip[4].trim());

        TextView text_risk_assessment_confirm = dialog.findViewById(R.id.text_risk_assessment_confirm);
        String riskAssessment = "No";
        if(trip[5].trim().endsWith("1")) {
            riskAssessment = "Yes";
        }
        text_risk_assessment_confirm.setText(riskAssessment);

        TextView text_location_confirm = dialog.findViewById(R.id.text_location_confirm);
        text_location_confirm.setText(trip[6].trim());

        TextView text_status_confirm = dialog.findViewById(R.id.text_status_confirm);
        text_status_confirm.setText(trip[7].trim());

        Button edit_confirm_btn = dialog.findViewById(R.id.btn_edit_confirm);
        edit_confirm_btn.setOnClickListener(v -> dialog.dismiss());

        Button confirm_btn = dialog.findViewById(R.id.btn_confirm);
        confirm_btn.setOnClickListener(v -> {
            TripDAO tripDAO = new TripDAO(this);
            Trip dto = new Trip();
            dto.setTripName(trip[1].trim());
            dto.setDestination(trip[2].trim());
            dto.setTripDate(trip[3].trim());
            dto.setDescription(trip[4].trim());
            dto.setRiskAssessment(Integer.parseInt(trip[5].trim()));
            dto.setLocation(trip[6].trim());
            dto.setStatus(trip[7].trim());
            try {
                tripDAO.insert(dto);
                showToast("Added trip '" + dto.getTripName() + "'");
            } catch (Exception e) {
                showToast("Trip name '" + dto.getTripName() + "' has exist");
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showToast(String textShow) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        layout.setMinimumWidth(700);

        TextView text = layout.findViewById(R.id.text);
        text.setText(textShow);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void replaceFragmetAll(int id) {
        switch (id) {
            case R.id.menu_home:
                if(mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;
                    navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }
                break;
            case R.id.menu_add:
                if(mCurrentFragment != FRAGMENT_ADD) {
                    replaceFragment(new AddFragment());
                    mCurrentFragment = FRAGMENT_ADD;
                    navigationView.getMenu().findItem(R.id.menu_add).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_add).setChecked(true);
                }
                break;
            case R.id.menu_list:
                if(mCurrentFragment != FRAGMENT_LIST || fragment_detail) {
                    replaceFragment(new ListFragment("", "", "", "", "", true));
                    mCurrentFragment = FRAGMENT_LIST;
                    navigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_list).setChecked(true);
                }
                break;
            case R.id.menu_about:
                if(mCurrentFragment != FRAGMENT_ABOUT) {
                    replaceFragment(new AboutFragment());
                    mCurrentFragment = FRAGMENT_ABOUT;
                    navigationView.getMenu().findItem(R.id.menu_about).setChecked(true);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_about).setChecked(true);
                }
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}