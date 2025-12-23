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
        JLabel labelJudul = new JLabel("DASHBOARD EKONOMI DAERAH", JLabel.CENTER);
        labelJudul.setFont(new Font("SansSerif", Font.BOLD, 22));
        labelJudul.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(labelJudul, BorderLayout.NORTH);

        // --- Tabel ---
        String[] columns = {"Nama Daerah", "Total Pendapatan (Rp)", "Rata-rata Tren (%)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; } // Sel tidak bisa diedit ketik
        };

        table = new JTable(model);
        table.setRowHeight(40); // Baris lebih tinggi agar mudah diklik
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

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
                // Menggunakan double click (2 kali klik) agar tidak sengaja terpencet
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String namaDaerah = model.getValueAt(row, 0).toString();

                        // Berpindah ke Halaman UMKM
                        new HalamanUMKM(namaDaerah).setVisible(true);
                        dispose(); // Menutup halaman utama
                    }
                }
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