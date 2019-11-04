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
import android.widget.Switch;
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

public class EditTutorInfoFragment extends Fragment {

    private View root;
    private View editBtn;
    private EditText descriptionEditText;
    private EditText subjectEditText;
    private EditText priceEditText;
    private Button editConfirmBtn;
    private Button editCancelBtn;
    private Switch editTutorVisibleSwitch;
    private Student student;
    private ObjectMapper m = new ObjectMapper();

    public EditTutorInfoFragment() {

    }

    public static EditTutorInfoFragment newInstance() {
        EditTutorInfoFragment fragment = new EditTutorInfoFragment();
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tutor_info_profile, container, false);
        editBtn = root.findViewById(R.id.enable_profile_tutor_edit_button);
        descriptionEditText = root.findViewById(R.id.edit_profile_tutor_description);
        subjectEditText = root.findViewById(R.id.edit_profile_tutor_subject);
        priceEditText = root.findViewById(R.id.edit_profile_tutor_price);
        editTutorVisibleSwitch = root.findViewById(R.id.edit_profile_tutor_visible_switch);


        editConfirmBtn = root.findViewById(R.id.edit_profile_tutor_confirm_button);
        editCancelBtn = root.findViewById(R.id.edit_profile_tutor_cancel_button);
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
                String description = descriptionEditText.getText().toString().trim();
                if (description.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.description_required));
                    return;
                }

                String subject = subjectEditText.getText().toString().trim();
                if (subject.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.subject_required));
                    return;
                }

                String price = priceEditText.getText().toString().trim();
                if (price.length() == 0) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.price_required));
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
                                        ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.tutor_updated));
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
                            params.put("email", student.email);
                            params.put("description", descriptionEditText.getText().toString().trim());
                            params.put("subject", subjectEditText.getText().toString().trim());
                            params.put("price", priceEditText.getText().toString().trim());
                            params.put("isTutorVisible", String.valueOf(editTutorVisibleSwitch.isChecked()));
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

        // Show the edit button
        editBtn.setVisibility(View.GONE);

        editTutorVisibleSwitch.setEnabled(true);
        descriptionEditText.setEnabled(true);
        descriptionEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        subjectEditText.setEnabled(true);
        subjectEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        priceEditText.setEnabled(true);
        priceEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
    }

    private void disableEditMode(){
        // Hide the edit control elements
        editConfirmBtn.setVisibility(View.GONE);
        editCancelBtn.setVisibility(View.GONE);

        // Show the edit button
        editBtn.setVisibility(View.VISIBLE);

        editTutorVisibleSwitch.setEnabled(false);
        descriptionEditText.setEnabled(false);
        descriptionEditText.setInputType(InputType.TYPE_NULL);
        subjectEditText.setEnabled(false);
        subjectEditText.setInputType(InputType.TYPE_NULL);
        priceEditText.setEnabled(false);
        priceEditText.setInputType(InputType.TYPE_NULL);
    }

    private void setTextFields() {
        student = ((MainActivity) getActivity()).student;
        descriptionEditText.setText(student.description);
        subjectEditText.setText(student.subject);
        priceEditText.setText(student.price);
        editTutorVisibleSwitch.setChecked(Boolean.parseBoolean(student.isTutorVisible));
    }
}