// File: src/com/bloodBank/model/Recipient.java
package com.bloodBank.model;

import java.time.LocalDate;

/**
 * Represents a patient who receives blood.
 * Inherits all personal details from the Person class.
 */
public class Recipient extends Person {

    // Unique identifier for serialization
    private static final long serialVersionUID = 4L; // Must be different from other classes

    // Static counter for auto-incrementing ID
    private static int nextId = 1;

    private int recipientId;
    private String reasonForTransfusion; // e.g., "Surgery", "Accident", "Anemia"

    /**
     * Constructor for a new Recipient.
     * All parameters from 'firstName' to 'fullAddress' are passed to the Person superclass.
     * * @param reasonForTransfusion The medical reason for needing blood.
     */
    public Recipient(String firstName, String lastName, String fatherName, String motherName,
                     LocalDate dob, String mobileNo, String gender, String email,
                     String bloodGroup, String city, String fullAddress,
                     String reasonForTransfusion) { // <-- Recipient-specific field
        
        // Call the constructor of the parent (Person) class to set all shared fields
        super(firstName, lastName, fatherName, motherName, dob, mobileNo, gender, 
              email, bloodGroup, city, fullAddress);
        
        // Set the Recipient-specific fields
        this.recipientId = nextId++;
        this.reasonForTransfusion = reasonForTransfusion;
    }

    // --- Getters for Recipient-specific fields ---

    public int getRecipientId() {
        return recipientId;
    }

    public String getReasonForTransfusion() {
        return reasonForTransfusion;
    }

    // --- Static Methods ---

    /**
     * This is called by the BloodBankModel when loading data from a file
     * to ensure new recipients don't re-use old IDs.
     * * @param id The next ID to be used.
     */
    public static void setNextId(int id) {
        nextId = id;
    }

    @Override
    public String toString() {
        // You can get all the Person fields using their getters
        return "Recipient [ID=" + recipientId + 
               ", Name=" + getFirstName() + " " + getLastName() + 
               ", Group=" + getBloodGroup() + 
               ", Reason=" + reasonForTransfusion + "]";
    }
}