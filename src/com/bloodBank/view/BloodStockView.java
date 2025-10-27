// File: src/com/bloodBank/view/BloodStockView.java
package com.bloodBank.view;

import com.bloodBank.model.BloodUnit; // Import the BloodUnit model
import com.bloodBank.model.BloodStatus; // Import the BloodStatus enum

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList; // Switched from Map to ArrayList
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // For formatting dates

public class BloodStockView extends JDialog {

    private JTable stockTable;
    private DefaultTableModel tableModel;
    // Formatter for displaying dates in a friendly way
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public BloodStockView(Frame owner) {
        super(owner, "Detailed Blood Inventory", true); // Title changed
        setSize(650, 500); // Made window wider for new columns
        setLayout(new BorderLayout());

        // --- Setup the Table Model ---
        String[] columnNames = {
            "Unit ID", "Blood Group", "Donor ID",
            "Donation Date", "Expiry Date", "Status"
        };

        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };

        // --- Setup the JTable ---
        stockTable = new JTable(tableModel);

        // --- Modern Look & Feel ---
        stockTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockTable.setRowHeight(28);
        stockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        stockTable.getTableHeader().setReorderingAllowed(false);
        stockTable.setFillsViewportHeight(true); // Fills the scroll pane

        // --- Add Custom Renderer for Status Colors ---
        stockTable.setDefaultRenderer(Object.class, new StatusCellRenderer());

        // --- Setup Column Widths ---
        stockTable.getColumnModel().getColumn(0).setPreferredWidth(60); // Unit ID
        stockTable.getColumnModel().getColumn(1).setPreferredWidth(90); // Blood Group
        stockTable.getColumnModel().getColumn(2).setPreferredWidth(70); // Donor ID
        stockTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Donation Date
        stockTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Expiry Date
        stockTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE); // Set background of scroll area

        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(owner);
    }

    /**
     * The controller will call this to populate the table.
     * Note: It now accepts an ArrayList of BloodUnit objects.
     */
    public void displayStock(ArrayList<BloodUnit> units) {
        // Clear previous data
        tableModel.setRowCount(0);

        // Loop through the list of BloodUnit objects
        for (BloodUnit unit : units) {
            // Create a row of data
            Object[] rowData = {
                unit.getUnitId(),
                unit.getBloodGroup(),
                unit.getDonorId(),
                unit.getDonationDate().format(DATE_FORMATTER), // Format date
                unit.getExpiryDate().format(DATE_FORMATTER), // Format date
                unit.getStatus() // Get the enum status
            };
            // Add the row to the table model
            tableModel.addRow(rowData);
        }
    }

    /**
     * Inner class to render cells with colors based on blood unit status.
     * This adds the "traffic light" coloring for inventory management.
     */
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        
        // Define our status colors
        private final Color COLOR_EXPIRED = new Color(255, 192, 203); // Light Pink/Red
        private final Color COLOR_EXPIRING_SOON = new Color(255, 253, 208); // Light Yellow
        private final Color COLOR_USED = new Color(220, 220, 220); // Light Gray
        private final Color COLOR_IN_STOCK = Color.WHITE;
        private final Color COLOR_IN_STOCK_SELECTED = new Color(57, 105, 138); // Dark blue for selection
        // private final Color COLOR_OTHER_SELECTED = new Color(57, 105, 138);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
            
            // Get the component from the parent class (handles text, alignment, etc.)
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the status from the table model for the current row
            // Note: This assumes the "Status" column is always at index 5
            BloodStatus status = (BloodStatus) table.getModel().getValueAt(row, 5);
            
            // Get the text from the Expiry Date column (index 4) to check if it's expiring soon
            String expiryDateStr = (String) table.getModel().getValueAt(row, 4);
            LocalDate expiryDate = LocalDate.parse(expiryDateStr, DATE_FORMATTER);
            boolean isExpiringSoon = expiryDate.isBefore(LocalDate.now().plusDays(7)) && 
                                     status == BloodStatus.IN_STOCK;

            // Set background color based on status
            if (isSelected) {
                // Use a single, clear selection color
                c.setBackground(COLOR_IN_STOCK_SELECTED);
                c.setForeground(Color.WHITE);
            } else {
                // Set row color based on status
                c.setForeground(Color.BLACK); // Default text color
                switch (status) {
                    case EXPIRED:
                        c.setBackground(COLOR_EXPIRED);
                        break;
                    case ISSUED:
                        c.setBackground(COLOR_USED);
                        c.setForeground(Color.GRAY); // Dim the text for used units
                        break;
                    case IN_STOCK:
                        if (isExpiringSoon) {
                            c.setBackground(COLOR_EXPIRING_SOON);
                        } else {
                            c.setBackground(COLOR_IN_STOCK);
                        }
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
}