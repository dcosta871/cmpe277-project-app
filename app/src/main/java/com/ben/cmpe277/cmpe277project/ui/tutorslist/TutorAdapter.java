package com.ben.cmpe277.cmpe277project.ui.tutorslist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ben.cmpe277.cmpe277project.R;
import com.ben.cmpe277.cmpe277project.Student;

import java.util.ArrayList;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.MyViewHolder> {
    private ArrayList<Student> tutors;
    private TutorListListener tutorListListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tutorNameTextView;
        public TextView tutorNameSubjectView;
        public MyViewHolder(View view) {
            super(view);
            tutorNameTextView = view.findViewById(R.id.tutor_name);
            tutorNameSubjectView = view.findViewById(R.id.tutor_subject);
        }
    }

    public TutorAdapter(ArrayList<Student> tutors, TutorListListener tutorListListener) {
        this.tutors = tutors;
        this.tutorListListener = tutorListListener;
    }

    @Override
    public TutorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tutor_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tutorNameTextView.setText(this.tutors.get(position).name);
        holder.tutorNameSubjectView.setText(this.tutors.get(position).subject);
        if (position % 2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("white"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorListListener.tutorSelected(tutors.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tutors.size();
    }

    public void setTutors(ArrayList<Student> tutors) {
        this.tutors = tutors;
    }

    public interface TutorListListener {
        void tutorSelected(Student student);
    }
}
