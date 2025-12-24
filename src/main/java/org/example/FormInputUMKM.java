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

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 125, 50));
        headerPanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblJudul = new JLabel("TAMBAH DATA UMKM", JLabel.CENTER);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(lblJudul);
        add(headerPanel, BorderLayout.NORTH);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(110, 40));
        btnBatal.setBackground(new Color(244, 67, 54)); // MERAH
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBatal.setFocusPainted(false); // Menghilangkan garis fokus agar lebih rapi

        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setPreferredSize(new Dimension(130, 40));
        btnSimpan.setBackground(new Color(46, 125, 50)); // HIJAU
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setFocusPainted(false);

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBatal.addActionListener(e -> dispose());

        btnSimpan.addActionListener(e -> {
            String nama = txtNama.getText();
            String pendapatanRaw = txtPendapatan.getText().replace(".", "");
            String persenRaw = txtPersen.getText().replace("%", "");

            if (nama.isEmpty() || pendapatanRaw.isEmpty() || persenRaw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua kolom!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Long.parseLong(pendapatanRaw);
                Double.parseDouble(persenRaw);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pendapatan dan Tren harus berupa angka!", "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaDaerah + ".txt", true))) {
                bw.write(nama + "," + pendapatanRaw + "," + persenRaw + "%");
                bw.newLine();

                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");

                parentHalaman.loadDataFromFile();

                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
            }
        });
    }
}