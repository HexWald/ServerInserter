package dev.hexwald.serverinserter.ui;

import dev.hexwald.serverinserter.model.ServerEntry;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ServerTableModel extends AbstractTableModel {

    private final List<ServerEntry> servers = new ArrayList<>();

    public void setServers(List<ServerEntry> list) {
        servers.clear();
        servers.addAll(list);
        fireTableDataChanged();
    }

    public List<ServerEntry> getServers() {
        return servers;
    }

    @Override
    public int getRowCount() {
        return servers.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Name" : "IP";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ServerEntry s = servers.get(rowIndex);
        return columnIndex == 0 ? s.getName() : s.getIp();
    }
}
