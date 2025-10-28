// File: src/com/bloodBank/view/DonorInfoView.java
package com.bloodBank.view;

import com.bloodBank.model.Donor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A JDialog (pop-up window) to display a list of ALL donor information.
 */
public class DonorInfoView extends JDialog {

    // --- NEW: Pastel Color Palette for Aesthetics ---
    private static final Color PASTEL_BACKGROUND = new Color(245, 247, 249);
    private static final Color PASTEL_HEADER = new Color(110, 180, 210);
    private static final Color PASTEL_SELECTION = new Color(220, 235, 240);
    private static final Color PASTEL_GRID_COLOR = new Color(220, 220, 220);

    private JTable donorTable;
    private DefaultTableModel tableModel;

    /**
     * Constructor for the Donor Info pop-up.
     * @param owner The main window (JFrame) that this dialog belongs to.
     */
    public DonorInfoView(Frame owner) {
        // --- Dialog Setup ---
        super(owner, "All Donor Information", true);
        
        // --- Column list with all fields ---
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
        
        // Ensure horizontal scrolling and prevent columns from being squashed
        donorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // --- Modern Look & Feel ---
        
        // Table cell style
        donorTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        donorTable.setRowHeight(32);
        donorTable.setGridColor(PASTEL_GRID_COLOR);
        donorTable.setSelectionBackground(PASTEL_SELECTION);
        donorTable.setSelectionForeground(Color.BLACK);

        // Table Header Style
        JTableHeader header = donorTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 17));
        header.setBackground(PASTEL_HEADER);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // --- Apply custom column widths ---
        setColumnWidths(); 

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(donorTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PASTEL_BACKGROUND);
        
        // --- Add components to the dialog ---
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // --- Window Configuration ---
        setSize(1920, 1080);
        getContentPane().setBackground(PASTEL_BACKGROUND);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(owner); 
    }

    /**
     * NEW: Sets appropriate, fixed widths for all columns.
     */
    private void setColumnWidths() {
        // 0. ID
        // 1. Full Name
        // 2. Blood Group
        // 3. Mobile No.
        // 4. Email
        // 5. Gender
        // 6. Date of Birth
        // 7. City
        // 8. Full Address
        // 9. Father's Name
        // 10. Mother's Name
        
        int[] widths = {
            60,  // 0. ID (Small)
            200, // 1. Full Name (Medium)
            120, // 2. Blood Group (Small)
            140, // 3. Mobile No.
            250, // 4. Email (Wide)
            100, // 5. Gender
            140, // 6. Date of Birth
            120, // 7. City
            350, // 8. Full Address (Extra Wide)
            180, // 9. Father's Name
            180  // 10. Mother's Name
        };

        for (int i = 0; i < widths.length; i++) {
            TableColumn column = donorTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    /**
     * The controller calls this method to fill the table with data.
     * @param donorList A List of all Donor objects from the model.
     */
    public void displayDonors(List<Donor> donorList) {
        // Clear any old data
        tableModel.setRowCount(0);
        
        if (donorList == null) return;

        // --- Add data from the donor object ---
        for (Donor donor : donorList) {
            // Get all data points from the donor object
            int id = donor.getDonorId();
            String fullName = donor.getFirstName() + " " + donor.getLastName();
            String bloodGroup = donor.getBloodGroup();
            String mobileNo = donor.getMobileNo();
            String email = donor.getEmail();
            String gender = donor.getGender();
            LocalDate dob = donor.getDob();
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