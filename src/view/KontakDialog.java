// File: src/view/KontakDialog.java
package view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import model.Kontak;
import net.miginfocom.swing.MigLayout;

public class KontakDialog extends JDialog {

    private final JTextField namaField = new JTextField(25);
    private final JTextField teleponField = new JTextField(25);
    private final JTextField emailField = new JTextField(25);
    private final JButton saveButton = new JButton("Simpan");
    private final JButton cancelButton = new JButton("Batal");

    private Kontak kontak;

    public KontakDialog(JFrame owner) {
        super(owner, "Tambah Kontak Baru", true);
        this.kontak = new Kontak();
        setupComponents();
    }

    public KontakDialog(JFrame owner, Kontak kontakToEdit) {
        super(owner, "Edit Kontak", true);
        this.kontak = kontakToEdit;
        setupComponents();
        
        namaField.setText(kontakToEdit.getNama());
        teleponField.setText(kontakToEdit.getTelepon());
        emailField.setText(kontakToEdit.getEmail());
    }

    private void setupComponents() {
        setLayout(new MigLayout("fill, insets 30", "[right]20[grow]"));
        
        add(new JLabel("Nama *"), "");
        add(namaField, "growx, wrap");
        
        add(new JLabel("Telepon *"), "");
        add(teleponField, "growx, wrap");
        
        add(new JLabel("Email"), "");
        add(emailField, "growx, wrap");

        saveButton.setBackground(UIManager.getColor("Button.default.background"));
        saveButton.setForeground(UIManager.getColor("Button.default.foreground"));
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD));

        JPanel buttonPanel = new JPanel(new MigLayout("", "[]10[]"));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, "span, right");

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(getOwner());
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public Kontak getKontak() {
        kontak.setNama(namaField.getText().trim());
        kontak.setTelepon(teleponField.getText().trim());
        kontak.setEmail(emailField.getText().trim());
        return kontak;
    }
}