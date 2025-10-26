package com.bloodBank.view; // Corrected package name

import com.bloodBank.model.Donor; // Must import the model
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener; // Only imports the listener
import java.util.ArrayList;

public class BloodBankView extends JFrame {

    // --- Components ---
    private JTextArea displayArea;
    private JButton addButton;

    // New form fields
    private JTextField firstNameField, lastNameField;
    private JTextField fatherNameField, motherNameField;
    private JTextField dobField; // Will take text "YYYY-MM-DD"
    private JTextField mobileNoField, emailField;
    private JComboBox<String> genderComboBox;
    private JTextField bloodGroupField;
    private JTextField cityField, fullAddressField;

    public BloodBankView() {
        // --- 1. Configure the Window ---
        setTitle("Blood Bank Management System (MVC)");
        // Increased window size for the large form
        setSize(800, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- 2. Input Panel (NORTH) ---
        // This panel will hold all the input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5); // Padding
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 0: First Name, Last Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name: *"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; // Field takes up space
        firstNameField = new JTextField(20);
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0; // Label takes no extra space
        inputPanel.add(new JLabel("Last Name: *"), gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        lastNameField = new JTextField(20);
        inputPanel.add(lastNameField, gbc);

        // --- Row 1: Father Name, Mother Name
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Father's Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        fatherNameField = new JTextField(20);
        inputPanel.add(fatherNameField, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        inputPanel.add(new JLabel("Mother's Name:"), gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        motherNameField = new JTextField(20);
        inputPanel.add(motherNameField, gbc);

        // --- Row 2: DOB, Gender
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("DOB (YYYY-MM-DD): *"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        dobField = new JTextField(20);
        inputPanel.add(dobField, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        inputPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 3; gbc.gridy = 2;
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        inputPanel.add(genderComboBox, gbc);

        // --- Row 3: Mobile No, Email
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Mobile No: *"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        mobileNoField = new JTextField(20);
        inputPanel.add(mobileNoField, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        inputPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 3; gbc.gridy = 3;
        emailField = new JTextField(20);
        inputPanel.add(emailField, gbc);

        // --- Row 4: Blood Group, City
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Blood Group: *"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        bloodGroupField = new JTextField(20);
        inputPanel.add(bloodGroupField, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        inputPanel.add(new JLabel("City:"), gbc);

        gbc.gridx = 3; gbc.gridy = 4;
        cityField = new JTextField(20);
        inputPanel.add(cityField, gbc);

        // --- Row 5: Full Address (Spans 3 columns)
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Full Address:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 3; // Make this field span 3 columns
        fullAddressField = new JTextField(20);
        inputPanel.add(fullAddressField, gbc);
        gbc.gridwidth = 1; // Reset to default

        // --- Row 6: Add Button (Aligned to the right)
        gbc.gridx = 3; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE; // Don't stretch button
        gbc.anchor = GridBagConstraints.EAST; // Align right
        addButton = new JButton("Add Donor");
        inputPanel.add(addButton, gbc);

        // --- Add the input panel (inside a scroll pane) to the window ---
        JScrollPane inputScrollPane = new JScrollPane(inputPanel);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("Add New Donor"));
        add(inputScrollPane, BorderLayout.NORTH);

        // --- 3. Display Area (CENTER) ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Donors"));
        add(scrollPane, BorderLayout.CENTER);

        // --- 4. Finalize ---
        setLocationRelativeTo(null); // Center the window
    }

    // --- Public methods for the Controller ---

    // --- GETTERS (one for each field) ---
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

    /**
     * Gets the Date of Birth as a String.
     * The controller is responsible for parsing it.
     */
    public String getDobString() {
        return dobField.getText().trim();
    }

    public String getMobileNo() {
        return mobileNoField.getText().trim();
    }

    /**
     * Gets the selected gender from the dropdown.
     */
    public String getGender() {
        return (String) genderComboBox.getSelectedItem();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getBloodGroup() {
        return bloodGroupField.getText().trim();
    }

    public String getCity() {
        return cityField.getText().trim();
    }

    public String getFullAddress() {
        return fullAddressField.getText().trim();
    }

    // --- UTILITY METHODS ---

    /**
     * Clears all input fields in the form.
     */
    public void clearInputFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");
        dobField.setText("");
        mobileNoField.setText("");
        emailField.setText("");
        bloodGroupField.setText("");
        cityField.setText("");
        fullAddressField.setText("");
        genderComboBox.setSelectedIndex(0); // Reset dropdown to "Male"
    }

    /**
     * The Controller will pass its event-handling logic to this method.
     */
    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    /**
     * The Controller calls this to update the text area with new data.
     * (This method works as-is because Donor.toString() was updated)
     */
    public void refreshDonorList(ArrayList<Donor> donors) {
        displayArea.setText(""); // Clear old text
        if (donors.isEmpty()) {
            displayArea.setText("No donors in the system yet.");
        } else {
            for (Donor d : donors) {
                displayArea.append(d.toString() + "\n");
            }
        }
        displayArea.setCaretPosition(0); // Scroll to top
    }

    public void showErrorMessage(String message) {
        // Using \n in JOptionPane creates line breaks
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}