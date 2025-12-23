package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FormInputUMKM extends JFrame {
    private JTextField txtNama, txtPendapatan, txtPersen;
    private String namaDaerah;
    private HalamanUMKM parentHalaman;

    public FormInputUMKM(HalamanUMKM parent, String daerah) {
        this.parentHalaman = parent;
        this.namaDaerah = daerah;

        setTitle("Input UMKM Baru - " + namaDaerah);
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10)); // Layout rapi berbaris

        // --- Komponen GUI ---
        add(new JLabel(" Nama UMKM:"));
        txtNama = new JTextField();
        add(txtNama);

        add(new JLabel(" Pendapatan (Rp):"));
        txtPendapatan = new JTextField();
        add(txtPendapatan);

        add(new JLabel(" Tren Kenaikan (%):"));
        txtPersen = new JTextField();
        add(txtPersen);

        JButton btnSimpan = new JButton("Simpan Data");
        JButton btnBatal = new JButton("Batal");

        add(btnBatal);
        add(btnSimpan);

        // --- Logika Simpan ---
        btnSimpan.addActionListener(e -> simpanData());

        btnBatal.addActionListener(e -> dispose());
    }

    private void simpanData() {
        String nama = txtNama.getText();
        String pendapatanStr = txtPendapatan.getText();
        String persenStr = txtPersen.getText();

        // 1. Validasi Input Kosong
        if (nama.isEmpty() || pendapatanStr.isEmpty() || persenStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Exception Handling (Modul 6): Validasi Angka
        try {
            Double.parseDouble(pendapatanStr); // Cek apakah pendapatan angka
            Double.parseDouble(persenStr);    // Cek apakah persen angka
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Pendapatan dan Persen harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. File Handling (Modul 5): Simpan ke file daerah terkait
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaDaerah + ".txt", true))) {
            // Format simpan: Nama,Pendapatan,Persen
            bw.write(nama + "," + pendapatanStr + "," + persenStr + "%");
            bw.newLine();

            JOptionPane.showMessageDialog(this, "Data UMKM berhasil disimpan di " + namaDaerah);

            // Refresh tabel di halaman sebelumnya
            parentHalaman.loadDataFromFile();
            dispose(); // Tutup form input

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke file: " + ex.getMessage());
        }
    }
}
