package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

public class HalamanUMKM extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String namaDaerah;

    public HalamanUMKM(String daerah) {
        this.namaDaerah = daerah;
        setTitle("Data UMKM - " + namaDaerah);
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // 1. HEADER
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
        table.setRowHeight(40); // Tinggi baris agar lebih lega
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- PERBAIKAN BORDER TEBAL & GAYA TABEL ---
        table.setCellSelectionEnabled(false);      // Diubah ke false agar tidak muncul border sel individu
        table.setRowSelectionAllowed(true);       // Agar satu baris utuh terseleksi
        table.setFocusable(false);                // Menghilangkan garis fokus (garis tebal saat diklik)
        table.setShowGrid(true);                  // Menampilkan garis kisi
        table.setGridColor(new Color(230, 230, 230)); // Warna garis kisi yang halus
        table.setIntercellSpacing(new Dimension(0, 1)); // Menghilangkan double border vertikal

        // Pengaturan agar teks di kolom 1 (Pendapatan) dan 2 (Tren) berada di tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        // --------------------------------------------

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new javax.swing.border.EmptyBorder(20, 50, 20, 50));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 3. PANEL TOMBOL
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

        // LOGIKA TOMBOL
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = showCustomConfirm("Yakin ingin menghapus data?");
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusDataBaris(row);
                    loadDataFromFile();
                    showCustomMessage("Data berhasil dihapus!");
                }
            } else {
                showCustomMessage("Pilih data yang ingin dihapus!");
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String namaLama = model.getValueAt(row, 0).toString();
                String pendapatanLama = model.getValueAt(row, 1).toString().replace(".", "");
                String trenLama = model.getValueAt(row, 2).toString();

                UpdateDialog dialog = new UpdateDialog(this, namaLama, pendapatanLama, trenLama);
                dialog.setVisible(true);

                if (dialog.isUpdated()) {
                    updateDataDiFile(row, dialog.getDataBaru());
                    loadDataFromFile();
                    showCustomMessage("Data berhasil diperbarui!");
                }
            } else {
                showCustomMessage("Pilih data yang ingin diupdate!");
            }
        });

        btnTambah.addActionListener(e -> new FormInputUMKM(this, namaDaerah).setVisible(true));

        btnKembali.addActionListener(e -> {
            new EkonomiHome().setVisible(true);
            this.dispose();
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == 0 && e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String nama = model.getValueAt(row, 0).toString();
                        String pendapatan = model.getValueAt(row, 1).toString().replace(".", "");
                        new HalamanDetailUMKM(nama, pendapatan).setVisible(true);
                    }
                }
            }
        });

        loadDataFromFile();
    }

    private String formatRupiah(String nominal) {
        try {
            double value = Double.parseDouble(nominal.replace(".", ""));
            NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
            return nf.format(value);
        } catch (Exception e) { return nominal; }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    public void loadDataFromFile() {
        model.setRowCount(0);
        File file = new File(namaDaerah + ".txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    data[1] = formatRupiah(data[1]);
                }
                model.addRow(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void hapusDataBaris(int index) { prosesFile(index, null); }
    private void updateDataDiFile(int index, String dataBaru) { prosesFile(index, dataBaru); }

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
        return JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void showCustomMessage(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
}

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
        btnBatal.setBackground(new Color(244, 67, 54)); // MERAH
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setPreferredSize(new Dimension(100, 40));
        btnSimpan.setBackground(new Color(46, 125, 50)); // HIJAU
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);

        btnSimpan.addActionListener(e -> {
            if (!fPendapatan.getText().replace(".", "").matches("\\d+")) {
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
    public String getDataBaru() {
        return fNama.getText() + "," + fPendapatan.getText().replace(".", "") + "," + fTren.getText();
    }
}