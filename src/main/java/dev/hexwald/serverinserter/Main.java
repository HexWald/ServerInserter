package dev.hexwald.serverinserter;

import dev.hexwald.serverinserter.ui.MainWindow;
import dev.hexwald.serverinserter.ui.ThemeManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ThemeManager.applyDark();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}
