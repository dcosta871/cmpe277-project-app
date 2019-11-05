package com.ben.cmpe277.cmpe277project.ui.viewtutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ben.cmpe277.cmpe277project.MainActivity;
import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

public class ViewTutorActivity extends AppCompatActivity {

    private TextView descriptionTextView;
    private TextView subjectTextView;
    private TextView priceTextView;
    private TextView dateTextView;
    private Button selectDateButton;
    private Spinner selectTimeSpinner;
    private Button bookTutorButton;

    private Student tutor;
    private Student student;
    int day;
    int month;
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        descriptionTextView = findViewById(R.id.view_tutor_description);
        subjectTextView = findViewById(R.id.view_tutor_subject);
        priceTextView = findViewById(R.id.view_tutor_price);
        selectDateButton = findViewById(R.id.select_date);
        dateTextView = findViewById(R.id.view_tutor_date);
        selectTimeSpinner = findViewById(R.id.spinner);
        bookTutorButton = findViewById(R.id.view_tutor_book_button);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tutor = (Student) extras.getSerializable("tutor");
            student = (Student) extras.getSerializable("student");
            descriptionTextView.setText(tutor.description);
            subjectTextView.setText(tutor.subject);
            priceTextView.setText(tutor.price);
            getSupportActionBar().setTitle(tutor.name);
        }
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        addItemsOnSpinner();
        bookTutorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String endpoint = getString(R.string.baseURL_) + "/" + tutor.email + "/pending";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        finish();
                                    } catch (Exception e) {
                                        showSnackBarMessage(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showSnackBarMessage(getString(R.string.time_already_selected));
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("student", student.email);
                            params.put("date", dateTextView.getText().toString().trim());
                            params.put("time",  selectTimeSpinner.getSelectedItem().toString().trim());
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };;
                    queue.add(stringRequest);
                }
                else {
                    showSnackBarMessage(getString(R.string.wifi_disconnected));
                }
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTutorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateTextView.setText(i + "/" + i1 + "/" + i2);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("8-9am");
        list.add("9-10am");
        list.add("10-11am");
        list.add("11-12pm");
        list.add("12-1pm");
        list.add("1-2pm");
        list.add("2-3pm");
        list.add("3-4pm");
        list.add("4-5pm");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTimeSpinner.setAdapter(dataAdapter);
    }

    public void showSnackBarMessage(String message) {
        View v = findViewById(android.R.id.content);
        if (v != null) {
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        return;
    }
}
