package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class EkonomiHome extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public EkonomiHome() {
        setTitle("Sistem Monitoring Ekonomi Daerah");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 125, 50)); // Warna Hijau Tua
        JLabel labelJudul = new JLabel("DASHBOARD EKONOMI DAERAH", JLabel.CENTER);
        labelJudul.setForeground(Color.WHITE); // Teks jadi putih
        labelJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(labelJudul);
        add(headerPanel, BorderLayout.NORTH);

        // --- Tabel ---
        String[] columns = {"Nama Daerah", "Total Pendapatan (Rp)", "Rata-rata Tren (%)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; } // Sel tidak bisa diedit ketik
        };

        table = new JTable(model);
        table.setRowHeight(40); // Baris lebih tinggi agar mudah diklik
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedColumn() != 0 && table.getSelectedColumn() != -1) {
                // Jika user klik kolom lain, paksa balik ke kolom 0
                table.setColumnSelectionInterval(0, 0);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // Angka 80 di bawah ini yang membuat tabel tidak mentok pinggir (berada di tengah)
        centerPanel.setBorder(new javax.swing.border.EmptyBorder(30, 80, 30, 80));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- Footer Informasi ---
        JLabel lblInfo = new JLabel(" * Klik 2x pada nama daerah untuk melihat detail UMKM", JLabel.LEFT);
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        add(lblInfo, BorderLayout.SOUTH);

        // --- Load Data & Logika Klik ---
        String[] daftarDaerah = {"Kecamatan Lowokwaru", "Kecamatan Klojen", "Kecamatan Blimbing", "Kecamatan Kedunkandang", "Kecamatan Sukun"};
        for (String daerah : daftarDaerah) {
            updateRowData(daerah);
        }

        // LOGIKA PINDAH HALAMAN SAAT DIKLIK (Tanpa Button)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Ambil indeks kolom yang benar-benar diklik oleh mouse
                int columnUnderMouse = table.columnAtPoint(e.getPoint());

                // HANYA jalan jika klik dilakukan di kolom 0 (Nama Daerah)
                if (columnUnderMouse == 0) {
                    if (e.getClickCount() == 2) { // Double click
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            String namaDaerah = model.getValueAt(row, 0).toString();
                            new HalamanUMKM(namaDaerah).setVisible(true);
                            dispose();
                        }
                    }
                }
                // Jika klik di kolom 1 atau 2, program diam saja (tidak ada interaksi)
            }
        });
    }

    private void updateRowData(String namaDaerah) {
        double totalPendapatan = 0;
        double totalPersen = 0;
        int jumlahUMKM = 0;
        String fileName = namaDaerah + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    totalPendapatan += Double.parseDouble(data[1].trim());
                    String persenStr = data[2].replace("%", "").trim();
                    totalPersen += Double.parseDouble(persenStr);
                    jumlahUMKM++;
                }
            }
        } catch (Exception e) { /* File belum ada atau kosong */ }

        String rataRataPersen = (jumlahUMKM > 0) ? String.format("%.2f", totalPersen / jumlahUMKM) + "%" : "-";
        String formatPendapatan = (totalPendapatan > 0) ? String.format("%,.0f", totalPendapatan) : "-";

        model.addRow(new Object[]{namaDaerah, formatPendapatan, rataRataPersen});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EkonomiHome().setVisible(true));
    }
}