package com.ben.cmpe277.cmpe277project.ui.gallery;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ben.cmpe277.cmpe277project.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private View root;
    private View editBtn;
    private View passwordLayout;
    private View confirmPasswordLayout;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button editConfirmBtn;
    private Button editCancelBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
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

        editConfirmBtn = root.findViewById(R.id.edit_profile_confirm_button);
        editCancelBtn = root.findViewById(R.id.edit_profile_cancel_button);
        disableEditMode();

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
                disableEditMode();
            }
        });
        editCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
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
}