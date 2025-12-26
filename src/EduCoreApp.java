// File: EduCoreApp.java
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

import controller.KontakController;
import view.KontakFrame;

public class EduCoreApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightFlatIJTheme());
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            KontakFrame frame = new KontakFrame();
            new KontakController(frame);
            frame.setVisible(true);
        });
    }
}