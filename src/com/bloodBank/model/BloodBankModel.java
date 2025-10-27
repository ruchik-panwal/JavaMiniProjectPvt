// File: src/com/bloodBank/model/BloodBankModel.java
package com.bloodBank.model;

// Importing File Handlers
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// General Utils
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors; // For filtering

public class BloodBankModel {

    // Files where data is stored
    private static final String donorFile = "donors.dat";
    private static final String bloodUnitFile = "bloodUnits.dat";

    private ArrayList<Donor> donorList; // List of Donors
    private ArrayList<BloodUnit> bloodUnitList; // List of Blood Units

    public BloodBankModel() {
        this.donorList = loadDonorData();
        this.bloodUnitList = loadBloodUnitData();
        // Update statuses on load (e.g., mark newly expired units)
        updateUnitStatuses();
    }

    // --- Donor Methods ---

    public ArrayList<Donor> getDonors() {
        return new ArrayList<>(this.donorList);
    }

    public void addDonor(Donor donor) {
        this.donorList.add(donor);
        saveDonorData();

        // --- NEW LOGIC ---
        // Automatically create and add the associated blood unit
        // for this new donation.
        BloodUnit newUnit = new BloodUnit(donor.getDonorId(), donor.getBloodGroup());
        this.addBloodUnit(newUnit); // Use our existing method to add and save the unit
    }

    public boolean deleteDonorById(int id) {
        Donor donorToRemove = donorList.stream()
                .filter(d -> d.getDonorId() == id)
                .findFirst()
                .orElse(null);

        if (donorToRemove != null) {
            donorList.remove(donorToRemove);
            saveDonorData(); // Save donor list changes

            // Also remove all associated blood units from the stock
            boolean unitsRemoved = this.bloodUnitList.removeIf(unit -> unit.getDonorId() == id);
            if (unitsRemoved) {
                saveBloodUnitData(); // Save unit list changes if any were made
            }
            return true; // Success
        }
        return false; // Donor not found
    }

    // --- Blood Unit Methods ---

    public ArrayList<BloodUnit> getBloodUnits() {
        return new ArrayList<>(this.bloodUnitList);
    }

    /**
     * Adds a new BloodUnit and saves the list.
     * The unit should be created by the Controller.
     * 
     * @param unit The BloodUnit to add.
     */
    public void addBloodUnit(BloodUnit unit) {
        this.bloodUnitList.add(unit);
        saveBloodUnitData();
    }

    /**
     * Gets a map of all available blood units, grouped by blood type.
     * This now provides an accurate count of available stock.
     * * @return A Map where the key is the blood group (e.g., "A+") and
     * the value is the count of IN_STOCK units.
     */
    public Map<String, Integer> getBloodStock() {
        Map<String, Integer> stock = new HashMap<>();

        // Initialize all common blood types to 0 for a complete report
        String[] bloodTypes = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        for (String type : bloodTypes) {
            stock.put(type, 0);
        }

        // Count only units that are currently IN_STOCK
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getStatus() == BloodStatus.IN_STOCK) {
                String bloodGroup = unit.getBloodGroup().toUpperCase().trim();
                stock.put(bloodGroup, stock.getOrDefault(bloodGroup, 0) + 1);
            }
        }
        return stock;
    }

    /**
     * Finds all blood units that are IN_STOCK and expiring soon.
     * 
     * @return A list of BloodUnit objects.
     */
    public ArrayList<BloodUnit> getUnitsExpiringSoon() {
        // Use Java 8 Stream API for a clean filter
        return bloodUnitList.stream()
                .filter(unit -> unit.isExpiringSoon())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Marks a specific blood unit as USED.
     * 
     * @param unitId The ID of the unit to be used.
     * @return true if the unit was found, was IN_STOCK, and was updated. false
     *         otherwise.
     */
    public boolean useBloodUnit(int unitId) {
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getUnitId() == unitId) {
                if (unit.getStatus() == BloodStatus.IN_STOCK) {
                    unit.setStatus(BloodStatus.ISSUED);
                    saveBloodUnitData();
                    return true; // Successfully used
                } else {
                    return false; // Found, but not in stock (e.g., already used or expired)
                }
            }
        }
        return false; // Unit ID not found
    }

    /**
     * Iterates over all units and updates their status if they have expired.
     * This is useful to run when the application starts.
     */
    private void updateUnitStatuses() {
        boolean dataChanged = false;
        for (BloodUnit unit : bloodUnitList) {
            // Only check units that are currently marked as IN_STOCK
            if (unit.getStatus() == BloodStatus.IN_STOCK && unit.isExpired()) {
                unit.setStatus(BloodStatus.EXPIRED);
                dataChanged = true;
                System.out.println("Unit " + unit.getUnitId() + " marked as EXPIRED.");
            }
        }

        // If any statuses were changed, save the updated list to the file
        if (dataChanged) {
            saveBloodUnitData();
        }
    }

    // --- Data Persistence ---

    @SuppressWarnings("unchecked")
    private ArrayList<Donor> loadDonorData() {
        ArrayList<Donor> donorInpList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(donorFile))) {
            donorInpList = (ArrayList<Donor>) ois.readObject();
            System.out.println("Donor data loaded successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("No donor save file found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading donor data: " + e.getMessage());
        }

        if (!donorInpList.isEmpty()) {
            int maxId = donorInpList.stream().mapToInt(Donor::getDonorId).max().orElse(0);
            Donor.setNextId(maxId + 1);
        }
        return donorInpList;
    }

    private void saveDonorData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(donorFile))) {
            oos.writeObject(this.donorList);
        } catch (Exception e) {
            System.out.println("Error saving donor data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<BloodUnit> loadBloodUnitData() {
        ArrayList<BloodUnit> unitInpList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bloodUnitFile))) {
            unitInpList = (ArrayList<BloodUnit>) ois.readObject();
            System.out.println("Blood unit data loaded successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("No blood unit save file found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading blood unit data: " + e.getMessage());
        }

        if (!unitInpList.isEmpty()) {
            int maxId = unitInpList.stream().mapToInt(BloodUnit::getUnitId).max().orElse(0);
            BloodUnit.setNextId(maxId + 1);
        }
        return unitInpList;
    }

    private void saveBloodUnitData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bloodUnitFile))) {
            oos.writeObject(this.bloodUnitList);
        } catch (Exception e) {
            System.out.println("Error saving blood unit data: " + e.getMessage());
        }
    }
}