// File: src/worker/kontak/UpdateKontakWorker.java
package worker.kontak;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.KontakApiClient;
import model.Kontak;
import view.KontakFrame;

public class UpdateKontakWorker extends SwingWorker<Void, Void> {
    private final KontakFrame frame;
    private final KontakApiClient kontakApiClient;
    private final Kontak kontak;

    public UpdateKontakWorker(KontakFrame frame, KontakApiClient kontakApiClient, Kontak kontak) {
        this.frame = frame;
        this.kontakApiClient = kontakApiClient;
        this.kontak = kontak;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Memperbarui data kontak...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        kontakApiClient.update(kontak);
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Kontak berhasil diperbarui");
            JOptionPane.showMessageDialog(frame,
                    "Data kontak berhasil diperbarui.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Gagal memperbarui kontak");
            JOptionPane.showMessageDialog(frame,
                    "Error memperbarui data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}