// File: src/com/bloodBank/controller/DashboardController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.BloodUnit;
import com.bloodBank.model.Donor;
import com.bloodBank.model.Recipient; // <-- NEW Import
import com.bloodBank.view.DashboardView;
import com.bloodBank.view.DonorInfoView;
import com.bloodBank.view.AddDonorView;
import com.bloodBank.view.BloodStockView;
import com.bloodBank.view.AddRecipientView; // <-- NEW Import
import com.bloodBank.view.RecipientInfoView; // <-- NEW Import

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DashboardController {

    private BloodBankModel model;
    private DashboardView view;

    public DashboardController(BloodBankModel model, DashboardView view) {
        this.model = model;
        this.view = view;

        // --- Donor Listeners ---
        this.view.addAddDonorListener(new AddDonorButtonListener());
        this.view.addRemoveDonorListener(new RemoveDonorButtonListener());
        this.view.addViewAllDonorsListener(new ViewAllDonorsListener());
        
        // --- Stock Listener ---
        this.view.addViewBloodStockListener(new ViewStockButtonListener());

        // --- NEW Recipient Listeners ---
        this.view.addAddRecipientListener(new AddRecipientButtonListener());
        this.view.addRemoveRecipientListener(new RemoveRecipientButtonListener());
        this.view.addViewAllRecipientsListener(new ViewAllRecipientsListener());
    }

    // --- Donor Listener Classes ---

    class AddDonorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddDonorView addView = new AddDonorView(view);

            // Create a callback to run on success (e.g., refresh a table)
            new AddDonorController(model, addView, new Runnable() {
                @Override
                public void run() {
                    // This is where you would refresh the main donor table if you had one
                    System.out.println("Donor added, callback executed.");
                }
            });
            addView.setVisible(true);
        }
    }

    class RemoveDonorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Ask user for the ID
            String idString = view.showDonorIDInputDialog("Enter the ID of the donor to remove:");

            if (idString == null || idString.trim().isEmpty()) {
                return;
            }

            try {
                int id = Integer.parseInt(idString.trim());
                boolean success = model.deleteDonorById(id);

                if (success) {
                    view.showInfoMessage("Donor with ID " + id + " has been removed.");
                    // You would refresh your main donor table here
                } else {
                    view.showErrorMessage("No donor found with ID " + id + ".");
                }
            } catch (NumberFormatException ex) {
                view.showErrorMessage("Invalid ID. Please enter a number.");
            }
        }
    }
    
    class ViewAllDonorsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Get the list of all donors from the model
            ArrayList<Donor> allDonors = model.getDonors();

            // 2. Create the pop-up window (the view)
            DonorInfoView infoView = new DonorInfoView(view);

            // 3. Give the data to the view, which will build the table
            infoView.displayDonors(allDonors);

            // 4. Show the pop-up window
            infoView.setVisible(true);
        }
    }

    // --- Stock Listener Class ---

    class ViewStockButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BloodStockView stockView = new BloodStockView(view);
            ArrayList<BloodUnit> allUnits = model.getBloodUnits();
            new BloodStockController(stockView, allUnits);
            stockView.setVisible(true);
        }
    }

    // --- NEW Recipient Listener Classes ---

    class AddRecipientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddRecipientView addView = new AddRecipientView(view);

            // Create a callback to run on success
            new AddRecipientController(model, addView, new Runnable() {
                @Override
                public void run() {
                    // This is where you would refresh the main recipient table
                    System.out.println("Recipient added, callback executed.");
                }
            });
            addView.setVisible(true);
        }
    }

    class RemoveRecipientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Ask user for the ID
            String idString = view.showRecipientIDInputDialog("Enter the ID of the recipient to remove:");

            if (idString == null || idString.trim().isEmpty()) {
                return;
            }

            try {
                int id = Integer.parseInt(idString.trim());
                boolean success = model.deleteRecipientById(id); // <-- Calls new model method

                if (success) {
                    view.showInfoMessage("Recipient with ID " + id + " has been removed.");
                    // You would refresh your main recipient table here
                } else {
                    view.showErrorMessage("No recipient found with ID " + id + ".");
                }
            } catch (NumberFormatException ex) {
                view.showErrorMessage("Invalid ID. Please enter a number.");
            }
        }
    }

    class ViewAllRecipientsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Get the list of all recipients from the model
            ArrayList<Recipient> allRecipients = model.getRecipients(); // <-- Calls new model method

            // 2. Create the pop-up window (the view)
            // (Assumes you have created RecipientInfoView similar to DonorInfoView)
            RecipientInfoView infoView = new RecipientInfoView(view);

            // 3. Give the data to the view, which will build the table
            // (Assumes RecipientInfoView has a 'displayRecipients' method)
            infoView.displayRecipients(allRecipients);

            // 4. Show the pop-up window
            infoView.setVisible(true);
        }
    }
}