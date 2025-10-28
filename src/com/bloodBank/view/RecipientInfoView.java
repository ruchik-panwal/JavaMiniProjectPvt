// File: src/com/bloodBank/view/RecipientInfoView.java
package com.bloodBank.view;

import com.bloodBank.model.Recipient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A JDialog (pop-up window) to display a list of ALL recipient information.
 */
public class RecipientInfoView extends JDialog {

    private JTable recipientTable;
    private DefaultTableModel tableModel;

    /**
     * Constructor for the Recipient Info pop-up.
     * @param owner The main window (JFrame) that this dialog belongs to.
     */
    public RecipientInfoView(Frame owner) {
        // --- Dialog Setup ---
        super(owner, "All Recipient Information", true); 
        
        // --- MODIFICATION: Added "Status" and "Date Received" ---
        String[] columnNames = {
            "ID", "Full Name", "Blood Group", 
            "Status", "Date Received", // <-- NEW COLUMNS
            "Mobile No.", "Email",
            "Gender", "Date of Birth", "City", "Full Address",
            "Father's Name", "Mother's Name", "Reason for Blood"
        };
        
        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells non-editable
            }
        };

        // --- Setup the JTable ---
        recipientTable = new JTable(tableModel);
        
        // Add this line for horizontal scrolling
        recipientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // --- Modern Look & Feel ---
        recipientTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recipientTable.setRowHeight(28);
        recipientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        recipientTable.getTableHeader().setReorderingAllowed(false);

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(recipientTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Add components to the dialog ---
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // --- MODIFICATION: Make the dialog wider to show more data ---
        setSize(1200, 600); // Increased width for new columns
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(owner); 
    }

    /**
     * The controller calls this method to fill the table with data.
     * @param recipientList A List of all Recipient objects from the model.
     */
    public void displayRecipients(List<Recipient> recipientList) {
        // Clear any old data
        tableModel.setRowCount(0);
        
        if (recipientList == null) return; // Safety check

        // --- MODIFICATION: Add all fields from the recipient object ---
        for (Recipient recipient : recipientList) {
            // Get all data points from the recipient object
            int id = recipient.getRecipientId();
            String fullName = recipient.getFirstName() + " " + recipient.getLastName();
            String bloodGroup = recipient.getBloodGroup();
            String mobileNo = recipient.getMobileNo();
            String email = recipient.getEmail();
            String gender = recipient.getGender();
            LocalDate dob = recipient.getDob();
            String city = recipient.getCity();
            String address = recipient.getFullAddress();
            String fatherName = recipient.getFatherName();
            String motherName = recipient.getMotherName();
            String reason = recipient.getReasonForTransfusion();
            
            // --- NEW: Get status data ---
            String status = recipient.didReceiveUnit() ? "Received" : "Pending";
            LocalDate date = recipient.getDateReceived();
            String dateReceivedStr = (date != null) ? date.toString() : "---";
            
            // Create an array for the row data
            Object[] rowData = {
                id,
                fullName,
                bloodGroup,
                status,          // <-- NEW
                dateReceivedStr, // <-- NEW
                mobileNo,
                email,
                gender,
                (dob != null ? dob.toString() : "N/A"),
                city,
                address,
                fatherName,
                motherName,
                reason
            };
            
            // Add the row to the table model
            tableModel.addRow(rowData);
        }
    }
}