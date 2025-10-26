// File: src/com/bloodBank/view/components/ModernButton.java
package com.bloodBank.view.components;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    public ModernButton(String text, Color primaryColor) {
        // 1. Set the button text
        super(text);

        // 2. Automatically define hover and pressed colors based on the primary color
        final Color hoverColor = primaryColor.brighter();
        final Color pressedColor = primaryColor.darker();

        // 3. Set font and padding
        setFont(new Font("SansSerif", Font.PLAIN, 18));
        setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

        // 4. Set initial colors and styles
        setForeground(Color.WHITE); // White text
        setBackground(primaryColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 5. Remove the default border painting and focus ring
        setBorderPainted(false);
        setFocusPainted(false);

        // 6. Add hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(primaryColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getMousePosition() != null) {
                    setBackground(hoverColor);
                } else {
                    setBackground(primaryColor);
                }
            }
        });
    }
}