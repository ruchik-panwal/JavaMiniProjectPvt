// File: src/com/bloodBank/Main.java
package com.bloodBank;

import com.bloodBank.model.BloodBankModel;
import com.bloodBank.view.DashboardView; // CHANGED
import com.bloodBank.controller.DashboardController; // CHANGED

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // 1. Create the Model (loads data)
                BloodBankModel model = new BloodBankModel();
                
                // 2. Create the new Dashboard View
                DashboardView view = new DashboardView();
                
                // 3. Create the new Dashboard Controller
                new DashboardController(model, view); // CHANGED
                
                // 4. Make the application visible
                view.setVisible(true);
            }
        });
    }
}