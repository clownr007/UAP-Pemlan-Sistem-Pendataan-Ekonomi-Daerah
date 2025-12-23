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
        JPanel panelData = new JPanel(new GridLayout(5, 2, 10, 10));
        panelData.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelData.add(new JLabel("Nama UMKM:"));
        panelData.add(new JLabel(namaUMKM));

        panelData.add(new JLabel("Pendapatan 1 Hari:"));
        panelData.add(new JLabel("Rp " + formatRupiah(pendapatanHarian)));

        panelData.add(new JLabel("Estimasi 1 Minggu (7 hari):"));
        panelData.add(new JLabel("Rp " + formatRupiah(pendapatanHarian * 7)));

        panelData.add(new JLabel("Estimasi 1 Bulan (30 hari):"));
        panelData.add(new JLabel("Rp " + formatRupiah(pendapatanHarian * 30)));

        panelData.add(new JLabel("Estimasi 1 Tahun (365 hari):"));
        panelData.add(new JLabel("Rp " + formatRupiah(pendapatanHarian * 365)));

        add(panelData, BorderLayout.CENTER);

        // --- Tombol Tutup ---
        JButton btnTutup = new JButton("Tutup Detail");
        btnTutup.addActionListener(e -> dispose());
        add(btnTutup, BorderLayout.SOUTH);
    }

    // Helper untuk merapikan format angka
    private String formatRupiah(double nilai) {
        return String.format("%,.0f", nilai);
    }

}
