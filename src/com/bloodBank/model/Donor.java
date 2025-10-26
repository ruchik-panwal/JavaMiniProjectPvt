package com.bloodBank.model;

import java.time.LocalDate;

// Donor now EXTENDS Person
public class Donor extends Person {

    // Keep its own serialVersionUID
    private static final long serialVersionUID = 3L; 

    // --- Donor-specific fields ---
    private static int nextId = 1;
    private int donorId;

    public Donor(String firstName, String lastName, String fatherName, String motherName,
                 LocalDate dob, String mobileNo, String gender, String email,
                 String bloodGroup, String city, String fullAddress) {

        // 1. Call the PARENT (Person) constructor with all the shared info
        super(firstName, lastName, fatherName, motherName, dob, mobileNo, 
              gender, email, bloodGroup, city, fullAddress);

        // 2. Set the DONOR-specific info
        this.donorId = nextId++;
    }

    // --- Donor-specific methods ---

    public int getDonorId() {
        return donorId;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    @Override
    public String toString() {
        String fullName = getFirstName() + " " + getLastName();
        return String.format("%-4d | %-20s | Blood Group: %s", 
                             donorId, fullName, getBloodGroup());
    }
}