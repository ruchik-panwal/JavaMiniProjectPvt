// File: src/com/bloodBank/controller/DashboardController.java
package com.bloodBank.controller;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.view.DashboardView;
import com.bloodBank.view.AddDonorView;
import com.bloodBank.view.BloodStockView; // NEW Import

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map; // NEW Import

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
            BloodStockView stockView = new BloodStockView(view);
            
            // 2. Get the data from the model
            Map<String, Integer> stock = model.getBloodStock();
            
            // 3. Create a controller for the new view
            new BloodStockController(stockView, stock);
            
            // 4. Show the view
            stockView.setVisible(true);
        }
    }
}