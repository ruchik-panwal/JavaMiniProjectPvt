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
    private static final String recipientFile = "recipients.dat";

    private ArrayList<Donor> donorList; // List of Donors
    private ArrayList<BloodUnit> bloodUnitList; // List of Blood Units
    private ArrayList<Recipient> recipientList;

    public BloodBankModel() {
        this.donorList = loadDonorData();
        this.bloodUnitList = loadBloodUnitData();
        this.recipientList = loadRecipientData();
        
        // Update statuses on load (e.g., mark newly expired units)
        updateUnitStatuses();
    }

    // --- Donor Methods ---

    public ArrayList<Donor> getDonors() {
        return new ArrayList<>(this.donorList);
    }

    /**
     * Adds a new donor, creates their blood unit, and checks the waiting list.
     * @param donor The new Donor object to add.
     * @return A String message indicating the result (e.g., auto-issued to waiting list).
     */
    public String addDonor(Donor donor) {
        this.donorList.add(donor);
        saveDonorData(); // Save the new donor

        // Automatically create the associated blood unit
        BloodUnit newUnit = new BloodUnit(donor.getDonorId(), donor.getBloodGroup());
        this.bloodUnitList.add(newUnit); // Add to the list in memory
        
        // --- NEW LOGIC ---
        // Check if the new unit can fulfill a waiting list request *before* saving
        String autoIssueMessage = processWaitingListForUnit(newUnit);
        
        // Now, save the blood unit list. Its status will be either 
        // IN_STOCK (if no match) or ISSUED (if a match was found).
        saveBloodUnitData(); 
        
        if (autoIssueMessage != null) {
            // The process method already saved the recipient data
            System.out.println("WAITING LIST UPDATE: " + autoIssueMessage);
            return "Donor added successfully.\n" + autoIssueMessage;
        } else {
            // No auto-issue, just return a simple success message
            return "Donor added successfully. Unit " + newUnit.getUnitId() + " is now in stock.";
        }
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

    // --- Recipient Methods ---

    /**
     * Gets a copy of the current recipient list (both pending and completed).
     * @return A new ArrayList of Recipient objects.
     */
    public ArrayList<Recipient> getRecipients() {
        return new ArrayList<>(this.recipientList);
    }

    /**
     * (This method is no longer recommended for issuing blood)
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

    // --- Transaction & Waiting List Methods ---

    /**
     * Tries to issue one unit of blood.
     * If successful, unit is marked ISSUED and Recipient is marked as RECEIVED.
     * If unsuccessful, Recipient is added to the log as "pending" (waiting list).
     *
     * @param recipient The recipient object (used for logging and to get blood group).
     * @return A String message indicating success or waiting list status.
     */
    public String issueOneUnitToRecipient(Recipient recipient) {

        String requestedBloodGroup = recipient.getBloodGroup();
        String recipientName = recipient.getFirstName() + " " + recipient.getLastName();

        // 1. Try to find an available unit
        BloodUnit unitToIssue = this.bloodUnitList.stream()
            .filter(unit -> unit.getBloodGroup().equals(requestedBloodGroup) && 
                            unit.getStatus() == BloodStatus.IN_STOCK)
            .findFirst() // Get the first available unit
            .orElse(null); // Or null if none is found

        // 2. Perform transaction based on result
        if (unitToIssue != null) {
            // --- SUCCESS CASE (Blood is available) ---
            
            // Step A: Mark unit as ISSUED and link to recipient ID
            unitToIssue.issueToRecipient(recipient.getRecipientId()); // <-- UPDATED
            
            // Step B: Mark recipient as RECEIVED (sets the date)
            recipient.markAsReceived(); 
            
            // Step C: Add the *completed* recipient to the log
            this.recipientList.add(recipient); 
            
            // Step D: Save both files
            saveBloodUnitData();
            saveRecipientData();
            
            return "Success: Issued 1 unit of " + requestedBloodGroup + 
                   " to " + recipientName + " (Unit ID: " + unitToIssue.getUnitId() + ")";
        } else {
            // --- WAITING LIST CASE (Blood not available) ---
            
            // Step A: The recipient's 'dateReceived' is still null, 
            // so they are automatically "pending".
            
            // Step B: Add the *pending* recipient to the log
            this.recipientList.add(recipient); 
            
            // Step C: Save the recipient list
            saveRecipientData();
            
            return "Blood not available. " + recipientName + 
                   " has been added to the waiting list.";
        }
    }

    /**
     * Checks the waiting list for a recipient who needs this specific new unit.
     * If a match is found, it updates both objects and saves the recipient list.
     * NOTE: This method does NOT save the bloodUnitList. The calling method must do that.
     * @param newUnit The newly donated blood unit (which is NOT yet saved).
     * @return A message if a waiting list recipient was fulfilled, or null otherwise.
     */
    private String processWaitingListForUnit(BloodUnit newUnit) {
        // Find the first recipient on the waiting list who needs this blood type
        Recipient recipientToFulfill = this.getWaitingList().stream()
            .filter(r -> r.getBloodGroup().equals(newUnit.getBloodGroup()))
            .findFirst()
            .orElse(null);
            
        if (recipientToFulfill != null) {
            // --- We found a match! Fulfill the request ---
            
            // Step A: Mark the unit as ISSUED and link to recipient ID
            newUnit.issueToRecipient(recipientToFulfill.getRecipientId()); // <-- UPDATED
            
            // Step B: Mark the recipient as RECEIVED
            recipientToFulfill.markAsReceived();
            
            // Step C: Save the RECIPIENT list (which already contained this recipient)
            saveRecipientData();
            
            // Return a message
            return "Auto-issued new unit " + newUnit.getUnitId() + 
                   " to waiting recipient: " + recipientToFulfill.getFirstName() +
                   " (ID: " + recipientToFulfill.getRecipientId() + ")";
        }
        
        // No one on the waiting list needed this type
        return null; 
    }

    /**
     * Gets a list of all recipients who are still waiting for blood.
     * @return An ArrayList of Recipient objects where 'dateReceived' is null.
     */
    public ArrayList<Recipient> getWaitingList() {
        return this.recipientList.stream()
            .filter(recipient -> !recipient.didReceiveUnit()) // or recipient.didReceiveUnit() == false
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calculates the total number of units needed for the waiting list,
     * grouped by blood type.
     * @return A Map<String, Integer> where key is blood type and value is count.
     */
    public Map<String, Integer> getShortageByBloodType() {
        // Get the list of people waiting
        ArrayList<Recipient> waitingList = getWaitingList();
        
        // Group them by blood type and count how many are in each group
        Map<String, Long> rawShortage = waitingList.stream()
            .collect(Collectors.groupingBy(
                Recipient::getBloodGroup, // The key to group by
                Collectors.counting()     // How to count them
            ));

        // Convert the Map<String, Long> to Map<String, Integer>
        Map<String, Integer> shortage = new HashMap<>();
        for (Map.Entry<String, Long> entry : rawShortage.entrySet()) {
            shortage.put(entry.getKey(), entry.getValue().intValue());
        }

        // Ensure all blood types are present in the map, even if count is 0
        String[] bloodTypes = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        for (String type : bloodTypes) {
            shortage.putIfAbsent(type, 0);
        }
        
        return shortage;
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
        return bloodUnitList.stream()
                .filter(unit -> unit.isExpiringSoon())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean useBloodUnit(int unitId) {
        for (BloodUnit unit : bloodUnitList) {
            if (unit.getUnitId() == unitId) {
                if (unit.getStatus() == BloodStatus.IN_STOCK) {
                    unit.setStatus(BloodStatus.ISSUED); // This is now a simple "mark used"
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

    // --- Recipient Persistence ---

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

    private void saveRecipientData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(recipientFile))) {
            oos.writeObject(this.recipientList);
        } catch (Exception e) {
            System.out.println("Error saving recipient data: " + e.getMessage());
        }
    }
}