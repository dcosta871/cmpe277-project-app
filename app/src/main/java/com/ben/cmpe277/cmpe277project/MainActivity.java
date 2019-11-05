package com.ben.cmpe277.cmpe277project;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ben.cmpe277.cmpe277project.ui.login.LoginActivity;
import com.ben.cmpe277.cmpe277project.ui.login.RegisterActivity;
import com.ben.cmpe277.cmpe277project.ui.tutorslist.TutorListFragment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public Student student = null;
    public String _baseURL;
    private TextView navUserName;
    private TextView navEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _baseURL = getString(R.string.baseURL_);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tutors, R.id.nav_profile, R.id.nav_pending, R.id.nav_schedule)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_tutors) {

                    int id = destination.getId();

                }
            }
        });
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        navUserName = header.findViewById(R.id.nav_user_name);
        navEmail = header.findViewById(R.id.nav_user_email);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            student = (Student) extras.getSerializable("student");
            updateStudentUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateTutors() {
        TutorListFragment tutorListFragment = (TutorListFragment) getSupportFragmentManager().findFragmentById(R.id.nav_tutors);
        if (tutorListFragment != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                RequestQueue queue = Volley.newRequestQueue(this);
                String endpoint = _baseURL;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray resJSON = new JSONArray(response);
                                    ArrayList<Student> tutors = new ArrayList<Student>();
                                    ObjectMapper m = new ObjectMapper();
                                    for (int i = 0; i < resJSON.length(); i++) {
                                        String tutorString = resJSON.getJSONObject(i).toString();
                                        Student tutor = m.readValue(tutorString, Student.class);
                                        tutors.add(tutor);
                                    }
                                    //tutorListFragment.setTutors(tutors);
                                } catch (Exception e) {
                                    showSnackBarMessage(e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showSnackBarMessage(getString(R.string.request_error));
                    }
                });
                queue.add(stringRequest);
            }
            else {
                showSnackBarMessage(getString(R.string.wifi_disconnected));
            }
        }
    }

    public void showSnackBarMessage(String message) {
        View v = findViewById(android.R.id.content);
        if (v != null) {
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        return;
    }

    public void getUserInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String endpoint = _baseURL + "/" + student.email;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject resJSON = new JSONObject(response);
                                if (navUserName != null) {
                                    navUserName.setText(resJSON.getString("name"));
                                }
                                if (navEmail != null) {
                                    navEmail.setText(resJSON.getString("email"));
                                }
                            } catch (JSONException e) {
                                showSnackBarMessage(e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showSnackBarMessage(getString(R.string.request_error));
                }
            });
            queue.add(stringRequest);
        }
        else {
            showSnackBarMessage(getString(R.string.wifi_disconnected));
        }
    }

    public void updateStudentUI() {
        navUserName.setText(student.name);
        navEmail.setText(student.email);
    }
}