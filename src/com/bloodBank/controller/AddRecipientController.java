// File: src/com/bloodBank/controller/AddRecipientController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.Recipient; // <-- CHANGED
import com.bloodBank.view.AddRecipientView; // <-- CHANGED

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddRecipientController {

    private BloodBankModel model;
    private AddRecipientView view; // <-- CHANGED
    private Runnable onAddSuccessCallback;

    public AddRecipientController(BloodBankModel model, AddRecipientView view, Runnable onAddSuccessCallback) { // <-- CHANGED
        this.model = model;
        this.view = view;
        this.onAddSuccessCallback = onAddSuccessCallback;

        // The view's button listener method is named the same
        this.view.addAddButtonListener(new AddRecipientListener()); // <-- CHANGED
    }

    class AddRecipientListener implements ActionListener { // <-- CHANGED
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                // 1. Get all data from the view
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
                String reasonForTransfusion = view.getReasonForTransfusion(); // <-- NEW FIELD

                // 2. Validate essential inputs
                if (firstName.isEmpty() || lastName.isEmpty() || mobileNo.isEmpty() || bloodGroup.isEmpty()
                        || dobString.isEmpty()) {
                    view.showErrorMessage(
                            "Please fill in all required fields:\nFirst Name, Last Name, DOB, Mobile No, Blood Group.");
                    return;
                }

                // 3. Parse Date Input
                LocalDate dob = parseDate(dobString);
                if (dob == null) {
                    view.showErrorMessage("Invalid Date Format. Please use YYYY-MM-DD.");
                    return; // Stop execution
                }

                // 4. Create new Recipient object
                Recipient newRecipient = new Recipient( // <-- CHANGED
                        firstName, lastName, fatherName, motherName,
                        dob, mobileNo, gender, email,
                        bloodGroup, city, fullAddress,
                        reasonForTransfusion); // <-- NEW FIELD ADDED

                // 5. Add to model
                model.addRecipient(newRecipient); // <-- CHANGED
                
                // 6. Give feedback and close
                view.showSuccessMessage("Recipient added successfully!"); // <-- CHANGED
                view.dispose();

                // 7. Run the callback function to tell the dashboard to refresh
                onAddSuccessCallback.run();

            } catch (Exception ex) {
                view.showErrorMessage("An error occurred: ".trim() + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // Funtion to parse date (identical to AddDonorController)
        private LocalDate parseDate(String dateString) {
            try {
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException ex) {
                return null; // If Failed
            }
        }
    }
}