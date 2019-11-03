package com.ben.cmpe277.cmpe277project.ui.tutorslist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.ben.cmpe277.cmpe277project.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.util.ArrayList;

public class TutorListFragment extends Fragment implements TutorAdapter.TutorListListener{
    private TutorAdapter tutorAdapter;
    private ArrayList<Student> tutors = new ArrayList<Student>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            tutorAdapter = new TutorAdapter(this.tutors, this);

            recyclerView.setAdapter(tutorAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
        }
        getTutors();
        return view;
    }

    public void setTutors(ArrayList<Student> tutors) {
        this.tutors = tutors;
        tutorAdapter.setTutors(tutors);
        tutorAdapter.notifyDataSetChanged();
    }

    public void getTutors() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String endpoint = ((MainActivity) getActivity())._baseURL;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray resJSON = new JSONArray(response);
                                ArrayList<Student> obtainedTutors = new ArrayList<Student>();
                                ObjectMapper m = new ObjectMapper();
                                for (int i = 0; i < resJSON.length(); i++) {
                                    String tutorString = resJSON.getJSONObject(i).toString();
                                    Student tutor = m.readValue(tutorString, Student.class);
                                    if (!tutor.email.equals(((MainActivity) getActivity()).student.email))
                                    {
                                        obtainedTutors.add(tutor);
                                    }
                                }
                                tutors = obtainedTutors;
                                tutorAdapter.setTutors(tutors);
                                tutorAdapter.notifyDataSetChanged();
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
        }
        else {
            ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.wifi_disconnected));
        }
    }

    @Override
    public void tutorSelected(Student student) {
        System.out.println("Student selected");
    }
}
