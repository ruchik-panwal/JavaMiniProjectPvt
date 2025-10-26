// File: src/com/bloodBank/view/BloodStockView.java
package com.bloodBank.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel; // <-- Import table model
import java.awt.*;
import java.util.Map;

public class BloodStockView extends JDialog {

    private JTable stockTable; // <-- Replaced JTextArea with JTable
    private DefaultTableModel tableModel; // <-- Added model to manage table data

    public BloodStockView(Frame owner) {
        super(owner, "Current Blood Stock", true); // Modal dialog
        setSize(350, 400);
        setLayout(new BorderLayout());
        
        // --- Setup the Table Model ---
        String[] columnNames = {"Blood Group", "Units in Stock"};
        
        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This makes the table cells non-editable
                return false;
            }
        };

        // --- Setup the JTable ---
        stockTable = new JTable(tableModel);
        
        // --- Modern Look & Feel ---
        stockTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        stockTable.setRowHeight(28); // Add vertical spacing
        stockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        stockTable.getTableHeader().setReorderingAllowed(false); // Don't let user drag columns
        
        // Center-align the text in the "Units in Stock" column
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        stockTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // --- Setup the Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(stockTable);
        // Add padding around the table
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        
        // Add scroll pane to the dialog's content pane
        add(scrollPane, BorderLayout.CENTER);
        
        setLocationRelativeTo(owner);
    }
    
    /**
     * The controller will call this to populate the table.
     */
    public void displayStock(Map<String, Integer> stock) {
        
        // --- MODIFICATION ---
        // Clear previous data from the table
        tableModel.setRowCount(0); 
        
        // Define all standard blood groups to ensure all are displayed
        final String[] ALL_BLOOD_GROUPS = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };

        // Loop through the *complete list* of blood groups
        for (String bloodGroup : ALL_BLOOD_GROUPS) {
            // Use getOrDefault to get the actual count, or 0 if the group isn't in the map
            int count = stock.getOrDefault(bloodGroup, 0);
            
            // Create a row of data
            Object[] rowData = { bloodGroup, count };
            
            // Add the row to the table model
            tableModel.addRow(rowData);
        }
    }
}