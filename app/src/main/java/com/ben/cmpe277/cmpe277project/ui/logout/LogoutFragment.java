package com.ben.cmpe277.cmpe277project.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ben.cmpe277.cmpe277project.MainActivity;
import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent LoginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(LoginIntent);
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        return root;
    }
}