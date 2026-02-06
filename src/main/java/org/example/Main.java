package org.example;

import org.example.ui.MainWindow;
import org.example.ui.ThemeManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ThemeManager.applyDark();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}