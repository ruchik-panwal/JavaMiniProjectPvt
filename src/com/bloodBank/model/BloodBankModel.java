// File: src/com/bloodBank/model/BloodBankModel.java
package com.bloodBank.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodBankModel {

    private static final String dataFile = "donors.dat";
    private ArrayList<Donor> donorList;

    public BloodBankModel() {
        this.donorList = loadData();
    }

    public ArrayList<Donor> getDonors() {
        return new ArrayList<>(this.donorList);
    }

    public void addDonor(Donor donor) {
        this.donorList.add(donor);
        saveData();
    }

    public void deleteDonor(Donor donor) {
        this.donorList.remove(donor);
        saveData();
    }

    public boolean deleteDonorById(int id) {
        
        Donor donorToRemove = null;
        
        for (Donor d : donorList) {
            if (d.getDonorId() == id) {
                donorToRemove = d;
                break;
            }
        }

        if (donorToRemove != null) {
            donorList.remove(donorToRemove);
            saveData(); // Save changes
            return true; // Success
        }

        return false; // Donor not found
    }

    public Map<String, Integer> getBloodStock() {
        // Use a HashMap to store the counts
        Map<String, Integer> stock = new HashMap<>();

        for (Donor d : donorList) {
            String bloodGroup = d.getBloodGroup().toUpperCase().trim();
            // Get the current count (or 0 if it's not in the map yet)
            int currentCount = stock.getOrDefault(bloodGroup, 0);
            // Put the incremented count back into the map
            stock.put(bloodGroup, currentCount + 1);
        }

        return stock;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Donor> loadData() {

        ArrayList<Donor> list = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            list = (ArrayList<Donor>) ois.readObject();
            System.out.println("Data loaded successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("No save file found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        // Logic to set the next Donor ID
        if (!list.isEmpty()) {
            int maxId = 0;
            for (Donor d : list) {
                if (d.getDonorId() > maxId) {
                    maxId = d.getDonorId();
                }
            }
            Donor.setNextId(maxId + 1);
        }
        return list;
    }

    private void saveData() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(this.donorList);
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }

    }
}