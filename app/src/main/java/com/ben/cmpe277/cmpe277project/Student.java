package com.ben.cmpe277.cmpe277project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student implements Serializable, Comparable<Student> {
    public String name;
    public String phone_number;
    public String email;
    public String subject;
    public String zipcode;
    public String password;
    public String isTutorVisible;
    public float rating;
    public String price;
    public String description;

    public Student() {
        super();
    }
    public Student(String name, String phone_number, String email, String subject, String zipcode, String password, String isTutorVisible, float rating, String price, String description) {
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.subject = subject;
        this.isTutorVisible = isTutorVisible;
        this.zipcode = zipcode;
        this.password = password;
        this.price = price;
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Student s)
    {
        if (rating == s.rating) return 0;
        return rating < s.rating ? 1:-1;
    }
}
