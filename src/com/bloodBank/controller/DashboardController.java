// File: src/com/bloodBank/controller/DashboardController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.model.BloodUnit;
import com.bloodBank.model.Donor;
import com.bloodBank.view.DashboardView;
import com.bloodBank.view.DonorInfoView;
import com.bloodBank.view.AddDonorView;
import com.bloodBank.view.BloodStockView; // NEW Import

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    private BloodBankModel model;
    private DashboardView view;

    public DashboardController(BloodBankModel model, DashboardView view) {
        this.model = model;
        this.view = view;

        // Add listeners for the new dashboard buttons
        this.view.addAddDonorListener(new AddButtonListener());
        this.view.addRemoveDonorListener(new RemoveButtonListener());
        this.view.addViewBloodStockListener(new ViewStockButtonListener());
        this.view.addViewAllDonorsListener(new ViewAllDonorsListener());
    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddDonorView addView = new AddDonorView(view);

            // The callback is now just an empty function
            new AddDonorController(model, addView, new Runnable() {
                @Override
                public void run() {
                }
            });

            addView.setVisible(true);
        }
    }

    class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Ask user for the ID
            String idString = view.showIDInputDialog("Enter the ID of the donor to remove:");

            if (idString == null || idString.trim().isEmpty()) {
                return;
            }

            try {
                int id = Integer.parseInt(idString.trim());
                boolean success = model.deleteDonorById(id);

                if (success) {
                    view.showInfoMessage("Donor with ID " + id + " has been removed.");
                } else {
                    view.showErrorMessage("No donor found with ID " + id + ".");
                }
            } catch (NumberFormatException ex) {
                view.showErrorMessage("Invalid ID. Please enter a number.");
            }
        }
    }

    class ViewStockButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Create the new view
            BloodStockView stockView = new BloodStockView(view); // 'view' is your main JFrame

            // 2. Get the NEW data from the model
            // --- MODIFICATION ---
            ArrayList<BloodUnit> allUnits = model.getBloodUnits();

            // 3. Create a controller for the new view
            // --- MODIFICATION ---
            new BloodStockController(stockView, allUnits);

            // 4. Show the view
            stockView.setVisible(true);
        }
    }

    class ViewAllDonorsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // 1. Get the list of all donors from the model
            // (You must add a getAllDonors() method to your BloodBankModel)
            List<Donor> allDonors = model.getDonors();

            // 2. Create the pop-up window (the view)
            // We pass 'view' (the main DashboardView) so the pop-up appears on top.
            DonorInfoView infoView = new DonorInfoView(view);

            // 3. Give the data to the view, which will build the table
            infoView.displayDonors(allDonors);

            // 4. Show the pop-up window
            infoView.setVisible(true);
        }
    }
}