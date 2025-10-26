// File: src/com/bloodBank/controller/BloodStockController.java
package com.bloodBank.controller;

import com.bloodBank.view.BloodStockView;
import java.util.Map;

public class BloodStockController {

    /**
     * This controller is very simple. It doesn't need to listen for any events.
     * It just takes the data and passes it to the view.
     */
    public BloodStockController(BloodStockView view, Map<String, Integer> stockData) {
        // Immediately tell the view to display the data
        view.displayStock(stockData);
    }
}