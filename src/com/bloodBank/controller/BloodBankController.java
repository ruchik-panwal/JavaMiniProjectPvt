package com.bloodBank.controller;

// Importing Project Packages
import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.Donor;
import com.bloodBank.view.BloodBankView;

// Importing Java Packages
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate; // For Date of Birth
import java.time.format.DateTimeParseException; // Import for date parsing errors



public class BloodBankController {

    private BloodBankModel model;
    private BloodBankView view;

    public BloodBankController(BloodBankModel model, BloodBankView view) {
        this.model = model;
        this.view = view;

        // "Listen" for the add button click in the View
        this.view.addAddButtonListener(new AddDonorListener());

        // Initialize the view with data from the model
        updateView();
    }

    /**
     * A private helper to refresh the view with the model's current data.
     */
    private void updateView() {
        view.refreshDonorList(model.getDonors());
    }

    // --- Inner class for handling the "Add" button event ---
    class AddDonorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 1. Get all inputs from View
                String firstName = view.getFirstName();
                String lastName = view.getLastName();
                String fatherName = view.getFatherName();
                String motherName = view.getMotherName();
                String dobString = view.getDobString(); // Get DOB as a String
                String mobileNo = view.getMobileNo();
                String gender = view.getGender();
                String email = view.getEmail();
                String bloodGroup = view.getBloodGroup();
                String city = view.getCity();
                String fullAddress = view.getFullAddress();

                // 2. Validate essential inputs
                if (firstName.isEmpty() || lastName.isEmpty() || mobileNo.isEmpty() || bloodGroup.isEmpty() || dobString.isEmpty()) {
                    view.showErrorMessage("Please fill in all required fields:\nFirst Name, Last Name, DOB, Mobile No, Blood Group.");
                    return;
                }

                // 3. Parse Date of Birth (String to LocalDate)
                LocalDate dob;
                try {
                    // This assumes the view provides the date in "YYYY-MM-DD" format
                    dob = LocalDate.parse(dobString);
                } catch (DateTimeParseException ex) {
                    view.showErrorMessage("Invalid Date Format. Please use YYYY-MM-DD.");
                    return;
                }

                // 4. Update Model
                Donor newDonor = new Donor(
                        firstName, lastName, fatherName, motherName,
                        dob, mobileNo, gender, email,
                        bloodGroup, city, fullAddress
                );
                
                model.addDonor(newDonor); // The model handles the saving

                // 5. Update View
                updateView(); // Re-fetch all data and display it
                view.clearInputFields(); // Assumes this method now clears all new fields
                view.showSuccessMessage("Donor added successfully!");

            } catch (Exception ex) {
                // General catch block for any other unexpected errors
                view.showErrorMessage("An error occurred: ".trim() + ex.getMessage());
                ex.printStackTrace(); // Good for debugging
            }
        }
    }
}