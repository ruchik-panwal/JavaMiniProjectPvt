// File: src/com/bloodBank/view/DashboardView.java
package com.bloodBank.view;

import com.bloodBank.view.components.ModernButton; // Import your custom button
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {

    // --- NEW: A consistent color palette for beautification ---
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 249);
    private static final Color COLOR_FOREGROUND = new Color(50, 50, 50);
    private static final Color COLOR_BORDER = new Color(200, 200, 200);

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
        setSize(1960, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // --- BEAUTIFY: Set a clean background color for the whole frame ---
        getContentPane().setBackground(COLOR_BACKGROUND);
    }

    // Title Creation
    private void initTitlePanel() {
        JLabel titleLabel = new JLabel("Blood Bank Management System", SwingConstants.CENTER);
        
        // --- MODIFIED: Increased font size and padding ---
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 54)); // Much larger font
        titleLabel.setBorder(new EmptyBorder(40, 20, 40, 20)); // More padding for the large font
        
        // --- BEAUTIFY: Set a modern foreground color ---
        titleLabel.setForeground(COLOR_FOREGROUND);
        
        add(titleLabel, BorderLayout.NORTH);
    }

    // --- REFACTORED Button Panel ---
    private void initButtonPanel() {
        
        // 1. Create the main panel with 3 columns
        JPanel mainButtonPanel = new JPanel(new GridLayout(1, 3, 40, 40));
        mainButtonPanel.setBorder(new EmptyBorder(20, 40, 40, 40));
        mainButtonPanel.setBackground(COLOR_BACKGROUND); // BEAUTIFY: Match frame background

        // 2. Instantiate all the buttons
        // --- Donor Buttons ---
        addDonorButton = new ModernButton("Add New Donor", new Color(125, 194, 93)); // Green
        removeDonorButton = new ModernButton("Remove Donor by ID", new Color(184, 77, 75)); // Red
        viewDonorsButton = new ModernButton("View All Donors", new Color(65, 141, 176)); // Blue
        
        // --- Recipient Buttons ---
        addRecipientButton = new ModernButton("Add New Recipient", new Color(125, 194, 93)); // Green
        removeRecipientButton = new ModernButton("Remove Recipient by ID", new Color(184, 77, 75)); // Red
        viewRecipientsButton = new ModernButton("View All Recipients", new Color(65, 141, 176)); // Blue
        
        // --- Stock Button ---
        viewBloodStockButton = new ModernButton("View Blood Stock", new Color(20, 100, 140)); // Darker Blue

        // 3. Create and populate Group 1: Donor Panel
        JPanel donorPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        donorPanel.setBorder(createStyledBorder("Donor Management"));
        donorPanel.setBackground(COLOR_BACKGROUND); // BEAUTIFY: Match frame background
        donorPanel.add(addDonorButton);
        donorPanel.add(removeDonorButton);
        donorPanel.add(viewDonorsButton);

        // 4. Create and populate Group 2: Recipient Panel
        JPanel recipientPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        recipientPanel.setBorder(createStyledBorder("Recipient Management"));
        recipientPanel.setBackground(COLOR_BACKGROUND); // BEAUTIFY: Match frame background
        recipientPanel.add(addRecipientButton);
        recipientPanel.add(removeRecipientButton);
        recipientPanel.add(viewRecipientsButton);

        // 5. Create and populate Group 3: Stock Panel
        JPanel stockPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        stockPanel.setBorder(createStyledBorder("Blood Stock"));
        stockPanel.setBackground(COLOR_BACKGROUND); // BEAUTIFY: Match frame background
        stockPanel.add(viewBloodStockButton);
        // The other 2 slots are left empty, keeping the button large and at the top.

        // 6. Add the 3 group panels to the main panel
        mainButtonPanel.add(donorPanel);
        mainButtonPanel.add(recipientPanel);
        mainButtonPanel.add(stockPanel);

        // 7. Add the main panel to the frame
        add(mainButtonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Helper method to create a consistent, styled TitledBorder
     */
    private Border createStyledBorder(String title) {
        // Font for the title
        Font titleFont = new Font("SansSerif", Font.BOLD, 22);
        
        // Create a TitledBorder
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1), // MODIFIED: Use new border color
                title,
                TitledBorder.CENTER, // Title justification
                TitledBorder.TOP,    // Title position
                titleFont,           // Title font
                COLOR_FOREGROUND     // MODIFIED: Use new foreground color
        );
        
        // Add padding INSIDE the border
        Border padding = new EmptyBorder(20, 20, 20, 20);
        
        // Return a compound border (padding inside the title)
        return BorderFactory.createCompoundBorder(titledBorder, padding);
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
        viewRecipientsButton.addActionListener(listener);
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