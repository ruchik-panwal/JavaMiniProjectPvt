// File: src/com/bloodBank/view/RemoveByIdView.java
package com.bloodBank.view;

import com.bloodBank.view.components.ModernButton;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RemoveByIdView extends JDialog {

    // --- Consistent color palette and fonts (MODIFIED) ---
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 249);
    private static final Color COLOR_FOREGROUND = new Color(50, 50, 50);
    private static final Color COLOR_BORDER = new Color(200, 200, 200);

    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 22); // Larger size
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 22); // Larger size

    // --- Components ---
    private JTextField idField;
    private JButton removeButton;
    private JButton cancelButton;

    // --- State for the controller ---
    private String id;
    private boolean removeClicked = false;

    public RemoveByIdView(Frame owner, String title) {
        super(owner, title, true); // true = modal

        // --- Configure the Dialog Window (MAXIMUM SIZE) ---
        setSize(1000, 500); // Significantly larger and wider
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BACKGROUND);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(40, 40, 40, 40)); // Increased padding

        // --- Create Panel ---
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // More spacing
        gbc.anchor = GridBagConstraints.WEST;

        // --- Label ---
        JLabel label = new JLabel("Enter ID to Remove:");
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_FOREGROUND);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        // --- Text Field ---
        idField = new JTextField(20);
        idField.setFont(FONT_FIELD);
        idField.setBorder(createModernFieldBorder());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(idField, gbc);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        removeButton = new ModernButton("Remove", new Color(184, 77, 75)); // Red
        cancelButton = new ModernButton("Cancel", new Color(100, 100, 100)); // Gray

        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        panel.add(buttonPanel, gbc);

        // --- Add main panel to dialog ---
        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(owner);

        // --- Add Listeners ---
        removeButton.addActionListener(e -> onRemove());
        cancelButton.addActionListener(e -> onCancel());
    }

    private void onRemove() {
        String inputId = idField.getText().trim();
        if (inputId.isEmpty()) {
            showErrorMessage("ID cannot be empty.");
            return;
        }
        
        this.id = inputId;
        this.removeClicked = true;
        dispose(); // Close the dialog
    }

    private void onCancel() {
        this.removeClicked = false;
        dispose(); // Close the dialog
    }

    // --- Public methods for the Controller ---
    public String getId() {
        return id;
    }

    public boolean isRemoveClicked() {
        return removeClicked;
    }

    // --- Helper Methods ---
    private Border createModernFieldBorder() {
        Border line = BorderFactory.createLineBorder(COLOR_BORDER, 1);
        // Padding inside the field increased
        Border padding = new EmptyBorder(8, 12, 8, 12); 
        return BorderFactory.createCompoundBorder(line, padding);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}