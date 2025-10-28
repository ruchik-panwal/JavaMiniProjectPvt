// File: src/com/bloodBank/view/BloodStockView.java
package com.bloodBank.view;

import com.bloodBank.model.BloodUnit;
import com.bloodBank.model.BloodStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodStockView extends JDialog {

    // --- NEW: Color Palette & Fonts ---
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 249);
    private static final Color COLOR_HEADER = new Color(20, 100, 140); // Darker Blue
    private static final Color COLOR_TABLE_GRID = new Color(220, 220, 220);
    private static final Color COLOR_SELECTION = new Color(175, 215, 245); 

    private static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 17);
    private static final Font FONT_TABLE_BODY = new Font("SansSerif", Font.PLAIN, 16);
    
    // Formatter for displaying dates in a friendly way
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private JTable detailedTable;
    private DefaultTableModel detailedTableModel;
    private JTable summaryTable; // NEW
    private DefaultTableModel summaryTableModel; // NEW

    public BloodStockView(Frame owner) {
        super(owner, "Detailed Blood Inventory & Summary", true);
        
        // --- WINDOW SIZE (MODIFIED) ---
        setSize(1900, 1000); // Maximize the view
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BACKGROUND);

        // ------------------------------------
        // 1. SETUP THE SUMMARY PANEL (Left Side)
        // ------------------------------------
        JPanel summaryPanel = createSummaryPanel();

        // ------------------------------------
        // 2. SETUP THE DETAILED TABLE PANEL (Right Side)
        // ------------------------------------
        JScrollPane detailedScrollPane = createDetailedTablePane();

        // ------------------------------------
        // 3. COMBINE WITH JSplitPane
        // ------------------------------------
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, summaryPanel, detailedScrollPane);
        splitPane.setDividerLocation(350); // Give 350px width to the summary panel
        splitPane.setResizeWeight(0.0); // Keep summary size fixed on resize
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(splitPane, BorderLayout.CENTER);
        setLocationRelativeTo(owner);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BACKGROUND);

        JLabel title = new JLabel("Blood Group Summary", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        title.setForeground(COLOR_HEADER.darker());
        panel.add(title, BorderLayout.NORTH);

        // Model for the summary table
        String[] summaryCols = {"Blood Group", "In Stock Units"};
        summaryTableModel = new DefaultTableModel(summaryCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Integer.class : String.class;
            }
        };

        summaryTable = new JTable(summaryTableModel);
        
        // Styling the summary table
        JTableHeader header = summaryTable.getTableHeader();
        header.setFont(FONT_HEADER.deriveFont(Font.BOLD, 18f));
        header.setBackground(COLOR_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45)); 

        summaryTable.setFont(FONT_TABLE_BODY.deriveFont(20f)); // Bigger font for summary counts
        summaryTable.setRowHeight(45);
        summaryTable.setGridColor(COLOR_TABLE_GRID);
        summaryTable.setDefaultRenderer(Object.class, new SummaryCellRenderer()); // Custom renderer

        JScrollPane scroll = new JScrollPane(summaryTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Inner padding
        panel.add(scroll, BorderLayout.CENTER);
        
        // Initialize 8 blood groups (will be updated by controller)
        initializeSummaryData(); 
        
        return panel;
    }

    private JScrollPane createDetailedTablePane() {
        // --- Setup the Table Model ---
        String[] columnNames = {
            "Unit ID", "Blood Group", "Donor ID",
            "Donation Date", "Expiry Date", "Status"
        };
        detailedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // --- Setup the JTable ---
        detailedTable = new JTable(detailedTableModel);

        // --- Modern Look & Feel ---
        detailedTable.setFont(FONT_TABLE_BODY);
        detailedTable.setRowHeight(32);
        detailedTable.getTableHeader().setFont(FONT_HEADER);
        detailedTable.getTableHeader().setBackground(COLOR_HEADER);
        detailedTable.getTableHeader().setForeground(Color.WHITE);
        detailedTable.getTableHeader().setReorderingAllowed(false);
        detailedTable.setFillsViewportHeight(true); 
        detailedTable.setGridColor(COLOR_TABLE_GRID);
        detailedTable.setSelectionBackground(COLOR_SELECTION);
        detailedTable.setSelectionForeground(Color.BLACK);

        // --- Add Custom Renderer for Status Colors (applies to whole row) ---
        detailedTable.setDefaultRenderer(Object.class, new StatusCellRenderer());

        // --- Setup Column Widths ---
        detailedTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Unit ID
        detailedTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Blood Group
        detailedTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Donor ID
        detailedTable.getColumnModel().getColumn(3).setPreferredWidth(140); // Donation Date
        detailedTable.getColumnModel().getColumn(4).setPreferredWidth(140); // Expiry Date
        detailedTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        
        JScrollPane scrollPane = new JScrollPane(detailedTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);

        return scrollPane;
    }

    /**
     * Initializes the summary table with all 8 blood groups and zero counts.
     */
    private void initializeSummaryData() {
        // This is a placeholder, as the actual BloodGroup enum is not provided.
        // Assuming the BloodGroup enum provides A_POS, O_NEG, etc.
        String[] allGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String group : allGroups) {
            summaryTableModel.addRow(new Object[]{group, 0});
        }
    }

    /**
     * The controller will call this to populate the detailed table and summary.
     */
    public void displayStock(ArrayList<BloodUnit> units) {
        // 1. Clear previous data
        detailedTableModel.setRowCount(0);

        // Map to hold summary counts
        Map<String, Integer> stockCounts = new HashMap<>();
        String[] allGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String group : allGroups) {
            stockCounts.put(group, 0); // Initialize counts
        }

        // 2. Loop through the list of BloodUnit objects for details and summary count
        for (BloodUnit unit : units) {
            // Update detailed table
            Object[] rowData = {
                unit.getUnitId(),
                unit.getBloodGroup(),
                unit.getDonorId(),
                unit.getDonationDate().format(DATE_FORMATTER),
                unit.getExpiryDate().format(DATE_FORMATTER),
                unit.getStatus()
            };
            detailedTableModel.addRow(rowData);
            
            // Update stock summary count
            if (unit.getStatus() == BloodStatus.IN_STOCK && stockCounts.containsKey(unit.getBloodGroup())) {
                stockCounts.put(unit.getBloodGroup(), stockCounts.get(unit.getBloodGroup()) + 1);
            }
        }
        
        // 3. Update the summary table model
        for (int i = 0; i < summaryTableModel.getRowCount(); i++) {
            String group = (String) summaryTableModel.getValueAt(i, 0);
            summaryTableModel.setValueAt(stockCounts.getOrDefault(group, 0), i, 1);
        }
    }

    // ------------------------------------
    // Custom Renderer Classes
    // ------------------------------------

    /**
     * Inner class to render detailed table cells with colors based on blood unit status.
     */
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        
        // Define status colors (softened)
        private final Color COLOR_EXPIRED = new Color(255, 230, 230); // Light Red/Pink
        private final Color COLOR_EXPIRING_SOON = new Color(255, 255, 204); // Light Yellow
        private final Color COLOR_USED = new Color(235, 235, 235); // Light Gray
        private final Color COLOR_IN_STOCK = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the status from the table model for the current row
            BloodStatus status = (BloodStatus) table.getModel().getValueAt(row, 5);
            
            // Get expiry date for expiring soon check
            String expiryDateStr = (String) table.getModel().getValueAt(row, 4);
            LocalDate expiryDate = LocalDate.parse(expiryDateStr, DATE_FORMATTER);
            boolean isExpiringSoon = expiryDate.isBefore(LocalDate.now().plusDays(7)) && 
                                     status == BloodStatus.IN_STOCK;

            // Set colors based on status
            if (isSelected) {
                c.setBackground(COLOR_SELECTION);
                c.setForeground(Color.BLACK); // Clear text color
            } else {
                c.setForeground(Color.BLACK); 
                switch (status) {
                    case EXPIRED:
                        c.setBackground(COLOR_EXPIRED);
                        break;
                    case ISSUED:
                        c.setBackground(COLOR_USED);
                        c.setForeground(Color.GRAY.darker()); // Dim the text for used units
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
            // Center alignment for all cells except text
            if (column != 5) { // Status column (5) should be left-aligned
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            return c;
        }
    }
    
    /**
     * Inner class to color code the Summary Table based on stock quantity.
     */
    private class SummaryCellRenderer extends DefaultTableCellRenderer {

        // Define stock level colors
        private final Color COLOR_HIGH_STOCK = new Color(197, 245, 197); // Light Green
        private final Color COLOR_MEDIUM_STOCK = new Color(255, 255, 190); // Light Yellow
        private final Color COLOR_LOW_STOCK = new Color(255, 230, 230); // Light Red

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 1) { // Only apply color code to the 'In Stock Units' column
                setHorizontalAlignment(SwingConstants.CENTER);
                int count = (Integer) table.getModel().getValueAt(row, column);
                
                if (isSelected) {
                    c.setBackground(COLOR_SELECTION);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.BLACK);
                    if (count >= 20) {
                        c.setBackground(COLOR_HIGH_STOCK);
                    } else if (count >= 5) {
                        c.setBackground(COLOR_MEDIUM_STOCK);
                    } else if (count > 0) {
                        c.setBackground(COLOR_LOW_STOCK);
                    } else {
                        c.setBackground(Color.WHITE); // Zero stock
                        c.setForeground(Color.GRAY.darker());
                    }
                }
            } else { // Blood Group column (index 0)
                setHorizontalAlignment(SwingConstants.LEFT);
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }
}