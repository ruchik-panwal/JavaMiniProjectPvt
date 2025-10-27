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
    private static final String recipientFile = "recipients.dat"; // <-- NEW

    private ArrayList<Donor> donorList; // List of Donors
    private ArrayList<BloodUnit> bloodUnitList; // List of Blood Units
    private ArrayList<Recipient> recipientList; // <-- NEW

    public BloodBankModel() {
        this.donorList = loadDonorData();
        this.bloodUnitList = loadBloodUnitData();
        this.recipientList = loadRecipientData(); // <-- NEW
        
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

        // Automatically create and add the associated blood unit
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

    // --- Recipient Methods --- // <-- NEW SECTION

    /**
     * Gets a copy of the current recipient list.
     * @return A new ArrayList of Recipient objects.
     */
    public ArrayList<Recipient> getRecipients() {
        return new ArrayList<>(this.recipientList);
    }

    /**
     * Adds a new recipient to the list and saves to file.
     * @param recipient The Recipient object to add.
     */
    public void addRecipient(Recipient recipient) {
        this.recipientList.add(recipient);
        saveRecipientData();
    }

    /**
     * Deletes a recipient from the list by their ID.
     * @param id The ID of the recipient to remove.
     * @return true if a recipient was found and removed, false otherwise.
     */
    public boolean deleteRecipientById(int id) {
        Recipient recipientToRemove = recipientList.stream()
                .filter(r -> r.getRecipientId() == id)
                .findFirst()
                .orElse(null);

        if (recipientToRemove != null) {
            recipientList.remove(recipientToRemove);
            saveRecipientData();
            return true; // Success
        }
        return false; // Recipient not found
    }

    // --- Blood Unit Methods ---

    public ArrayList<BloodUnit> getBloodUnits() {
        return new ArrayList<>(this.bloodUnitList);
    }

    public void addBloodUnit(BloodUnit unit) {
        this.bloodUnitList.add(unit);
        saveBloodUnitData();
    }

    public Map<String, Integer> getBloodStock() {
        // ... (method unchanged)
        Map<String, Integer> stock = new HashMap<>();
        String[] bloodTypes = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        for (String type : bloodTypes) {
            stock.put(type, 0);
        }
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getStatus() == BloodStatus.IN_STOCK) {
                String bloodGroup = unit.getBloodGroup().toUpperCase().trim();
                stock.put(bloodGroup, stock.getOrDefault(bloodGroup, 0) + 1);
            }
        }
        return stock;
    }

    public ArrayList<BloodUnit> getUnitsExpiringSoon() {
        // ... (method unchanged)
        return bloodUnitList.stream()
                .filter(unit -> unit.isExpiringSoon())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean useBloodUnit(int unitId) {
        // ... (method unchanged)
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getUnitId() == unitId) {
                if (unit.getStatus() == BloodStatus.IN_STOCK) {
                    unit.setStatus(BloodStatus.ISSUED);
                    saveBloodUnitData();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void updateUnitStatuses() {
        // ... (method unchanged)
        boolean dataChanged = false;
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getStatus() == BloodStatus.IN_STOCK && unit.isExpired()) {
                unit.setStatus(BloodStatus.EXPIRED);
                dataChanged = true;
                System.out.println("Unit " + unit.getUnitId() + " marked as EXPIRED.");
            }
        }
        if (dataChanged) {
            saveBloodUnitData();
        }
    }

    // --- Data Persistence ---

    @SuppressWarnings("unchecked")
    private ArrayList<Donor> loadDonorData() {
        // ... (method unchanged)
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
        // ... (method unchanged)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(donorFile))) {
            oos.writeObject(this.donorList);
        } catch (Exception e) {
            System.out.println("Error saving donor data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<BloodUnit> loadBloodUnitData() {
        // ... (method unchanged)
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
        // ... (method unchanged)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bloodUnitFile))) {
            oos.writeObject(this.bloodUnitList);
        } catch (Exception e) {
            System.out.println("Error saving blood unit data: " + e.getMessage());
        }
    }

    // --- NEW METHODS for Recipient Persistence ---

    /**
     * Loads the Recipient Data from the file.
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Recipient> loadRecipientData() {
        ArrayList<Recipient> recipientInpList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(recipientFile))) {
            recipientInpList = (ArrayList<Recipient>) ois.readObject();
            System.out.println("Recipient data loaded successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("No recipient save file found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading recipient data: " + e.getMessage());
        }

        // Logic to set the next Recipient ID
        if (!recipientInpList.isEmpty()) {
            int maxId = recipientInpList.stream().mapToInt(Recipient::getRecipientId).max().orElse(0);
            Recipient.setNextId(maxId + 1);
        }
        return recipientInpList;
    }

    /**
     * Stores the Recipient data to the file.
     */
    private void saveRecipientData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(recipientFile))) {
            oos.writeObject(this.recipientList);
        } catch (Exception e) {
            System.out.println("Error saving recipient data: " + e.getMessage());
        }
    }
}