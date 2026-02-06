package org.example.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ThemeManager {

    public static void applyDark() {
        FlatDarkLaf.setup();
    }

    public static void applyLight() {
        FlatLightLaf.setup();
    }

    public static void toggle() {
        try {
            if (UIManager.getLookAndFeel().getName().contains("Dark")) {
                applyLight();
            } else {
                applyDark();
            }

            SwingUtilities.updateComponentTreeUI(
                    JFrame.getFrames()[0]
            );

        } catch (Exception ignored) {}
    }
}
