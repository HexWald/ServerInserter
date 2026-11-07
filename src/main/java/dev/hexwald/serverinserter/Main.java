package dev.hexwald.serverinserter;

import dev.hexwald.serverinserter.ui.MainWindow;
import dev.hexwald.serverinserter.ui.ThemeManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ThemeManager.applyDark();
            new MainWindow().setVisible(true);
        });
    }
}
