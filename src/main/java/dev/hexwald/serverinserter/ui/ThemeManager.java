package dev.hexwald.serverinserter.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static void applyDark() {
        FlatDarkLaf.setup();
    }

    public static void applyLight() {
        FlatLightLaf.setup();
    }

    public static void toggle(Window preferredWindow) {
        try {
            if (UIManager.getLookAndFeel().getName().contains("Dark")) {
                applyLight();
            } else {
                applyDark();
            }

            updateWindows(preferredWindow);

        } catch (Exception ex) {
            System.err.println("Could not switch theme: " + ex.getMessage());
        }
    }

    public static void toggle() {
        toggle(null);
    }

    private static void updateWindows(Window preferredWindow) {
        if (preferredWindow != null) {
            SwingUtilities.updateComponentTreeUI(preferredWindow);
            preferredWindow.invalidate();
            preferredWindow.validate();
            preferredWindow.repaint();
        }

        for (Window window : Window.getWindows()) {
            if (window != preferredWindow) {
                SwingUtilities.updateComponentTreeUI(window);
                window.invalidate();
                window.validate();
                window.repaint();
            }
        }
    }
}
