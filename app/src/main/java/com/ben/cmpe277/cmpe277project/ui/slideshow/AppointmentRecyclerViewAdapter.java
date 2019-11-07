package com.ben.cmpe277.cmpe277project.ui.slideshow;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.ui.slideshow.SlideshowFragment;
import com.ben.cmpe277.cmpe277project.ui.pendingsessions.TutorSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder> {

    private List<Appointment> mValues;
    private String currentStudent;
    private AppointmentRecyclerViewAdapter.AppointmentListListener pListener;

    public AppointmentRecyclerViewAdapter(List<Appointment> items, String currentStudent, AppointmentRecyclerViewAdapter.AppointmentListListener pListener) {
        mValues = items;
        this.currentStudent = currentStudent;
        this.pListener = pListener;
    }

    @Override
    public AppointmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_slideshow_list_item, parent, false);
        return new AppointmentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppointmentRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        if (position % 2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("white"));
        }

        if (mValues.get(position).tutor.equals(currentStudent)) {
            holder.studentNameTextView.setText(mValues.get(position).student);
        }
        else {
            holder.studentNameTextView.setText(mValues.get(position).tutor);
//            holder.approveSessionButton.hide();
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pListener.pendingSessionSelected(mValues.get(position));
//            }
//        });
//
//        holder.cancelSessionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pListener.removePendingSessionSelected(mValues.get(position));
//            }
//        });
//
//        holder.approveSessionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pListener.approvePendingSessionSelected(mValues.get(position));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView studentNameTextView;
        public final FloatingActionButton approveSessionButton;
        public final FloatingActionButton cancelSessionButton;
        public Appointment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            studentNameTextView = view.findViewById(R.id.student_name_session);
            approveSessionButton = view.findViewById(R.id.approve_session);
            cancelSessionButton = view.findViewById(R.id.cancel_session);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + studentNameTextView.getText() + "'";
        }
    }

    public void setAppointment(ArrayList<Appointment> appointments) {
        mValues = appointments;
    }

    public interface AppointmentListListener {
//        void pendingSessionSelected(TutorSession pendingTutorSession);
//        void removePendingSessionSelected(TutorSession pendingTutorSession);
//        void approvePendingSessionSelected(TutorSession pendingTutorSession);
    }
}
