// File: src/com/bloodBank/view/RecipientInfoView.java
package com.bloodBank.view;

import com.bloodBank.model.Recipient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A JDialog (pop-up window) to display a list of ALL recipient information.
 */
public class RecipientInfoView extends JDialog {

    // --- NEW: Pastel Green Color Palette for Aesthetics ---
    private static final Color PASTEL_BACKGROUND = new Color(245, 247, 249); // Very Light Gray-Green
    private static final Color PASTEL_HEADER = new Color(125, 194, 93);      // Soft Green
    private static final Color PASTEL_SELECTION = new Color(229, 240, 225);   // Very Light Green
    private static final Color PASTEL_GRID_COLOR = new Color(220, 220, 220);  // Light Gray Grid

    private JTable recipientTable;
    private DefaultTableModel tableModel;

    /**
     * Constructor for the Recipient Info pop-up.
     * @param owner The main window (JFrame) that this dialog belongs to.
     */
    public RecipientInfoView(Frame owner) {
        // --- Dialog Setup ---
        super(owner, "All Recipient Information", true); 
        
        // --- Column list with all fields ---
        String[] columnNames = {
            "ID", "Full Name", "Blood Group", 
            "Status", "Date Received",
            "Mobile No.", "Email",
            "Gender", "Date of Birth", "City", "Full Address",
            "Father's Name", "Mother's Name", "Reason for Blood" // <-- NEW Column
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
        
        // Ensure horizontal scrolling and prevent columns from being squashed
        recipientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // --- Modern Look & Feel (MODIFIED) ---
        
        // Table cell style
        recipientTable.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Larger font
        recipientTable.setRowHeight(32); // Taller rows for readability
        recipientTable.setGridColor(PASTEL_GRID_COLOR); // Light grid lines
        recipientTable.setSelectionBackground(PASTEL_SELECTION);
        recipientTable.setSelectionForeground(Color.BLACK);

        // Table Header Style
        JTableHeader header = recipientTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 17)); // Bigger, bolder font
        header.setBackground(PASTEL_HEADER); // Pastel Green header
        header.setForeground(Color.WHITE); // White text on header
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Taller header

        // --- Apply custom column widths ---
        setColumnWidths(); 

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(recipientTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        scrollPane.getViewport().setBackground(PASTEL_BACKGROUND); // Set viewport color

        // --- Add components to the dialog ---
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // --- Window Configuration (MODIFIED) ---
        setSize(1920, 1080); // Set to full size
        getContentPane().setBackground(PASTEL_BACKGROUND);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(owner); 
    }

    /**
     * Sets appropriate, fixed widths for all columns.
     */
    private void setColumnWidths() {
        // 0. ID
        // 1. Full Name
        // 2. Blood Group
        // 3. Status
        // 4. Date Received
        // 5. Mobile No.
        // 6. Email
        // 7. Gender
        // 8. Date of Birth
        // 9. City
        // 10. Full Address
        // 11. Father's Name
        // 12. Mother's Name
        // 13. Reason for Blood
        
        int[] widths = {
            60,  // 0. ID
            200, // 1. Full Name
            120, // 2. Blood Group
            100, // 3. Status (NEW)
            140, // 4. Date Received (NEW)
            140, // 5. Mobile No.
            250, // 6. Email (Wide)
            100, // 7. Gender
            140, // 8. Date of Birth
            120, // 9. City
            300, // 10. Full Address (Wide)
            180, // 11. Father's Name
            180, // 12. Mother's Name
            350  // 13. Reason for Blood (Extra Wide)
        };

        for (int i = 0; i < widths.length; i++) {
            TableColumn column = recipientTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    /**
     * The controller calls this method to fill the table with data.
     * @param recipientList A List of all Recipient objects from the model.
     */
    public void displayRecipients(List<Recipient> recipientList) {
        // Clear any old data
        tableModel.setRowCount(0);
        
        if (recipientList == null) return; // Safety check

        // --- Add data from the recipient object ---
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
            
            // --- Get status data ---
            String status = recipient.didReceiveUnit() ? "Received" : "Pending";
            LocalDate date = recipient.getDateReceived();
            String dateReceivedStr = (date != null) ? date.toString() : "---";
            
            // Create an array for the row data
            Object[] rowData = {
                id,
                fullName,
                bloodGroup,
                status, 
                dateReceivedStr, 
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