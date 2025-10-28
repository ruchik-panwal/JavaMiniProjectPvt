// File: src/com/bloodBank/controller/DashboardController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.BloodUnit;
import com.bloodBank.model.Donor;
import com.bloodBank.model.Recipient;
import com.bloodBank.view.DashboardView;
import com.bloodBank.view.DonorInfoView;
import com.bloodBank.view.AddDonorView;
import com.bloodBank.view.BloodStockView;
import com.bloodBank.view.AddRecipientView;
import com.bloodBank.view.RecipientInfoView;
import com.bloodBank.view.RemoveByIdView; // <-- NEW: Import the custom remove view

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

        // --- Recipient Listeners ---
        this.view.addAddRecipientListener(new AddRecipientButtonListener());
        this.view.addRemoveRecipientListener(new RemoveRecipientButtonListener());
        this.view.addViewAllRecipientsListener(new ViewAllRecipientsListener());
    }

    // --- Donor Listener Classes ---

    class AddDonorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddDonorView addView = new AddDonorView(view);
            new AddDonorController(model, addView, new Runnable() {
                @Override
                public void run() {
                    System.out.println("Donor added, callback executed.");
                }
            });
            addView.setVisible(true);
        }
    }

    // --- MODIFIED: Uses the new RemoveByIdView ---
    class RemoveDonorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Create and show the new custom view
            RemoveByIdView removeView = new RemoveByIdView(view, "Remove Donor");
            removeView.setVisible(true); // This call is blocking because the dialog is modal

            // 2. Check if the user clicked "Remove" (and not "Cancel")
            if (removeView.isRemoveClicked()) {
                String idString = removeView.getId(); // Get the ID from the view

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
            // If removeView.isRemoveClicked() is false, the user clicked "Cancel", so we do nothing.
        }
    }
    
    class ViewAllDonorsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Donor> allDonors = model.getDonors();
            DonorInfoView infoView = new DonorInfoView(view);
            infoView.displayDonors(allDonors);
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

    // --- Recipient Listener Classes ---

    class AddRecipientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddRecipientView addView = new AddRecipientView(view);
            new AddRecipientController(model, addView, new Runnable() {
                @Override
                public void run() {
                    System.out.println("Recipient added, callback executed.");
                }
            });
            addView.setVisible(true);
        }
    }

    // --- MODIFIED: Uses the new RemoveByIdView ---
    class RemoveRecipientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Create and show the new custom view
            RemoveByIdView removeView = new RemoveByIdView(view, "Remove Recipient");
            removeView.setVisible(true); // This call is blocking

            // 2. Check if the user clicked "Remove"
            if (removeView.isRemoveClicked()) {
                String idString = removeView.getId(); // Get the ID from the view

                try {
                    int id = Integer.parseInt(idString.trim());
                    boolean success = model.deleteRecipientById(id);

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
            // If removeView.isRemoveClicked() is false, the user clicked "Cancel", so we do nothing.
        }
    }

    class ViewAllRecipientsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Recipient> allRecipients = model.getRecipients();
            RecipientInfoView infoView = new RecipientInfoView(view);
            infoView.displayRecipients(allRecipients);
            infoView.setVisible(true);
        }
    }
}