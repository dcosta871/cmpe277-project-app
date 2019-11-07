package com.ben.cmpe277.cmpe277project.ui.slideshow;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ben.cmpe277.cmpe277project.MainActivity;
import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.ui.pendingsessions.MyTutorSessionRecyclerViewAdapter;
import com.ben.cmpe277.cmpe277project.ui.pendingsessions.TutorSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment implements AppointmentRecyclerViewAdapter.AppointmentListListener {

    private SlideshowViewModel slideshowViewModel;
    private AppointmentRecyclerViewAdapter appointmentRecyclerViewAdapter;
    private ArrayList<Appointment> appointments;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow_list, container, false);

        appointments = new ArrayList<Appointment>();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            appointmentRecyclerViewAdapter = new AppointmentRecyclerViewAdapter(this.appointments, ((MainActivity) getActivity()).student.email, this);
            recyclerView.setAdapter(appointmentRecyclerViewAdapter);
        }
        getPendingTutorSessions();
        return view;
    }


    public void getPendingTutorSessions() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String endpoint = getString(R.string.baseURL_) + "/" + ((MainActivity) getActivity()).student.email + "/schedule";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                JSONArray resJSON = res.getJSONArray("appointments");
                                ArrayList<Appointment> obtainedTutorSessions = new ArrayList<Appointment>();
//                                ObjectMapper m = new ObjectMapper();
                                for (int i = 0; i < resJSON.length(); i++) {
                                    JSONObject appointmentJSON = resJSON.getJSONObject(i);
                                    Appointment ap = new Appointment(appointmentJSON.getString("tutor"),
                                            appointmentJSON.getString("student"),
                                            appointmentJSON.getString("time"),
                                            appointmentJSON.getString("date"),
                                            appointmentJSON.getString("startTime"),
                                            appointmentJSON.getString("endTime"));
                                    obtainedTutorSessions.add(ap);
                                }
                                appointmentRecyclerViewAdapter.setAppointment(obtainedTutorSessions);
                                appointmentRecyclerViewAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                ((MainActivity) getActivity()).showSnackBarMessage(e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.request_error));
                }
            });
            queue.add(stringRequest);
        } else {
            ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.wifi_disconnected));
        }
    }
}
//    public View onCreateView(@NonNull LayoutInflater inflater,
//        ViewGroup container, Bundle savedInstanceState) {
//        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
//}