package com.bloodBank.view;

import com.bloodBank.model.Donor; // Import your Donor class

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate; // Import for LocalDate
import java.util.List; // Import for the List of donors

/**
 * A JDialog (pop-up window) to display a list of ALL donor information.
 */
public class DonorInfoView extends JDialog {

    private JTable donorTable;
    private DefaultTableModel tableModel;

    /**
     * Constructor for the Donor Info pop-up.
     * @param owner The main window (JFrame) that this dialog belongs to.
     */
    public DonorInfoView(Frame owner) {
        // --- Dialog Setup ---
        super(owner, "All Donor Information", true);
        
        // --- MODIFICATION: New column list with all fields ---
        String[] columnNames = {
            "ID", "Full Name", "Blood Group", "Mobile No.", "Email",
            "Gender", "Date of Birth", "City", "Full Address",
            "Father's Name", "Mother's Name"
        };
        
        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells non-editable
            }
        };

        // --- Setup the JTable ---
        donorTable = new JTable(tableModel);
        
        // --- MODIFICATION: Add this line for horizontal scrolling ---
        // This is essential for tables with many columns.
        // It will create a horizontal scrollbar instead of shrinking columns.
        donorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // --- Modern Look & Feel ---
        donorTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        donorTable.setRowHeight(28);
        donorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        donorTable.getTableHeader().setReorderingAllowed(false);

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(donorTable);
        // The scroll pane will NOW show both vertical and horizontal scrollbars
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Add components to the dialog ---
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // --- MODIFICATION: Make the dialog wider to show more data ---
        setSize(1000, 600); // Increased width
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(owner); 
    }

    /**
     * The controller calls this method to fill the table with data.
     * @param donorList A List of all Donor objects from the model.
     */
    public void displayDonors(List<Donor> donorList) {
        // Clear any old data
        tableModel.setRowCount(0);
        
        if (donorList == null) return; // Safety check

        // --- MODIFICATION: Add all fields from the donor object ---
        for (Donor donor : donorList) {
            // Get all data points from the donor object
            int id = donor.getDonorId();
            String fullName = donor.getFirstName() + " " + donor.getLastName();
            String bloodGroup = donor.getBloodGroup();
            String mobileNo = donor.getMobileNo();
            String email = donor.getEmail();
            String gender = donor.getGender();
            LocalDate dob = donor.getDob(); // Get the date object
            String city = donor.getCity();
            String address = donor.getFullAddress();
            String fatherName = donor.getFatherName();
            String motherName = donor.getMotherName();
            
            // Create an array for the row data
            Object[] rowData = {
                id,
                fullName,
                bloodGroup,
                mobileNo,
                email,
                gender,
                (dob != null ? dob.toString() : "N/A"), // Convert date to string
                city,
                address,
                fatherName,
                motherName
            };
            
            // Add the row to the table model
            tableModel.addRow(rowData);
        }
    }
}