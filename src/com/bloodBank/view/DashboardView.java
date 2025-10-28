// File: src/com/bloodBank/view/DashboardView.java
package com.bloodBank.view;

import com.bloodBank.view.components.ModernButton;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {

    // --- Consistent color palette ---
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 249);
    private static final Color COLOR_FOREGROUND = new Color(50, 50, 50);
    private static final Color COLOR_BORDER = new Color(200, 200, 200);

    // --- Components ---
    private JButton addDonorButton;
    private JButton removeDonorButton;
    private JButton viewBloodStockButton;
    private JButton viewDonorsButton;
    private JButton addRecipientButton;
    private JButton removeRecipientButton;
    private JButton viewRecipientsButton;

    // Constructor
    public DashboardView() {
        initFrame();
        initTitlePanel();
        initButtonPanel();
        setLocationRelativeTo(null);
    }

    // Frame Setup
    private void initFrame() {
        setTitle("Blood Bank Dashboard");
        setSize(1960, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BACKGROUND);
    }

    // Title Creation
    private void initTitlePanel() {
        JLabel titleLabel = new JLabel("Blood Bank Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 54));
        titleLabel.setBorder(new EmptyBorder(40, 20, 40, 20));
        titleLabel.setForeground(COLOR_FOREGROUND);
        add(titleLabel, BorderLayout.NORTH);
    }

    // Button Panel
    private void initButtonPanel() {
        
        JPanel mainButtonPanel = new JPanel(new GridLayout(1, 3, 40, 40));
        mainButtonPanel.setBorder(new EmptyBorder(20, 40, 40, 40));
        mainButtonPanel.setBackground(COLOR_BACKGROUND);

        // --- Buttons ---
        addDonorButton = new ModernButton("Add New Donor", new Color(125, 194, 93));
        removeDonorButton = new ModernButton("Remove Donor by ID", new Color(184, 77, 75));
        viewDonorsButton = new ModernButton("View All Donors", new Color(65, 141, 176));
        addRecipientButton = new ModernButton("Add New Recipient", new Color(125, 194, 93));
        removeRecipientButton = new ModernButton("Remove Recipient by ID", new Color(184, 77, 75));
        viewRecipientsButton = new ModernButton("View All Recipients", new Color(65, 141, 176));
        viewBloodStockButton = new ModernButton("View Blood Stock", new Color(20, 100, 140));

        // --- Group 1: Donor Panel ---
        JPanel donorPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        donorPanel.setBorder(createStyledBorder("Donor Management"));
        donorPanel.setBackground(COLOR_BACKGROUND);
        donorPanel.add(addDonorButton);
        donorPanel.add(removeDonorButton);
        donorPanel.add(viewDonorsButton);

        // --- Group 2: Recipient Panel ---
        JPanel recipientPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        recipientPanel.setBorder(createStyledBorder("Recipient Management"));
        recipientPanel.setBackground(COLOR_BACKGROUND);
        recipientPanel.add(addRecipientButton);
        recipientPanel.add(removeRecipientButton);
        recipientPanel.add(viewRecipientsButton);

        // --- Group 3: Stock Panel ---
        JPanel stockPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        stockPanel.setBorder(createStyledBorder("Blood Stock"));
        stockPanel.setBackground(COLOR_BACKGROUND);
        stockPanel.add(viewBloodStockButton);

        // --- Add groups to main panel ---
        mainButtonPanel.add(donorPanel);
        mainButtonPanel.add(recipientPanel);
        mainButtonPanel.add(stockPanel);

        add(mainButtonPanel, BorderLayout.CENTER);
    }
    
    private Border createStyledBorder(String title) {
        Font titleFont = new Font("SansSerif", Font.BOLD, 22);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                titleFont,
                COLOR_FOREGROUND
        );
        Border padding = new EmptyBorder(20, 20, 20, 20);
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

    // --- Recipient Listeners ---
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
    
    // REMOVED showDonorIDInputDialog()
    
    // REMOVED showRecipientIDInputDialog()

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}