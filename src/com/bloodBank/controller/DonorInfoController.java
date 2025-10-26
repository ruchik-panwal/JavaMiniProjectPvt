package com.bloodBank.controller;

import com.bloodBank.model.Donor;
import com.bloodBank.view.DonorInfoView;
import java.util.List;

/**
 * This controller is very simple, just like BloodStockController.
 * It just takes the data and passes it to the view's display method.
 * It is NOT an ActionListener.
 */
public class DonorInfoController {

    public DonorInfoController(DonorInfoView view, List<Donor> donorList) {
        // Immediately tell the view to display the data
        view.displayDonors(donorList);
    }
}