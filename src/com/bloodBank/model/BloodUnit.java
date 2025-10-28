// File: src/com/bloodBank/model/BloodUnit.java
package com.bloodBank.model;

import java.io.Serializable;
import java.time.LocalDate;


public class BloodUnit implements Serializable {

    private static final long serialVersionUID = 5L;
    private static int nextId = 1;

    private int unitId;
    private int donorId;
    private String bloodGroup;
    private LocalDate donationDate;
    private LocalDate expiryDate;
    private BloodStatus status;
    private Integer recipientId; // <-- NEW FIELD (use Integer so it can be null)

    private static final int EXPIRY_WARNING_DAYS = 7;

    public BloodUnit(int donorId, String bloodGroup) {
        this.unitId = nextId++;
        this.donorId = donorId;
        this.bloodGroup = bloodGroup;

        this.donationDate = LocalDate.now();
        this.expiryDate = this.donationDate.plusDays(42);
        this.status = BloodStatus.IN_STOCK;
        this.recipientId = null; // <-- NEW: Explicitly null on creation
    }

    // --- Getters ---

    public int getUnitId() {
        return unitId;
    }

    public int getDonorId() {
        return donorId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BloodStatus getStatus() {
        return status;
    }
    
    public Integer getRecipientId() { // <-- NEW GETTER
        return recipientId;
    }

    // --- Setters ---

    public void setStatus(BloodStatus status) {
        this.status = status;
    }

    /**
     * This is the new, better way to issue a unit.
     * It sets the status AND logs who it went to.
     */
    public void issueToRecipient(int recipientId) { // <-- NEW METHOD
        this.status = BloodStatus.ISSUED;
        this.recipientId = recipientId;
    }

    // --- Logic Methods ---

    public boolean isExpiringSoon() {
        if (this.status != BloodStatus.IN_STOCK) {
            return false;
        }
        LocalDate warningDate = LocalDate.now().plusDays(EXPIRY_WARNING_DAYS);
        return !isExpired() && (this.expiryDate.isBefore(warningDate) || this.expiryDate.isEqual(warningDate));
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expiryDate);
    }

    public static void setNextId(int id) {
        nextId = id;
    }
}