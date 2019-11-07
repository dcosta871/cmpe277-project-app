package com.ben.cmpe277.cmpe277project.ui.slideshow;

import java.io.Serializable;

public class Appointment implements Serializable {

    public String tutor;
    public String student;
    public String timeSlot;
    public String date;
    public String startTime;
    public String endTime;

    public Appointment() {
        super();
    }
    public Appointment(String tutor, String student, String timeSlot, String date, String startTime, String endTime) {
        this.tutor = tutor;
        this.student = student;
        this.timeSlot = timeSlot;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return tutor;
    }

}
