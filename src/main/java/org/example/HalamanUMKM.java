package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class HalamanUMKM extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String namaDaerah;

    public HalamanUMKM(String daerah) {
        this.namaDaerah = daerah;
        setTitle("Data UMKM - " + namaDaerah);
        setSize(800, 550); // Ukuran sedikit lebih tinggi untuk estetika
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // 1. HEADER (Warna Hijau Dashboard)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 125, 50));
        headerPanel.setPreferredSize(new Dimension(800, 70));

        JLabel lblJudul = new JLabel("DAFTAR UMKM: " + namaDaerah.toUpperCase(), JLabel.CENTER);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblJudul, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // 2. TABEL UMKM
        String[] columns = {"Nama UMKM", "Pendapatan (Rp)", "Tren (%)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedColumn() != 0 && table.getSelectedColumn() != -1) {
                table.setColumnSelectionInterval(0, 0);
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new javax.swing.border.EmptyBorder(20, 50, 20, 50));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 3. PANEL TOMBOL (Aksi)
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelAksi.setBackground(Color.WHITE);
        panelAksi.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));

        JButton btnKembali = createStyledButton("Kembali", new Color(244, 67, 54));
        JButton btnHapus = createStyledButton("Hapus", new Color(255, 152, 0));
        JButton btnUpdate = createStyledButton("Update Data", new Color(33, 150, 243));
        JButton btnTambah = createStyledButton("Tambah UMKM", new Color(46, 125, 50));

        panelAksi.add(btnKembali);
        panelAksi.add(btnHapus);
        panelAksi.add(btnUpdate);
        panelAksi.add(btnTambah);
        add(panelAksi, BorderLayout.SOUTH);

        // --- LOGIKA TOMBOL HAPUS ---
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = showCustomConfirm("yakin ingin menghapus data?");
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusDataBaris(row);
                    loadDataFromFile();
                    showCustomMessage("Data berhasil dihapus!");
                }
            } else {
                showCustomMessage("Pilih data yang ingin dihapus!");
            }
        });

        // --- LOGIKA TOMBOL UPDATE (MENGGUNAKAN DIALOG CUSTOM) ---
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // Ambil data lama dari tabel
                String namaLama = model.getValueAt(row, 0).toString();
                String pendapatanLama = model.getValueAt(row, 1).toString();
                String trenLama = model.getValueAt(row, 2).toString();

                // Buka Dialog Update dengan desain seperti Form Input
                UpdateDialog dialog = new UpdateDialog(this, namaLama, pendapatanLama, trenLama);
                dialog.setVisible(true);

                // Jika user menekan Simpan dan validasi lolos
                if (dialog.isUpdated()) {
                    updateDataDiFile(row, dialog.getDataBaru());
                    loadDataFromFile();
                    showCustomMessage("Data berhasil diperbarui!");
                }
            } else {
                showCustomMessage("Pilih data yang ingin diupdate!");
            }
        });

        loadDataFromFile();

        btnKembali.addActionListener(e -> {
            new EkonomiHome().setVisible(true);
            this.dispose();
        });

        btnTambah.addActionListener(e -> {
            new FormInputUMKM(this, namaDaerah).setVisible(true);
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == 0 && e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String nama = model.getValueAt(row, 0).toString();
                        String pendapatan = model.getValueAt(row, 1).toString();
                        new HalamanDetailUMKM(nama, pendapatan).setVisible(true);
                    }
                }
            }
        });
    }

    // --- FUNGSI HELPER TOMBOL ---
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- METODE HELPER (LOGIKA FILE & UI) ---
    public void loadDataFromFile() {
        model.setRowCount(0);
        String fileName = namaDaerah + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
        } catch (IOException e) {
            // File mungkin belum ada
        }
    }

    private void hapusDataBaris(int index) {
        prosesFile(index, null);
    }

    private void updateDataDiFile(int index, String dataBaru) {
        prosesFile(index, dataBaru);
    }

    private void prosesFile(int targetIndex, String dataBaru) {
        File fileAsli = new File(namaDaerah + ".txt");
        File fileTemp = new File("temp.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(fileAsli));
             BufferedWriter bw = new BufferedWriter(new FileWriter(fileTemp))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == targetIndex) {
                    if (dataBaru != null) {
                        bw.write(dataBaru);
                        bw.newLine();
                    }
                } else {
                    bw.write(line);
                    bw.newLine();
                }
                i++;
            }
        } catch (IOException e) { e.printStackTrace(); }
        fileAsli.delete();
        fileTemp.renameTo(fileAsli);
    }

    private int showCustomConfirm(String pesan) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Button.background", new Color(46, 125, 50));
        UIManager.put("Button.foreground", Color.WHITE);
        return JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void showCustomMessage(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
}

// --- CLASS DIALOG UPDATE (DESAIN MIRIP TAMBAH DATA) ---
class UpdateDialog extends JDialog {
    private JTextField fNama, fPendapatan, fTren;
    private boolean isUpdated = false;

    public UpdateDialog(Frame owner, String n, String p, String t) {
        super(owner, "Update Data UMKM", true);
        setSize(500, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(46, 125, 50));
        header.setPreferredSize(new Dimension(500, 60));
        JLabel title = new JLabel("UPDATE DATA UMKM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(null);
        body.setBackground(Color.WHITE);

        JLabel lNama = new JLabel("Nama UMKM:");
        lNama.setBounds(50, 40, 150, 30);
        lNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fNama = new JTextField(n);
        fNama.setBounds(210, 40, 220, 40);

        JLabel lPendapatan = new JLabel("Pendapatan (Rp):");
        lPendapatan.setBounds(50, 120, 150, 30);
        lPendapatan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fPendapatan = new JTextField(p);
        fPendapatan.setBounds(210, 120, 220, 40);

        JLabel lTren = new JLabel("Tren Kenaikan (%):");
        lTren.setBounds(50, 200, 150, 30);
        lTren.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fTren = new JTextField(t);
        fTren.setBounds(210, 200, 220, 40);

        body.add(lNama); body.add(fNama);
        body.add(lPendapatan); body.add(fPendapatan);
        body.add(lTren); body.add(fTren);
        add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footer.setBackground(Color.WHITE);

        JButton btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(100, 40));

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setPreferredSize(new Dimension(100, 40));
        btnSimpan.setBackground(new Color(46, 125, 50));
        btnSimpan.setForeground(Color.WHITE);

        btnSimpan.addActionListener(e -> {
            // VALIDASI: Pendapatan harus angka
            if (!fPendapatan.getText().matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Pendapatan harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                isUpdated = true;
                dispose();
            }
        });

        btnBatal.addActionListener(e -> dispose());

        footer.add(btnBatal);
        footer.add(btnSimpan);
        add(footer, BorderLayout.SOUTH);
    }

    public boolean isUpdated() { return isUpdated; }
    public String getDataBaru() { return fNama.getText() + "," + fPendapatan.getText() + "," + fTren.getText(); }
}