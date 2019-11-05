package com.ben.cmpe277.cmpe277project.ui.pendingsessions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.Student;

import java.io.Serializable;
import java.net.URI;

public class ViewTutorSessionDialog extends DialogFragment {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

    private TextView dateTextView;
    private TextView timeTextView;
    private Button callButton;
    private Button cancelButton;
    private Student student;
    private TutorSession tutorSession;

    static ViewTutorSessionDialog newInstance(Student student, TutorSession tutorSession) {
        ViewTutorSessionDialog f = new ViewTutorSessionDialog();
        Bundle args = new Bundle();
        args.putSerializable("student", student);
        args.putSerializable("tutorSession", tutorSession);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutor_session_dialog, container, false);
        student = (Student) getArguments().getSerializable("student");
        tutorSession = (TutorSession) getArguments().getSerializable("tutorSession");
        nameTextView = view.findViewById(R.id.tutor_session_name);
        nameTextView.setText(student.name);
        emailTextView = view.findViewById(R.id.tutor_session_email);
        emailTextView.setText(student.email);
        phoneTextView = view.findViewById(R.id.tutor_session_phone_number);
        phoneTextView.setText(student.phone_number);
        dateTextView = view.findViewById(R.id.date_title);
        dateTextView.setText(tutorSession.date);
        timeTextView = view.findViewById(R.id.time_title);
        timeTextView.setText(tutorSession.timeSlot);
        callButton = view.findViewById(R.id.tutor_session_call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + student.phone_number));
                startActivity(i);
            }
        });
        cancelButton = view.findViewById(R.id.tutor_session_close);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }
}
