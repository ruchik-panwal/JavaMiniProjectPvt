package com.bloodBank.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A base class that holds all information common to
 * both Donors and Recipients.
 */
public abstract class Person implements Serializable {

    // Use a different serialVersionUID for the base class
    private static final long serialVersionUID = 1L;

    // All the shared fields
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

    public Person(String firstName, String lastName, String fatherName, String motherName,
                  LocalDate dob, String mobileNo, String gender, String email,
                  String bloodGroup, String city, String fullAddress) {
        
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

    // --- Add Getters for all these fields ---

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFatherName() { return fatherName; }
    public String getMotherName() { return motherName; }
    public LocalDate getDob() { return dob; }
    public String getMobileNo() { return mobileNo; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public String getCity() { return city; }
    public String getFullAddress() { return fullAddress; }
    public String getBloodGroup() { return bloodGroup; }
}