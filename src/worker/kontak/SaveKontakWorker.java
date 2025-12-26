// File: src/worker/kontak/SaveKontakWorker.java
package worker.kontak;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.KontakApiClient;
import model.Kontak;
import view.KontakFrame;

public class SaveKontakWorker extends SwingWorker<Void, Void> {
    private final KontakFrame frame;
    private final KontakApiClient kontakApiClient;
    private final Kontak kontak;

    public SaveKontakWorker(KontakFrame frame, KontakApiClient kontakApiClient, Kontak kontak) {
        this.frame = frame;
        this.kontakApiClient = kontakApiClient;
        this.kontak = kontak;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Menyimpan kontak baru...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        kontakApiClient.create(kontak);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Kontak berhasil disimpan");
            JOptionPane.showMessageDialog(frame,
                    "Kontak baru berhasil disimpan.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Gagal menyimpan kontak");
            JOptionPane.showMessageDialog(frame,
                    "Error menyimpan data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}