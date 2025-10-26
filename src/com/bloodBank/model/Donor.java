package com.bloodBank.model; // Corrected package name

import java.io.Serializable;
import java.time.LocalDate;

public class Donor implements Serializable {

    private static final long serialVersionUID = 3L;
    private static int nextId = 1;

    private int donorId; // The new serial number field for each donor
    private String firstName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private LocalDate dob;
    private String mobileNo;
    private String gender;
    private String email;
    private String city;
    private String fullAddress;
    private String bloodGroup;

    public Donor(String firstName, String lastName, String fatherName, String motherName,
            LocalDate dob, String mobileNo, String gender, String email,
            String bloodGroup, String city, String fullAddress) {

        // Assign the current 'nextId' to this donor, then increment 'nextId'
        this.donorId = nextId++;

        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dob = dob;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.fullAddress = fullAddress;
    }

    public int getDonorId() {
        return donorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    public String toString() {
        String fullName = firstName + " " + lastName;
        // Updated format to include the new donorId
        return String.format("%-4d | %-20s | Blood Group: %s", donorId, fullName, bloodGroup);
    }
}