// File: src/com/bloodBank/controller/RecipientInfoController.java
package com.bloodBank.controller;

import com.bloodBank.model.Recipient; // <-- CHANGED
import com.bloodBank.view.RecipientInfoView; // <-- CHANGED
import java.util.List;

/**
 * This controller is very simple, just like DonorInfoController.
 * It just takes the data and passes it to the view's display method.
 * It is NOT an ActionListener.
 */
public class RecipientInfoController {

    public RecipientInfoController(RecipientInfoView view, List<Recipient> recipientList) { // <-- CHANGED
        // Immediately tell the view to display the data
        view.displayRecipients(recipientList); // <-- CHANGED
    }
}