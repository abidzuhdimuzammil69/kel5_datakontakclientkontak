// File: src/worker/kontak/LoadKontakWorker.java
package worker.kontak;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import api.KontakApiClient;
import model.Kontak;
import view.KontakFrame;

public class LoadKontakWorker extends SwingWorker<List<Kontak>, Void> {
    private final KontakFrame frame;
    private final KontakApiClient kontakApiClient;

    public LoadKontakWorker(KontakFrame frame, KontakApiClient kontakApiClient) {
        this.frame = frame;
        this.kontakApiClient = kontakApiClient;
        frame.getProgressBar().setIndeterminate(true);
        frame.getProgressBar().setString("Memuat data kontak...");
    }

    @Override
    protected List<Kontak> doInBackground() throws Exception {
        return kontakApiClient.findAll();
    }

    @Override
    protected void done() {
        frame.getProgressBar().setIndeterminate(false);
        try {
            List<Kontak> result = get();
            frame.getProgressBar().setString(result.size() + " kontak dimuat");
        } catch (Exception e) {
            frame.getProgressBar().setString("Gagal memuat data");
            JOptionPane.showMessageDialog(frame,
                    "Error memuat data: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}