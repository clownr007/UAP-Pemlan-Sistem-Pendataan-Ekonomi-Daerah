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

        setTitle("Tambah UMKM - " + namaDaerah);
        setSize(500, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 125, 50));
        headerPanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblJudul = new JLabel("TAMBAH DATA UMKM", JLabel.CENTER);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(lblJudul);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 30));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new javax.swing.border.EmptyBorder(40, 60, 40, 60));

        txtNama = new JTextField();
        txtPendapatan = new JTextField();
        txtPersen = new JTextField();

        formPanel.add(new JLabel("Nama UMKM:")); formPanel.add(txtNama);
        formPanel.add(new JLabel("Pendapatan (Rp):")); formPanel.add(txtPendapatan);
        formPanel.add(new JLabel("Tren Kenaikan (%):")); formPanel.add(txtPersen);
        add(formPanel, BorderLayout.CENTER);

        // --- TOMBOL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        // Tombol Batal (Latar Merah)
        JButton btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(110, 40));
        btnBatal.setBackground(new Color(244, 67, 54)); // MERAH
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBatal.setFocusPainted(false); // Menghilangkan garis fokus agar lebih rapi

        // Tombol Simpan (Latar Hijau)
        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setPreferredSize(new Dimension(130, 40));
        btnSimpan.setBackground(new Color(46, 125, 50)); // HIJAU
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setFocusPainted(false);

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- LOGIKA ---
        btnBatal.addActionListener(e -> dispose());

        btnSimpan.addActionListener(e -> {
            String nama = txtNama.getText();
            // Membersihkan titik jika user mengetik manual (misal: 1.000.000 -> 1000000)
            String pendapatanRaw = txtPendapatan.getText().replace(".", "");
            String persenRaw = txtPersen.getText().replace("%", "");

            // 1. Validasi Input Kosong
            if (nama.isEmpty() || pendapatanRaw.isEmpty() || persenRaw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua kolom!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Tambahan: Validasi Angka agar program tidak crash saat load data
            try {
                Long.parseLong(pendapatanRaw);
                Double.parseDouble(persenRaw);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pendapatan dan Tren harus berupa angka!", "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Simpan ke File
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaDaerah + ".txt", true))) {
                // Simpan data murni tanpa titik (titik hanya untuk tampilan di tabel)
                bw.write(nama + "," + pendapatanRaw + "," + persenRaw + "%");
                bw.newLine();

                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");

                // 4. Refresh Tabel di Halaman Utama (Agar responsif)
                parentHalaman.loadDataFromFile();

                // 5. Tutup Jendela Input
                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
            }
        });
    }
}