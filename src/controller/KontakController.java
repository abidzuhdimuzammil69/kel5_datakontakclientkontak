// File: src/controller/KontakController.java
package controller;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import api.KontakApiClient;
import model.Kontak;
import view.KontakDialog;
import view.KontakFrame;
import worker.kontak.DeleteKontakWorker;
import worker.kontak.LoadKontakWorker;
import worker.kontak.SaveKontakWorker;
import worker.kontak.UpdateKontakWorker;

public class KontakController {
    private final KontakFrame frame;
    private final KontakApiClient kontakApiClient = new KontakApiClient();

    private List<Kontak> allKontak = new ArrayList<>();
    private List<Kontak> displayedKontak = new ArrayList<>();

    public KontakController(KontakFrame frame) {
        this.frame = frame;
        setupEventListeners();
        loadAllKontak();
    }

    private void setupEventListeners() {
        frame.getAddButton().addActionListener(e -> openKontakDialog(null));
        frame.getRefreshButton().addActionListener(e -> loadAllKontak());
        frame.getDeleteButton().addActionListener(e -> deleteSelectedKontak());
        frame.getKontakTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = frame.getKontakTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        openKontakDialog(displayedKontak.get(selectedRow));
                    }
                }
            }
        });
        frame.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearchFilter();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearchFilter();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearchFilter();
            }
            private void applySearchFilter() {
                String keyword = frame.getSearchField().getText().toLowerCase().trim();
                displayedKontak = new ArrayList<>();
                for (Kontak kontak : allKontak) {
                    if (kontak.getNama().toLowerCase().contains(keyword) ||
                            kontak.getTelepon().toLowerCase().contains(keyword) ||
                            (kontak.getEmail() != null && kontak.getEmail().toLowerCase().contains(keyword))) {
                        displayedKontak.add(kontak);
                    }
                }
                frame.getKontakTableModel().setKontakList(displayedKontak);
                updateTotalRecordsLabel();
            }
        });
    }

    private void openKontakDialog(Kontak kontakToEdit) {
        KontakDialog dialog;
        if (kontakToEdit == null) {
            dialog = new KontakDialog(frame);
        } else {
            dialog = new KontakDialog(frame, kontakToEdit);
        }
        dialog.getSaveButton().addActionListener(e -> {
            Kontak kontak = dialog.getKontak();
            SwingWorker<Void, Void> worker;
            if (kontakToEdit == null) {
                worker = new SaveKontakWorker(frame, kontakApiClient, kontak);
            } else {
                worker = new UpdateKontakWorker(frame, kontakApiClient, kontak);
            }
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    dialog.dispose();
                    loadAllKontak();
                }
            });
            worker.execute();
        });
        dialog.setVisible(true);
    }

    private void deleteSelectedKontak() {
        int selectedRow = frame.getKontakTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Pilih kontak yang akan dihapus.");
            return;
        }
        Kontak kontak = displayedKontak.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Hapus kontak: " + kontak.getNama() + " - " + kontak.getTelepon() + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            DeleteKontakWorker worker = new DeleteKontakWorker(frame, kontakApiClient, kontak);
            worker.addPropertyChangeListener(evt -> {
                if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                    loadAllKontak();
                }
            });
            worker.execute();
        }
    }

    private void loadAllKontak() {
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Memuat data...");
        LoadKontakWorker worker = new LoadKontakWorker(frame, kontakApiClient);
        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    allKontak = worker.get();
                    displayedKontak = new ArrayList<>(allKontak);
                    frame.getKontakTableModel().setKontakList(displayedKontak);
                    updateTotalRecordsLabel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Gagal memuat data.");
                } finally {
                    frame.getProgressBar().setIndeterminate(false);
                    frame.getProgressBar().setString("Siap");
                }
            }
        });
        worker.execute();
    }

    private void updateTotalRecordsLabel() {
        frame.getTotalRecordsLabel().setText(displayedKontak.size() + " Kontak");
    }
}