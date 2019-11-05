package com.ben.cmpe277.cmpe277project.ui.pendingsessions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TutorSession implements Serializable {

    public String tutor;
    public String student;
    public String timeSlot;
    public String date;

    public TutorSession() {
        super();
    }
    public TutorSession(String tutor, String student, String timeSlot, String date) {
        this.tutor = tutor;
        this.student = student;
        this.timeSlot = timeSlot;
        this.date = date;
    }

    @Override
    public String toString() {
        return tutor;
    }

}
