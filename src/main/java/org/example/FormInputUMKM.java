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
        setSize(500, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout()); // Layout rapi berbaris

        txtNama = new JTextField();
        txtPendapatan = new JTextField();
        txtPersen = new JTextField();

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 125, 50)); // Hijau
        headerPanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblJudul = new JLabel("TAMBAH DATA UMKM", JLabel.CENTER);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(lblJudul);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 30));
        formPanel.setBackground(Color.WHITE);
        // Memberi jarak agar tidak mentok ke pinggir (Atas, Kiri, Bawah, Kanan)
        formPanel.setBorder(new javax.swing.border.EmptyBorder(40, 60, 40, 60));

        // --- Komponen GUI ---
        formPanel.add(new JLabel("Nama UMKM:"));
        formPanel.add(txtNama);
        formPanel.add(new JLabel("Pendapatan (Rp):"));
        formPanel.add(txtPendapatan);
        formPanel.add(new JLabel("Tren Kenaikan (%):"));
        formPanel.add(txtPersen);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setPreferredSize(new Dimension(130, 40));

        JButton btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);
        add(buttonPanel, BorderLayout.SOUTH);


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
