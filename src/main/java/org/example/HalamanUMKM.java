package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*; // Untuk File Handling [cite: 50]

public class HalamanUMKM extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String namaDaerah;

    public HalamanUMKM(String daerah) {
        this.namaDaerah = daerah;
        setTitle("Data UMKM - " + namaDaerah);
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Judul Spesifik Daerah
        JLabel header = new JLabel("Daftar UMKM Kecamatan: " + namaDaerah, JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        // Tabel UMKM
        String[] columns = {"Nama UMKM", "Pendapatan (Rp)", "Tren (%)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol Navigasi
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah Data UMKM");
        JButton btnKembali = new JButton("Kembali");
        panelAksi.add(btnTambah);
        panelAksi.add(btnKembali);
        add(panelAksi, BorderLayout.SOUTH);

        // LOAD DATA SPESIFIK DAERAH
        loadDataFromFile();

        // Navigasi Kembali
        btnKembali.addActionListener(e -> {
            new EkonomiHome().setVisible(true);
            this.dispose();
        });

        // Menuju Form Input (Halaman 3)
        btnTambah.addActionListener(e -> {
            // Kita kirim namaDaerah agar Form Input tahu harus simpan ke file mana
            new FormInputUMKM(this, namaDaerah).setVisible(true);
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click untuk melihat detail
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String nama = model.getValueAt(row, 0).toString();
                        String pendapatan = model.getValueAt(row, 1).toString();

                        // Buka Halaman ke-4
                        new HalamanDetailUMKM(nama, pendapatan).setVisible(true);
                    }
                }
            }
        });
    }

    // Fungsi memuat data hanya untuk daerah ini [cite: 47, 48]
    public void loadDataFromFile() {
        model.setRowCount(0); // Kosongkan tabel dulu
        String fileName = namaDaerah + ".txt"; // File unik per daerah

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Asumsi format: Nama,Pendapatan,Tren
                model.addRow(data);
            }
        } catch (FileNotFoundException e) {
            // Jika file belum ada, tidak apa-apa (berarti daerah masih kosong)
        } catch (IOException e) {
            // Exception Handling sesuai kriteria
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
}