package com.ben.cmpe277.cmpe277project.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    public String _baseURL;
    private ObjectMapper m = new ObjectMapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        _baseURL = getString(R.string.baseURL_);
        mTextEmail = (EditText) findViewById(R.id.edittext_email);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mTextViewRegister = (TextView) findViewById(R.id.textview_register);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.getBoolean("showUserCreated")) {
                showSnackBarMessage(getString(R.string.created_user));
            }
        }

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mTextEmail.getText().toString().trim();
                if (user.length() == 0) {
                    showSnackBarMessage(getString(R.string.username_required));
                    return;
                }

                String pwd = mTextPassword.getText().toString().trim();
                if (pwd.length() == 0) {
                    showSnackBarMessage(getString(R.string.password_required));
                    return;
                }

                ConnectivityManager cm =
                        (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String endpoint = _baseURL + "/login";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject student = new JSONObject(response);
                                        String tutorString = student.toString();
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        Student tutor = m.readValue(tutorString, Student.class);
                                        intent.putExtra("student", tutor);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        showSnackBarMessage(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showSnackBarMessage(getString(R.string.login_failed));
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("email",  mTextEmail.getText().toString().trim());
                            params.put("password", mTextPassword.getText().toString().trim());
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
                else {
                    showSnackBarMessage(getString(R.string.wifi_disconnected));
                }
            }
        });
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

