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
    private static final int EXPIRY_WARNING_DAYS = 7;

    public BloodUnit(int donorId, String bloodGroup) {
        this.unitId = nextId++;
        this.donorId = donorId;
        this.bloodGroup = bloodGroup;

        this.donationDate = LocalDate.now(); // Set donation date to today
        this.expiryDate = this.donationDate.plusDays(42); // Standard 42-day expiry
        this.status = BloodStatus.IN_STOCK; // Set status to available
    }

    // Getters

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

    public void setStatus(BloodStatus status) {
        this.status = status;
    }

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

    /**
     * This is called by the BloodBankModel when loading data from a file
     * to ensure new units don't re-use old IDs.
     * 
     * @param id The next ID to be used.
     */
    public static void setNextId(int id) {
        nextId = id;
    }
}