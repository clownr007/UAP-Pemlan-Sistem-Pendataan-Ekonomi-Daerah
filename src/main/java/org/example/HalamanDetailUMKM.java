package org.example;

import javax.swing.*;
import java.awt.*;

public class HalamanDetailUMKM extends JFrame {
    private String namaUMKM;
    private double pendapatanHarian;

    public HalamanDetailUMKM(String nama, String pendapatanStr) {
        this.namaUMKM = nama;

        // Exception Handling: Mengubah String ke Double untuk perhitungan
        try {
            this.pendapatanHarian = Double.parseDouble(pendapatanStr.replace("Rp", "").replace(".", "").trim());
        } catch (NumberFormatException e) {
            this.pendapatanHarian = 0;
        }

        setTitle("Detail Ekonomi - " + namaUMKM);
        setSize(450, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // --- Header ---
        JLabel lblJudul = new JLabel("Laporan Proyeksi Pendapatan", JLabel.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lblJudul, BorderLayout.NORTH);

        // --- Panel Data (Grid Layout) ---
        // 1. Membuat Panel kartu dengan susunan vertikal (5 baris ke bawah)
        JPanel card = new JPanel(new GridLayout(5, 1, 0, 15));
        card.setBackground(Color.WHITE);

// 2. Membuat bingkai abu-abu tipis dan jarak di dalamnya (Padding)
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new javax.swing.border.EmptyBorder(20, 25, 20, 25),
                javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));

// 3. Memasukkan data proyeksi satu per satu secara lengkap
        card.add(createRow("NAMA UMKM", nama));
        card.add(createRow("PROYEKSI PENDAPATAN 1 HARI", "Rp " + formatRupiah(pendapatanHarian)));
        card.add(createRow("ESTIMASI PENDAPATAN 1 MINGGU", "Rp " + formatRupiah(pendapatanHarian * 7)));
        card.add(createRow("ESTIMASI PENDAPATAN 1 BULAN", "Rp " + formatRupiah(pendapatanHarian * 30)));
        card.add(createRow("ESTIMASI PENDAPATAN 1 TAHUN", "Rp " + formatRupiah(pendapatanHarian * 365)));

// 4. Masukkan kartu data ke tengah layar
        add(card, BorderLayout.CENTER);

        // --- Tombol Tutup ---
        JButton btnTutup = new JButton("Tutup Detail");
        btnTutup.addActionListener(e -> dispose());
        add(btnTutup, BorderLayout.SOUTH);
    }

    // Helper untuk merapikan format angka
    private String formatRupiah(double nilai) {
        return String.format("%,.0f", nilai);
    }

    private JPanel createRow(String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);

        // Label keterangan (kecil di atas)
        JLabel lblKet = new JLabel(label);
        lblKet.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblKet.setForeground(Color.GRAY);

        // Nilai uang (tebal di bawah)
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVal.setForeground(new Color(46, 125, 50));

        rowPanel.add(lblKet, BorderLayout.NORTH);
        rowPanel.add(lblVal, BorderLayout.SOUTH);

        return rowPanel;
    }
}
