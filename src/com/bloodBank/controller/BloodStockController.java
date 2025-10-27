// File: src/com/bloodBank/controller/BloodStockController.java
package com.bloodBank.controller;

import com.bloodBank.view.BloodStockView;
import com.bloodBank.model.BloodUnit; // <-- CHANGED: Import BloodUnit
import java.util.ArrayList; // <-- CHANGED: Import ArrayList (removed Map)

public class BloodStockController {

    /**
     * This controller is very simple. It doesn't need to listen for any events.
     * It just takes the data (now a list of units) and passes it to the view.
     */
    
    // --- MODIFICATION: Changed Map<String, Integer> to ArrayList<BloodUnit> ---
    public BloodStockController(BloodStockView view, ArrayList<BloodUnit> unitData) {
        
        // Immediately tell the view to display the data
        // --- MODIFICATION: Pass the unitData list to the view ---
        view.displayStock(unitData);
    }
}