package com.ben.cmpe277.cmpe277project.ui.editprofile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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

import java.util.HashMap;
import java.util.Map;

public class EditStudentInfoFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private View root;
    private View editBtn;
    private View passwordLayout;
    private View confirmPasswordLayout;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView userTitleText;
    private Button editConfirmBtn;
    private Button editCancelBtn;
    private Student student;
    private ObjectMapper m = new ObjectMapper();

    public EditStudentInfoFragment() {

    }

    public static EditStudentInfoFragment newInstance() {
        EditStudentInfoFragment fragment = new EditStudentInfoFragment();
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_student_info_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        editBtn = root.findViewById(R.id.enable_profile_edit_button);
        passwordLayout = root.findViewById(R.id.edit_profile_password_layout);
        confirmPasswordLayout = root.findViewById(R.id.edit_profile_confirm_password_layout);

        nameEditText = root.findViewById(R.id.edit_profile_name);
        emailEditText = root.findViewById(R.id.edit_profile_email);
        phoneEditText = root.findViewById(R.id.edit_profile_phone);
        passwordEditText = root.findViewById(R.id.edit_profile_password);
        confirmPasswordEditText = root.findViewById(R.id.edit_profile_confirm_password);
        userTitleText = root.findViewById(R.id.user_title);


        editConfirmBtn = root.findViewById(R.id.edit_profile_confirm_button);
        editCancelBtn = root.findViewById(R.id.edit_profile_cancel_button);
        disableEditMode();
        setTextFields();

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
               enableEditMode();
            }
        });
        editConfirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String name = nameEditText.getText().toString().trim();
                if (name.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.name_required));
                    return;
                }

                String email = emailEditText.getText().toString().trim();
                if (email.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.email_required));
                    return;
                }

                String phone = phoneEditText.getText().toString().trim();
                if (phone.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.phone_number_required));
                    return;
                }

                String pwd = passwordEditText.getText().toString().trim();
                if (pwd.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.password_required));
                    return;
                }

                String cnf_pwd = confirmPasswordEditText.getText().toString().trim();
                if (cnf_pwd.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.confirm_password_required));
                    return;
                }

                if (!pwd.equals(cnf_pwd)) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.passwords_must_match));
                    return;
                }

                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String endpoint = getString(R.string.baseURL_) + "/" + student.email;
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, endpoint,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        ((MainActivity) getActivity()).student = m.readValue(response, Student.class);
                                        ((MainActivity) getActivity()).updateStudentUI();
                                        setTextFields();
                                        ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.user_updated));
                                        disableEditMode();
                                    } catch (Exception e) {
                                        ((MainActivity) getActivity()).showSnackBarMessage(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.email_exists));
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", nameEditText.getText().toString().trim());
                            params.put("password", passwordEditText.getText().toString().trim());
                            params.put("email", emailEditText.getText().toString().trim());
                            params.put("phone_number", phoneEditText.getText().toString().trim());
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
                else {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.wifi_disconnected));
                }
            }
        });
        editCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                setTextFields();
                disableEditMode();
            }
        });

        return root;
    }

    private void enableEditMode(){
        // Hide the edit control elements
        editConfirmBtn.setVisibility(View.VISIBLE);
        editCancelBtn.setVisibility(View.VISIBLE);
        confirmPasswordLayout.setVisibility(View.VISIBLE);
        passwordLayout.setVisibility(View.VISIBLE);

        // Show the edit button
        editBtn.setVisibility(View.GONE);

        nameEditText.setEnabled(true);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        emailEditText.setEnabled(true);
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        phoneEditText.setEnabled(true);
        phoneEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
    }

    private void disableEditMode(){
        // Hide the edit control elements
        editConfirmBtn.setVisibility(View.GONE);
        editCancelBtn.setVisibility(View.GONE);
        confirmPasswordLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);

        // Show the edit button
        editBtn.setVisibility(View.VISIBLE);

        nameEditText.setEnabled(false);
        nameEditText.setInputType(InputType.TYPE_NULL);
        emailEditText.setEnabled(false);
        emailEditText.setInputType(InputType.TYPE_NULL);
        phoneEditText.setEnabled(false);
        phoneEditText.setInputType(InputType.TYPE_NULL);
    }

    private void setTextFields() {
        student = ((MainActivity) getActivity()).student;
        userTitleText.setText(student.name);
        nameEditText.setText(student.name);
        emailEditText.setText(student.email);
        phoneEditText.setText(student.phone_number);
        passwordEditText.setText(student.password);
        confirmPasswordEditText.setText(student.password);
    }
}