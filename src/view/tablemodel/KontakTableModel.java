// File: src/view/tablemodel/KontakTableModel.java
package view.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Kontak;

public class KontakTableModel extends AbstractTableModel {
    private List<Kontak> kontakList = new ArrayList<>();
    private final String[] columnNames = { "ID", "Nama", "Telepon", "Email" };

    public void setKontakList(List<Kontak> kontakList) {
        this.kontakList = kontakList;
        fireTableDataChanged();
    }

    public Kontak getKontakAt(int rowIndex) {
        return kontakList.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return kontakList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Kontak kontak = kontakList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> kontak.getId();
            case 1 -> kontak.getNama();
            case 2 -> kontak.getTelepon();
            case 3 -> kontak.getEmail();
            default -> null;
        };
    }
}