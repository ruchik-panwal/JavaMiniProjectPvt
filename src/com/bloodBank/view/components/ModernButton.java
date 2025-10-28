// File: src/com/bloodBank/view/components/ModernButton.java
package com.bloodBank.view.components;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    private int cornerRadius = 15;

    public ModernButton(String text, Color primaryColor) {
        // 1. Set the button text
        super(text);

        // 2. Automatically define hover and pressed colors (MODIFIED)
        final Color hoverColor = primaryColor.darker(); // Darken on hover
        final Color pressedColor = hoverColor.darker(); // Make pressed even darker

        // 3. Set font and padding
        setFont(new Font("SansSerif", Font.BOLD, 30));
        setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

        // 4. Set initial colors and styles
        setForeground(Color.WHITE);
        setBackground(primaryColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 5. Remove the default border painting and focus ring
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false); // Make the button transparent

        // 6. Add hover and click effects (MouseAdapter updated)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor); // Use darker hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(primaryColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor); // Use the even-darker pressed color
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getMousePosition() != null) {
                    setBackground(hoverColor); // Revert to hover color
                } else {
                    setBackground(primaryColor);
                }
            }
        });
    }

    // Override paintComponent to draw our custom rounded background
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Let the button's UI paint the text
        super.paintComponent(g);
        
        g2.dispose();
    }
}