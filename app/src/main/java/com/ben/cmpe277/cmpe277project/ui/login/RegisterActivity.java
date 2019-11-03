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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    EditText mTextName;
    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText mTextPhone;
    Button mButtonRegister;
    TextView mTextViewLogin;
    public String _baseURL = "http://172.16.200.1:4000/student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextName = (EditText) findViewById(R.id.edittext_user_name);
        mTextEmail = (EditText) findViewById(R.id.edittext_email);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText) findViewById(R.id.edittext_cnf_password);
        mTextPhone = (EditText) findViewById(R.id.edittext_phone);
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mTextViewLogin = (TextView) findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mTextName.getText().toString().trim();
                if (name.length() == 0) {
                    showSnackBarMessage(getString(R.string.name_required));
                    return;
                }

                String email = mTextEmail.getText().toString().trim();
                if (email.length() == 0) {
                    showSnackBarMessage(getString(R.string.email_required));
                    return;
                }

                String phone = mTextPhone.getText().toString().trim();
                if (phone.length() == 0) {
                    showSnackBarMessage(getString(R.string.phone_number_required));
                    return;
                }

                String pwd = mTextPassword.getText().toString().trim();
                if (pwd.length() == 0) {
                    showSnackBarMessage(getString(R.string.password_required));
                    return;
                }

                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                if (cnf_pwd.length() == 0) {
                    showSnackBarMessage(getString(R.string.confirm_password_required));
                    return;
                }

                if (!pwd.equals(cnf_pwd)) {
                    showSnackBarMessage(getString(R.string.passwords_must_match));
                    return;
                }
                ConnectivityManager cm =
                        (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String endpoint = _baseURL;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        loginIntent.putExtra("showUserCreated", true);
                                        startActivity(loginIntent);
                                    } catch (Exception e) {
                                        showSnackBarMessage(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showSnackBarMessage(getString(R.string.email_exists));
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("name", mTextName.getText().toString().trim());
                            params.put("phone_number", mTextPhone.getText().toString().trim());
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
    };

    public void showSnackBarMessage(String message) {
        View v = findViewById(android.R.id.content);
        if (v != null) {
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .show();
        }
        return;
    }
}

