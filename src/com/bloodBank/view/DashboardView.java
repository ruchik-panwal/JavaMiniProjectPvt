// File: src/com/bloodBank/view/DashboardView.java
package com.bloodBank.view;

import com.bloodBank.view.components.ModernButton; // Import your custom button
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {

    // --- Components ---
    private JButton addDonorButton;
    private JButton removeDonorButton;
    private JButton viewBloodStockButton;
    private JButton viewDonorsButton;
    
    // --- NEW Recipient Components ---
    private JButton addRecipientButton;
    private JButton removeRecipientButton;
    private JButton viewRecipientsButton;


    // Constructor
    public DashboardView() {
        initFrame(); //FrameMaker
        initTitlePanel(); //Adding Title
        initButtonPanel(); //Adding Buttons

        setLocationRelativeTo(null);
    }

    // Frame Setup
    private void initFrame() {
        setTitle("Blood Bank Dashboard");
        setSize(1900, 1000); // Kept your size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    // Title Creation
    private void initTitlePanel() {
        JLabel titleLabel = new JLabel("Blood Bank Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
    }

    // Button Panel
    private void initButtonPanel() {
        
        // --- MODIFIED: Changed to a 2x4 grid to fit all 7 buttons ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 15, 15)); // 2 rows, 4 cols, 15px gaps
        buttonPanel.setBorder(new EmptyBorder(20, 50, 50, 50)); // Add padding

        // --- Row 1: Donors & Stock ---
        addDonorButton = new ModernButton("Add New Donor", new Color(125, 194, 93)); // Green
        removeDonorButton = new ModernButton("Remove Donor by ID", new Color(184, 77, 75)); // Red
        viewDonorsButton = new ModernButton("View All Donors", new Color(65, 141, 176)); // Blue
        viewBloodStockButton = new ModernButton("View Blood Stock", new Color(20, 100, 140)); // Darker Blue

        // --- Row 2: Recipients ---
        addRecipientButton = new ModernButton("Add New Recipient", new Color(125, 194, 93)); // Green
        removeRecipientButton = new ModernButton("Remove Recipient by ID", new Color(184, 77, 75)); // Red
        
        // --- FIX: This line was missing, causing the NullPointerException ---
        viewRecipientsButton = new ModernButton("View All Recipients", new Color(65, 141, 176)); // Blue


        // --- Adding to the panel ---
        // Row 1
        buttonPanel.add(addDonorButton);
        buttonPanel.add(removeDonorButton);
        buttonPanel.add(viewDonorsButton);
        buttonPanel.add(viewBloodStockButton); // Stock button is on row 1
        
        // Row 2
        buttonPanel.add(addRecipientButton);
        buttonPanel.add(removeRecipientButton);
        buttonPanel.add(viewRecipientsButton); // <-- FIX: Added button to panel
        // The 8th slot in the 2x4 grid will be empty, which is fine.

        // Took a wrapper for sizing change
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.add(buttonPanel);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    // --- Donor Listeners ---
    public void addAddDonorListener(ActionListener listener) {
        addDonorButton.addActionListener(listener);
    }

    public void addRemoveDonorListener(ActionListener listener) {
        removeDonorButton.addActionListener(listener);
    }

    public void addViewAllDonorsListener(ActionListener listener) {
        viewDonorsButton.addActionListener(listener);
    }

    // --- Recipient Listeners (NEW) ---
    public void addAddRecipientListener(ActionListener listener) {
        addRecipientButton.addActionListener(listener);
    }

    public void addRemoveRecipientListener(ActionListener listener) {
        removeRecipientButton.addActionListener(listener);
    }

    public void addViewAllRecipientsListener(ActionListener listener) {
        viewRecipientsButton.addActionListener(listener); // This will no longer be null
    }

    // --- Stock Listener ---
    public void addViewBloodStockListener(ActionListener listener) {
        viewBloodStockButton.addActionListener(listener);
    }
    
    // --- Dialogs ---
    public String showDonorIDInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message, "Remove Donor", JOptionPane.QUESTION_MESSAGE);
    }

    // --- NEW Dialog for Recipient ID ---
    public String showRecipientIDInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message, "Remove Recipient", JOptionPane.QUESTION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}