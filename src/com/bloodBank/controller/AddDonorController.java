// File: src/com/bloodBank/controller/AddDonorController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.Donor;
import com.bloodBank.view.AddDonorView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddDonorController {

    private BloodBankModel model;
    private AddDonorView view;
    private Runnable onAddSuccessCallback;

    public AddDonorController(BloodBankModel model, AddDonorView view, Runnable onAddSuccessCallback) {
        this.model = model;
        this.view = view;
        this.onAddSuccessCallback = onAddSuccessCallback;

        this.view.addAddButtonListener(new AddDonorListener());
    }

    class AddDonorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                String firstName = view.getFirstName();
                String lastName = view.getLastName();
                String fatherName = view.getFatherName();
                String motherName = view.getMotherName();
                String dobString = view.getDobString();
                String mobileNo = view.getMobileNo();
                String gender = view.getGender();
                String email = view.getEmail();
                String bloodGroup = view.getBloodGroup();
                String city = view.getCity();
                String fullAddress = view.getFullAddress();

                // 2. Validate essential inputs
                if (firstName.isEmpty() || lastName.isEmpty() || mobileNo.isEmpty() || bloodGroup.isEmpty()
                        || dobString.isEmpty()) {
                    view.showErrorMessage(
                            "Please fill in all required fields:\nFirst Name, Last Name, DOB, Mobile No, Blood Group.");
                    return;
                }

                // Date Input
                LocalDate dob = parseDate(dobString);
                if (dob == null) {
                    view.showErrorMessage("Invalid Date Format. Please use YYYY-MM-DD.");
                    return; // Stop execution
                }

                Donor newDonor = new Donor(
                        firstName, lastName, fatherName, motherName,
                        dob, mobileNo, gender, email,
                        bloodGroup, city, fullAddress);

                // --- MODIFIED LINES ---
                // 1. Capture the status message from the model
                String successMessage = model.addDonor(newDonor);
                
                // 2. Show the message from the model (e.g., "Auto-issued to...")
                view.showSuccessMessage(successMessage);
                // --- END OF MODIFICATIONS ---

                view.dispose();

                // Run the callback function to tell the dashboard to refresh
                onAddSuccessCallback.run();

            } catch (Exception ex) {
                view.showErrorMessage("An error occurred: ".trim() + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // Funtion to parse date
        private LocalDate parseDate(String dateString) {
            try {
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException ex) {
                return null; // If Failed
            }
        }
    }
}