// File: src/com/bloodBank/view/AddRecipientView.java
package com.bloodBank.view;

import com.bloodBank.view.components.ModernButton; // Import your custom button
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*; // Import entire AWT package
import java.awt.event.ActionListener;

public class AddRecipientView extends JDialog {

    // --- NEW: Consistent color palette and fonts ---
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 249);
    private static final Color COLOR_FOREGROUND = new Color(50, 50, 50);
    private static final Color COLOR_BORDER = new Color(200, 200, 200);

    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 16);

    // --- Components ---
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JFormattedTextField dobField;
    private JComboBox<String> genderComboBox;
    private JTextField mobileNoField;
    private JTextField emailField;
    private JComboBox<String> bloodGroupComboBox;
    private JTextField cityField;
    private JTextArea fullAddressArea;
    private JButton addButton;
    private JButton cancelButton; // NEW

    // --- NEW FIELD for Recipient ---
    private JTextField reasonField;

    public AddRecipientView(Frame owner) {
        // --- Call JDialog's constructor ---
        super(owner, "Add New Recipient", true); // 'true' makes it modal

        // --- MODIFIED: Configure the Dialog Window to 80% of screen size ---
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.width * 0.8);
        int windowHeight = (int) (screenSize.height * 0.8);
        setSize(windowWidth, windowHeight);
        
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BACKGROUND); // Set background color

        // Add padding to the dialog's main content pane
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Create and add the main input panel ---
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.CENTER);

        // --- Finalize ---
        setLocationRelativeTo(owner); // Center relative to the dashboard
    }

    /**
     * NEW: Helper method to create a consistent, modern border for text fields
     */
    private Border createModernFieldBorder() {
        Border line = BorderFactory.createLineBorder(COLOR_BORDER, 1);
        Border padding = new EmptyBorder(5, 8, 5, 8); // Padding inside the field
        return BorderFactory.createCompoundBorder(line, padding);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(COLOR_BACKGROUND);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Increased insets
        gbc.anchor = GridBagConstraints.WEST;

        // Helper to create a styled label
        JLabel label;

        // --- Row 0: First Name, Last Name
        label = new JLabel("First Name: *");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 0, 1, 0.0, GridBagConstraints.NONE);
        
        firstNameField = new JTextField(15);
        firstNameField.setFont(FONT_FIELD);
        firstNameField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, firstNameField, gbc, 1, 0, 1, 1.0, GridBagConstraints.HORIZONTAL);

        label = new JLabel("Last Name: *");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 2, 0, 1, 0.0, GridBagConstraints.NONE);
        
        lastNameField = new JTextField(15);
        lastNameField.setFont(FONT_FIELD);
        lastNameField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, lastNameField, gbc, 3, 0, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 1: Father Name, Mother Name
        label = new JLabel("Father's Name:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 1, 1, 0.0, GridBagConstraints.NONE);

        fatherNameField = new JTextField(15);
        fatherNameField.setFont(FONT_FIELD);
        fatherNameField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, fatherNameField, gbc, 1, 1, 1, 1.0, GridBagConstraints.HORIZONTAL);

        label = new JLabel("Mother's Name:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 2, 1, 1, 0.0, GridBagConstraints.NONE);
        
        motherNameField = new JTextField(15);
        motherNameField.setFont(FONT_FIELD);
        motherNameField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, motherNameField, gbc, 3, 1, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 2: DOB, Gender
        label = new JLabel("DOB (YYYY-MM-DD): *");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 2, 1, 0.0, GridBagConstraints.NONE);

        try {
            javax.swing.text.MaskFormatter dobMask = new javax.swing.text.MaskFormatter("####-##-##");
            dobField = new JFormattedTextField(dobMask);
            dobField.setToolTipText("YYYY-MM-DD");
        } catch (java.text.ParseException e) {
            dobField = new JFormattedTextField("YYYY-MM-DD");
        }
        dobField.setFont(FONT_FIELD);
        dobField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, dobField, gbc, 1, 2, 1, 1.0, GridBagConstraints.HORIZONTAL);

        label = new JLabel("Gender:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 2, 2, 1, 0.0, GridBagConstraints.NONE);

        String[] genders = { "Male", "Female", "Other", "Prefer not to say" };
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(FONT_FIELD);
        genderComboBox.setBackground(Color.WHITE);
        addComponent(inputPanel, genderComboBox, gbc, 3, 2, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 3: Mobile No, Email
        label = new JLabel("Mobile No: *");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 3, 1, 0.0, GridBagConstraints.NONE);
        
        mobileNoField = new JTextField(15);
        mobileNoField.setFont(FONT_FIELD);
        mobileNoField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, mobileNoField, gbc, 1, 3, 1, 1.0, GridBagConstraints.HORIZONTAL);

        label = new JLabel("Email:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 2, 3, 1, 0.0, GridBagConstraints.NONE);
        
        emailField = new JTextField(15);
        emailField.setFont(FONT_FIELD);
        emailField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, emailField, gbc, 3, 3, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 4: Blood Group, City
        label = new JLabel("Blood Group: *");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 4, 1, 0.0, GridBagConstraints.NONE);

        String[] bloodGroups = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        bloodGroupComboBox = new JComboBox<>(bloodGroups);
        bloodGroupComboBox.setFont(FONT_FIELD);
        bloodGroupComboBox.setBackground(Color.WHITE);
        addComponent(inputPanel, bloodGroupComboBox, gbc, 1, 4, 1, 1.0, GridBagConstraints.HORIZONTAL);

        label = new JLabel("City:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 2, 4, 1, 0.0, GridBagConstraints.NONE);
        
        cityField = new JTextField(15);
        cityField.setFont(FONT_FIELD);
        cityField.setBorder(createModernFieldBorder());
        addComponent(inputPanel, cityField, gbc, 3, 4, 1, 1.0, GridBagConstraints.HORIZONTAL);

        // --- Row 5: REASON FOR TRANSFUSION (NEW) ---
        label = new JLabel("Reason for Blood:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 5, 1, 0.0, GridBagConstraints.NONE);
        
        reasonField = new JTextField(15);
        reasonField.setFont(FONT_FIELD);
        reasonField.setBorder(createModernFieldBorder());
        // Make this field span the remaining 3 columns
        addComponent(inputPanel, reasonField, gbc, 1, 5, 3, 1.0, GridBagConstraints.HORIZONTAL);


        // --- Row 6: Full Address (Spans 3 columns) ---
        label = new JLabel("Full Address:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        addComponent(inputPanel, label, gbc, 0, 6, 1, 0.0, GridBagConstraints.NORTHWEST);
        
        fullAddressArea = new JTextArea(3, 20);
        fullAddressArea.setFont(FONT_FIELD);
        fullAddressArea.setLineWrap(true);
        fullAddressArea.setWrapStyleWord(true);
        fullAddressArea.setBorder(new EmptyBorder(5, 8, 5, 8)); // Just inner padding

        JScrollPane addressScrollPane = new JScrollPane(fullAddressArea);
        addressScrollPane.setBorder(createModernFieldBorder()); // Apply modern border

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(addressScrollPane, gbc);

        // --- Row 7: Add Button (in its own panel) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        addButton = new ModernButton("Add Recipient", new Color(125, 194, 93)); // Green
        cancelButton = new ModernButton("Cancel", new Color(184, 77, 75)); // Red
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4; // Span all 4 columns
        inputPanel.add(buttonPanel, gbc);

        return inputPanel;
    }

    // This helper method is identical
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

    // --- NEW GETTER ---
    public String getReasonForTransfusion() {
        return reasonField.getText().trim();
    }

    public void clearInputFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");
        dobField.setValue(null); // MODIFIED: Correct way to clear masked field
        mobileNoField.setText("");
        emailField.setText("");
        bloodGroupComboBox.setSelectedIndex(0);
        cityField.setText("");
        fullAddressArea.setText("");
        genderComboBox.setSelectedIndex(0);
        reasonField.setText(""); // <-- Added
    }

    // This method name is kept the same for controller consistency
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