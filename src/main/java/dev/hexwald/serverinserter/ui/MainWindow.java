package dev.hexwald.serverinserter.ui;

import dev.hexwald.serverinserter.model.ServerEntry;
import dev.hexwald.serverinserter.service.ServerService;
import dev.hexwald.serverinserter.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    private JTextField txtServers;
    private JTextField txtDat;
    private JButton loadButton;
    private JButton insertButton;
    private JButton defaultDatButton;
    private JLabel statusBar;
    private boolean busy;

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

        setStatus("Ready. Select servers.txt to start.");

        setVisible(true);
    }

    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 15, 5, 15));

        JPanel fields = new JPanel(new GridLayout(2, 1, 6, 6));

        txtServers = createFileField(true);
        txtDat = createFileField(false);

        fields.add(createChooser(
                "servers.txt:",
                txtServers,
                new FileNameExtensionFilter("Text files (*.txt)", "txt"),
                false
        ));
        fields.add(createChooser(
                "servers.dat:",
                txtDat,
                new FileNameExtensionFilter("Minecraft server list (*.dat)", "dat"),
                true
        ));

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

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));

        statusBar = new JLabel();
        statusBar.setBorder(new EmptyBorder(0, 5, 0, 5));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        loadButton = new JButton("Load Preview");
        insertButton = new JButton("Insert Servers");
        JButton themeButton = new JButton("Change Theme (D/L)");

        loadButton.addActionListener(e -> loadPreview());
        insertButton.addActionListener(e -> insert());
        themeButton.addActionListener(e -> ThemeManager.toggle(this));

        buttons.add(loadButton);
        buttons.add(insertButton);
        buttons.add(themeButton);

        panel.add(statusBar, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);

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

    private JPanel createChooser(String name, JTextField field, FileNameExtensionFilter filter, boolean includeDefaultButton) {

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(name);
        label.setPreferredSize(new Dimension(110, 20));

        JButton browse = new JButton("Browse");

        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                field.setText(chooser.getSelectedFile().getAbsolutePath());
                setStatus("Selected " + chooser.getSelectedFile().getName());
            }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.add(browse);

        if (includeDefaultButton) {
            defaultDatButton = new JButton("Use default");
            defaultDatButton.addActionListener(e -> useDefaultServersDat());
            actions.add(defaultDatButton);
        }

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.EAST);

        return panel;
    }

    private void loadPreview() {
        if (busy) {
            return;
        }

        setBusy(true, "Loading preview...");

        SwingWorker<List<ServerEntry>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ServerEntry> doInBackground() throws Exception {
                return ServerParser.parse(new File(txtServers.getText()));
            }

            @Override
            protected void done() {
                try {
                    List<ServerEntry> list = get();
                    tableModel.setServers(list);
                    setStatus("Loaded " + list.size() + " servers.");
                } catch (Exception ex) {
                    handleError("Could not load preview.", ex);
                } finally {
                    setBusy(false, null);
                }
            }
        };

        worker.execute();
    }

    private void insert() {
        if (busy) {
            return;
        }

        List<ServerEntry> entries = new ArrayList<>(tableModel.getServers());
        setBusy(true, "Inserting " + entries.size() + " servers...");

        SwingWorker<File, Void> worker = new SwingWorker<>() {
            @Override
            protected File doInBackground() throws Exception {
                return ServerService.insert(new File(txtDat.getText()), entries);
            }

            @Override
            protected void done() {
                try {
                    File backup = get();
                    String message = "Inserted " + entries.size() + " servers.";
                    if (backup != null) {
                        message += " Backup created: " + backup.getName();
                    }
                    setStatus(message);
                } catch (Exception ex) {
                    handleError("Could not insert servers.", ex);
                } finally {
                    setBusy(false, null);
                }
            }
        };

        worker.execute();
    }

    private void useDefaultServersDat() {
        File defaultServersDat = MinecraftPaths.getDefaultServersDat();
        txtDat.setText(defaultServersDat.getAbsolutePath());
        setStatus("Using default servers.dat path.");
    }

    private void setBusy(boolean busy, String message) {
        this.busy = busy;
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());

        if (loadButton != null) {
            loadButton.setEnabled(!busy);
        }
        if (insertButton != null) {
            insertButton.setEnabled(!busy);
        }
        if (defaultDatButton != null) {
            defaultDatButton.setEnabled(!busy);
        }

        if (message != null) {
            setStatus(message);
        }
    }

    private void setStatus(String message) {
        if (statusBar != null) {
            statusBar.setText(message);
        }
    }

    private void handleError(String title, Exception ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        String message = cause.getMessage() == null ? cause.toString() : cause.getMessage();
        setStatus(title + " " + message);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
