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
        setSize(1900, 1000);
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
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 row, 3 cols
        buttonPanel.setBorder(new EmptyBorder(20, 50, 50, 50)); // Add padding

        // Uses a program package for style
        addDonorButton = new ModernButton("Add New Donor", new Color(125, 194, 93)); // A green color
        removeDonorButton = new ModernButton("Remove Donor by ID", new Color(184, 77, 75)); // A red color
        viewBloodStockButton = new ModernButton("View Blood Stock", new Color(65, 141, 176)); // A blue color

        // adding to the panel
        buttonPanel.add(addDonorButton);
        buttonPanel.add(removeDonorButton);
        buttonPanel.add(viewBloodStockButton);

        // Took a wrapper for sizing change
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.add(buttonPanel);
        add(wrapperPanel, BorderLayout.CENTER);
    }


    public void addAddDonorListener(ActionListener listener) {
        addDonorButton.addActionListener(listener);
    }

    public void addRemoveDonorListener(ActionListener listener) {
        removeDonorButton.addActionListener(listener);
    }

    public void addViewBloodStockListener(ActionListener listener) {
        viewBloodStockButton.addActionListener(listener);
    }
    
    public String showIDInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message, "Remove Donor", JOptionPane.QUESTION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}