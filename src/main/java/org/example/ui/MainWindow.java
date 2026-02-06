package org.example.ui;

import org.example.model.ServerEntry;
import org.example.service.ServerService;
import org.example.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainWindow extends JFrame {

    private JTextField txtServers;
    private JTextField txtDat;

    private final ServerTableModel tableModel = new ServerTableModel();

    public MainWindow() {

        setTitle("Minecraft Server Inserter");
        setSize(900, 650);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(8, 8));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 15, 5, 15));

        JPanel fields = new JPanel(new GridLayout(2, 1, 6, 6));

        txtServers = createFileField(true);
        txtDat = createFileField(false);

        fields.add(createChooser("servers.txt:", txtServers));
        fields.add(createChooser("servers.dat:", txtDat));

        panel.add(fields, BorderLayout.NORTH);

        JTextArea hint = new JTextArea(MinecraftPaths.getHints());
        hint.setEditable(false);
        hint.setOpaque(false);
        hint.setFont(hint.getFont().deriveFont(11f));
        hint.setBorder(new EmptyBorder(5, 5, 0, 5));

        panel.add(hint, BorderLayout.CENTER);

        return panel;
    }

    private JComponent createTablePanel() {

        JTable table = new JTable(tableModel);

        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(5, 15, 5, 15));

        return scroll;
    }

    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JButton load = new JButton("Load Preview");
        JButton insert = new JButton("Insert Servers");
        JButton theme = new JButton("Toggle Theme");

        load.addActionListener(e -> loadPreview());
        insert.addActionListener(e -> insert());
        theme.addActionListener(e -> ThemeManager.toggle());

        panel.add(load);
        panel.add(insert);
        panel.add(theme);

        return panel;
    }

    private JTextField createFileField(boolean previewOnDrop) {

        JTextField field = new JTextField();
        field.setBorder(new EmptyBorder(6, 8, 6, 8));

        field.setTransferHandler(new FileDropHandler(file -> {
            field.setText(file.getAbsolutePath());
            if (previewOnDrop) loadPreview();
        }));

        return field;
    }

    private JPanel createChooser(String name, JTextField field) {

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(name);
        label.setPreferredSize(new Dimension(110, 20));

        JButton browse = new JButton("Browse");

        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                field.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.add(browse, BorderLayout.EAST);

        return panel;
    }

    private void loadPreview() {

        try {
            List<ServerEntry> list =
                    ServerParser.parse(new File(txtServers.getText()));

            tableModel.setServers(list);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void insert() {

        try {
            ServerService.insert(
                    new File(txtDat.getText()),
                    tableModel.getServers()
            );

            JOptionPane.showMessageDialog(this, "Servers inserted!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}