// File: src/worker/kontak/DeleteKontakWorker.java
package worker.kontak;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.KontakApiClient;
import model.Kontak;
import view.KontakFrame;

public class DeleteKontakWorker extends SwingWorker<Void, Void> {
    private final KontakFrame frame;
    private final KontakApiClient kontakApiClient;
    private final Kontak kontak;

    public DeleteKontakWorker(KontakFrame frame, KontakApiClient kontakApiClient, Kontak kontak) {
        this.frame = frame;
        this.kontakApiClient = kontakApiClient;
        this.kontak = kontak;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Menghapus kontak...");
    }

    @Override
    protected Void doInBackground() throws Exception {
        kontakApiClient.delete(kontak.getId());
        return null;
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            get();
            frame.getProgressBar().setString("Kontak berhasil dihapus");
            JOptionPane.showMessageDialog(frame,
                    "Kontak berhasil dihapus.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            frame.getProgressBar().setString("Gagal menghapus kontak");
            JOptionPane.showMessageDialog(frame,
                    "Error menghapus data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}