// File: src/com/bloodBank/model/Recipient.java
package com.bloodBank.model;

import java.time.LocalDate;

/**
 * Represents a patient who receives blood.
 * Inherits all personal details from the Person class.
 */
public class Recipient extends Person  { // <-- NEW (implements)

    // Unique identifier for serialization
    private static final long serialVersionUID = 4L; 

    // Static counter for auto-incrementing ID
    private static int nextId = 1;

    private int recipientId;
    private String reasonForTransfusion; 
    private LocalDate dateReceived; // <-- NEW: Will be null until unit is issued

    /**
     * Constructor for a new Recipient.
     * Note: dateReceived is NOT set here.
     */
    public Recipient(String firstName, String lastName, String fatherName, String motherName,
                     LocalDate dob, String mobileNo, String gender, String email,
                     String bloodGroup, String city, String fullAddress,
                     String reasonForTransfusion) { 
        
        // Call the constructor of the parent (Person) class
        super(firstName, lastName, fatherName, motherName, dob, mobileNo, gender, 
              email, bloodGroup, city, fullAddress);
        
        // Set the Recipient-specific fields
        this.recipientId = nextId++;
        this.reasonForTransfusion = reasonForTransfusion;
        this.dateReceived = null; // Explicitly set to null on creation
    }

    // --- Getters for Recipient-specific fields ---

    public int getRecipientId() {
        return recipientId;
    }

    public String getReasonForTransfusion() {
        return reasonForTransfusion;
    }

    public LocalDate getDateReceived() { // <-- NEW
        return dateReceived;
    }
    
    // --- NEW "Function" (Method) to check status ---

    /**
     * This is the function you asked for.
     * @return true if the unit has been successfully received, false otherwise.
     */
    public boolean didReceiveUnit() { // <-- NEW
        return this.dateReceived != null;
    }

    /**
     * This method is called by the BloodBankModel during a
     * successful transaction to "stamp" the recipient as complete.
     */
    public void markAsReceived() { // <-- NEW
        this.dateReceived = LocalDate.now();
    }

    // --- Static Methods ---

    public static void setNextId(int id) {
        nextId = id;
    }

    @Override
    public String toString() {
        // Updated toString to show the new status
        String status = didReceiveUnit() ? "Received on " + dateReceived : "Pending";
        
        return "Recipient [ID=" + recipientId + 
                ", Name=" + getFirstName() + " " + getLastName() + 
                ", Group=" + getBloodGroup() + 
                ", Status=" + status + // <-- UPDATED
                ", Reason=" + reasonForTransfusion + "]";
    }
}