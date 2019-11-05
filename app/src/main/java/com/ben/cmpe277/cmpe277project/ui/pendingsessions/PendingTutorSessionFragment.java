package com.ben.cmpe277.cmpe277project.ui.pendingsessions;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ben.cmpe277.cmpe277project.ui.viewtutor.ViewTutorActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingTutorSessionFragment extends Fragment implements MyTutorSessionRecyclerViewAdapter.PendingSessionListListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<TutorSession> pendingTutorSessions = new ArrayList<TutorSession>();
    private MyTutorSessionRecyclerViewAdapter pendingTutorSessionAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PendingTutorSessionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PendingTutorSessionFragment newInstance(int columnCount) {
        PendingTutorSessionFragment fragment = new PendingTutorSessionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorsession_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            pendingTutorSessionAdapter = new MyTutorSessionRecyclerViewAdapter(this.pendingTutorSessions, ((MainActivity) getActivity()).student.email, this);
            recyclerView.setAdapter(pendingTutorSessionAdapter);
        }
        getPendingTutorSessions();
        return view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TutorSession item);
    }

    public void getPendingTutorSessions() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String endpoint = getString(R.string.baseURL_) + "/" + ((MainActivity) getActivity()).student.email + "/PENDING";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray resJSON = new JSONArray(response);
                                ArrayList<TutorSession> obtainedTutorSessions = new ArrayList<TutorSession>();
                                ObjectMapper m = new ObjectMapper();
                                for (int i = 0; i < resJSON.length(); i++) {
                                    JSONObject pendingTutorSessionJSON = resJSON.getJSONObject(i);
                                    TutorSession pendingTutorSession = new TutorSession(pendingTutorSessionJSON.getString("tutor"),
                                            pendingTutorSessionJSON.getString("student"),
                                            pendingTutorSessionJSON.getString("time"),
                                            pendingTutorSessionJSON.getString("date"));
                                    obtainedTutorSessions.add(pendingTutorSession);
                                }
                                setPendingTutorSessions(obtainedTutorSessions);
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

    public void setPendingTutorSessions(ArrayList<TutorSession> pendingTutorSessions) {
        this.pendingTutorSessions = pendingTutorSessions;
        pendingTutorSessionAdapter.setPendingTutorSessions(pendingTutorSessions);
        pendingTutorSessionAdapter.notifyDataSetChanged();
    }

    @Override
    public void pendingSessionSelected(final TutorSession pendingSession) {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String email = pendingSession.tutor;
            if (pendingSession.tutor.equals(((MainActivity) getActivity()).student.email)) email = pendingSession.student;
            String endpoint = getString(R.string.baseURL_) + "/" + email;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String tutorString = new JSONObject(response).toString();
                                ArrayList<TutorSession> obtainedTutorSessions = new ArrayList<TutorSession>();
                                ObjectMapper m = new ObjectMapper();
                                Student student = m.readValue(tutorString, Student.class);
                                DialogFragment dialog = ViewTutorSessionDialog.newInstance(student, pendingSession);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                dialog.show(getChildFragmentManager(), "TutorSessionDialog");
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
    public void removePendingSessionSelected(final TutorSession pendingSession) {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String email = ((MainActivity) getActivity()).student.email;
            if (!pendingSession.tutor.equals(((MainActivity) getActivity()).student.email)) email = pendingSession.tutor;
            String endpoint = getString(R.string.baseURL_) + "/" + email + "/deletepending";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.tutor_session_removed));
                                getPendingTutorSessions();
                            } catch (Exception e) {
                                ((MainActivity) getActivity()).showSnackBarMessage(e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.request_error));
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tutor", pendingSession.tutor);
                    params.put("student", pendingSession.student);
                    params.put("time", pendingSession.timeSlot);
                    params.put("date", pendingSession.date);
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

    @Override
    public void approvePendingSessionSelected(final TutorSession pendingSession) {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String email = ((MainActivity) getActivity()).student.email;
            if (!pendingSession.tutor.equals(((MainActivity) getActivity()).student.email)) email = pendingSession.tutor;
            String endpoint = getString(R.string.baseURL_) + "/" + email + "/addpending";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.tutor_session_added));
                                removePendingSessionSelected(pendingSession);
                            } catch (Exception e) {
                                ((MainActivity) getActivity()).showSnackBarMessage(e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.schedule_error));
                    }
                    else {
                        ((MainActivity) getActivity()).showSnackBarMessage(getString(R.string.request_error));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tutor", pendingSession.tutor);
                    params.put("student", pendingSession.student);
                    params.put("time", pendingSession.timeSlot);
                    params.put("date", pendingSession.date);
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
}
