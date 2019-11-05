package com.ben.cmpe277.cmpe277project.ui.pendingsessions;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.Student;
import com.ben.cmpe277.cmpe277project.ui.pendingsessions.PendingTutorSessionFragment.OnListFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TutorSession} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTutorSessionRecyclerViewAdapter extends RecyclerView.Adapter<MyTutorSessionRecyclerViewAdapter.ViewHolder> {

    private List<TutorSession> mValues;
    private String currentStudent;
    private PendingSessionListListener pListener;

    public MyTutorSessionRecyclerViewAdapter(List<TutorSession> items, String currentStudent, PendingSessionListListener pListener) {
        mValues = items;
        this.currentStudent = currentStudent;
        this.pListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tutorsession, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
            holder.approveSessionButton.hide();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.pendingSessionSelected(mValues.get(position));
            }
        });

        holder.cancelSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.removePendingSessionSelected(mValues.get(position));
            }
        });

        holder.approveSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.approvePendingSessionSelected(mValues.get(position));
            }
        });
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
        public TutorSession mItem;

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

    public void setPendingTutorSessions(ArrayList<TutorSession> tutorSessions) {
        mValues = tutorSessions;
    }

    public interface PendingSessionListListener {
        void pendingSessionSelected(TutorSession pendingTutorSession);
        void removePendingSessionSelected(TutorSession pendingTutorSession);
        void approvePendingSessionSelected(TutorSession pendingTutorSession);
    }
}
