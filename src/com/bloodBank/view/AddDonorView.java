// File: src/com/bloodBank/view/AddDonorView.java
package com.bloodBank.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Import Frame for the "owner"
import java.awt.*;
import java.awt.event.ActionListener;

public class AddDonorView extends JDialog {

    // ... inside your class definition (e.g., public class DonorForm extends
    // JFrame)
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JFormattedTextField dobField; // <-- IMPROVED
    private JComboBox<String> genderComboBox;
    private JTextField mobileNoField;
    private JTextField emailField;
    private JComboBox<String> bloodGroupComboBox; // <-- IMPROVED
    private JTextField cityField;
    private JTextArea fullAddressArea; // <-- IMPROVED
    private JButton addButton;

    public AddDonorView(Frame owner) {
        // --- Call JDialog's constructor ---
        super(owner, "Add New Donor", true); // 'true' makes it modal

        // --- Configure the Dialog Window ---
        setSize(750, 450);
        setLayout(new BorderLayout(10, 10));
        // Add padding to the dialog's main content pane
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Create and add the main input panel ---
        // Delegate the complex work to a helper method
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.CENTER);

        // --- Finalize ---
        setLocationRelativeTo(owner); // Center relative to the dashboard
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        // IMPROVEMENT: Add a border for overall padding around the whole panel
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        // IMPROVEMENT: Increased insets for more breathing room between components
        gbc.insets = new Insets(5, 8, 5, 8); // (top, left, bottom, right)
        gbc.anchor = GridBagConstraints.WEST;

        // --- Row 0: First Name, Last Name
        addComponent(inputPanel, new JLabel("First Name: *"), gbc, 0, 0, 1, 0.0, GridBagConstraints.NONE);
        firstNameField = new JTextField(15);
        addComponent(inputPanel, firstNameField, gbc, 1, 0, 1, 1.0, GridBagConstraints.HORIZONTAL);

        addComponent(inputPanel, new JLabel("Last Name: *"), gbc, 2, 0, 1, 0.0, GridBagConstraints.NONE);
        lastNameField = new JTextField(15);
        addComponent(inputPanel, lastNameField, gbc, 3, 0, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 1: Father Name, Mother Name
        addComponent(inputPanel, new JLabel("Father's Name:"), gbc, 0, 1, 1, 0.0, GridBagConstraints.NONE);
        fatherNameField = new JTextField(15);
        addComponent(inputPanel, fatherNameField, gbc, 1, 1, 1, 1.0, GridBagConstraints.HORIZONTAL);

        addComponent(inputPanel, new JLabel("Mother's Name:"), gbc, 2, 1, 1, 0.0, GridBagConstraints.NONE);
        motherNameField = new JTextField(15);
        addComponent(inputPanel, motherNameField, gbc, 3, 1, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 2: DOB, Gender
        addComponent(inputPanel, new JLabel("DOB (YYYY-MM-DD): *"), gbc, 0, 2, 1, 0.0, GridBagConstraints.NONE);

        // IMPROVEMENT: Use JFormattedTextField to force YYYY-MM-DD format
        try {
            javax.swing.text.MaskFormatter dobMask = new javax.swing.text.MaskFormatter("####-##-##");
            dobField = new JFormattedTextField(dobMask);
            dobField.setToolTipText("YYYY-MM-DD");
        } catch (java.text.ParseException e) {
            // Fallback in case of error
            dobField = new JFormattedTextField("YYYY-MM-DD");
        }
        addComponent(inputPanel, dobField, gbc, 1, 2, 1, 1.0, GridBagConstraints.HORIZONTAL);

        addComponent(inputPanel, new JLabel("Gender:"), gbc, 2, 2, 1, 0.0, GridBagConstraints.NONE);
        String[] genders = { "Male", "Female", "Other", "Prefer not to say" };
        genderComboBox = new JComboBox<>(genders);
        addComponent(inputPanel, genderComboBox, gbc, 3, 2, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 3: Mobile No, Email
        addComponent(inputPanel, new JLabel("Mobile No: *"), gbc, 0, 3, 1, 0.0, GridBagConstraints.NONE);
        mobileNoField = new JTextField(15);
        addComponent(inputPanel, mobileNoField, gbc, 1, 3, 1, 1.0, GridBagConstraints.HORIZONTAL);

        addComponent(inputPanel, new JLabel("Email:"), gbc, 2, 3, 1, 0.0, GridBagConstraints.NONE);
        emailField = new JTextField(15);
        addComponent(inputPanel, emailField, gbc, 3, 3, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 4: Blood Group, City
        addComponent(inputPanel, new JLabel("Blood Group: *"), gbc, 0, 4, 1, 0.0, GridBagConstraints.NONE);

        // IMPROVEMENT: Use JComboBox for Blood Group to ensure valid data
        String[] bloodGroups = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        bloodGroupComboBox = new JComboBox<>(bloodGroups);
        addComponent(inputPanel, bloodGroupComboBox, gbc, 1, 4, 1, 1.0, GridBagConstraints.HORIZONTAL);

        addComponent(inputPanel, new JLabel("City:"), gbc, 2, 4, 1, 0.0, GridBagConstraints.NONE);
        cityField = new JTextField(15);
        addComponent(inputPanel, cityField, gbc, 3, 4, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 5: Full Address (Spans 3 columns)
        // Anchor label to the top-left
        addComponent(inputPanel, new JLabel("Full Address:"), gbc, 0, 5, 1, 0.0, GridBagConstraints.NORTHWEST);

        // IMPROVEMENT: Use JTextArea in a JScrollPane for a multi-line address
        fullAddressArea = new JTextArea(3, 20); // 3 rows preferred size
        fullAddressArea.setLineWrap(true);
        fullAddressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(fullAddressArea);

        // Make the address area grow horizontally AND vertically
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // <-- This makes the row stretch vertically
        gbc.fill = GridBagConstraints.BOTH; // <-- Fill both ways
        inputPanel.add(addressScrollPane, gbc);

        // --- Row 6: Add Button (in its own panel for better alignment)

        // IMPROVEMENT: Use a separate panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligns buttons to the right
        addButton = new JButton("Add Donor");
        buttonPanel.add(addButton);
        // You could add more buttons here, e.g.: buttonPanel.add(new JButton("Clear"));

        // Reset weighty and set constraints for the button panel
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4; // Span all 4 columns
        inputPanel.add(buttonPanel, gbc);

        return inputPanel;
    }

    private void addComponent(Container container, Component component, GridBagConstraints gbc,
            int x, int y, int gridwidth, double weightx, int fill) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gridwidth;
        gbc.weightx = weightx;
        gbc.fill = fill;
        container.add(component, gbc);
    }

    // --- Public methods for the Controller ---
    public String getFirstName() {
        return firstNameField.getText().trim();
    }

    public String getLastName() {
        return lastNameField.getText().trim();
    }

    public String getFatherName() {
        return fatherNameField.getText().trim();
    }

    public String getMotherName() {
        return motherNameField.getText().trim();
    }

    public String getDobString() {
        return dobField.getText().trim();
    }

    public String getMobileNo() {
        return mobileNoField.getText().trim();
    }

    public String getGender() {
        return (String) genderComboBox.getSelectedItem();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getBloodGroup() {
        return (String) bloodGroupComboBox.getSelectedItem();
    }

    public String getCity() {
        return cityField.getText().trim();
    }

    public String getFullAddress() {
        return fullAddressArea.getText().trim();
    }

    public void clearInputFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");
        dobField.setText("");
        mobileNoField.setText("");
        emailField.setText("");
        bloodGroupComboBox.setSelectedIndex(0);
        cityField.setText("");
        fullAddressArea.setText("");
        genderComboBox.setSelectedIndex(0);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}