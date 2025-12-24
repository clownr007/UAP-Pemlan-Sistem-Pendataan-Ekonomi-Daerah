# 📊 Sistem Monitoring Ekonomi Daerah (Java Swing)

## 📌 Deskripsi Aplikasi
Sistem Monitoring Ekonomi Daerah merupakan aplikasi desktop berbasis **Java Swing** yang digunakan untuk memantau dan mengelola data **UMKM** pada setiap daerah/kecamatan.  
Aplikasi ini menyediakan fitur pengelolaan data UMKM, perhitungan pendapatan, serta proyeksi ekonomi berdasarkan data yang tersimpan dalam file teks.

Aplikasi dirancang dengan konsep **Object Oriented Programming (OOP)** dan penyimpanan data menggunakan **file (.txt)**.

---

## 🎯 Tujuan Aplikasi
- Menyediakan dashboard ekonomi daerah
- Mengelola data UMKM per kecamatan
- Menghitung total pendapatan dan tren ekonomi
- Menampilkan proyeksi pendapatan UMKM
- Melatih konsep GUI Java, File I/O, dan Event Handling

---

## 🧩 Struktur Kelas

| Nama Kelas | Fungsi |
|-----------|-------|
| `EkonomiHome` | Dashboard utama ekonomi daerah |
| `HalamanUMKM` | Daftar UMKM per daerah |
| `FormInputUMKM` | Form input UMKM |
| `UpdateDialog` | Dialog update data UMKM |
| `HalamanDetailUMKM` | Detail & proyeksi pendapatan UMKM |

---

## 🔁 Alur Cara Kerja Aplikasi

### 1️⃣ Dashboard Ekonomi (`EkonomiHome`)
- Menampilkan daftar daerah/kecamatan
- Menghitung:
    - Total pendapatan UMKM
    - Rata-rata tren kenaikan ekonomi
- Data diambil dari file `.txt` masing-masing daerah

**Interaksi:**
- Double klik pada kolom **Nama Daerah** → membuka halaman UMKM

---

### 2️⃣ Halaman Daftar UMKM (`HalamanUMKM`)
- Menampilkan seluruh UMKM pada daerah yang dipilih
- Data dibaca dari file:


**Fitur:**
- Tambah data UMKM
- Update data UMKM
- Hapus data UMKM
- Kembali ke Dashboard
- Double klik nama UMKM → melihat detail pendapatan

---

### 3️⃣ Input Data UMKM (`FormInputUMKM`)
- User mengisi:
- Nama UMKM
- Pendapatan
- Tren kenaikan (%)
- Sistem melakukan validasi input
- Data disimpan ke file daerah
- Tabel UMKM otomatis diperbarui
- Popup konfirmasi ditampilkan setelah data tersimpan

---

### 4️⃣ Update Data UMKM (`UpdateDialog`)
- Menampilkan data lama UMKM
- User dapat mengubah data
- Validasi angka dilakukan
- File diperbarui menggunakan file sementara (`temp.txt`)

---

### 5️⃣ Hapus Data UMKM
- User memilih data UMKM
- Konfirmasi penghapusan ditampilkan
- Data dihapus dari file
- Tabel diperbarui secara otomatis

---

### 6️⃣ Detail UMKM (`HalamanDetailUMKM`)
- Menampilkan proyeksi pendapatan:
- 1 Hari
- 1 Minggu
- 1 Bulan
- 1 Tahun
- Pendapatan dihitung secara otomatis
- Tampilan menggunakan konsep **Card Layout**

---

## 💾 Format Penyimpanan Data
Setiap daerah memiliki satu file `.txt`

**Contoh isi file:**

Warung Maju,1500000,5%

Toko Sejahtera,2500000,7%


---

## 🛠️ Teknologi yang Digunakan
- Bahasa Pemrograman: **Java**
- GUI: **Java Swing**
- Penyimpanan Data: **File Teks (.txt)**
- Konsep:
    - OOP
    - File I/O
    - Exception Handling
    - Event Handling
    - GUI Desktop

---

## ✅ Kesimpulan
Aplikasi Sistem Monitoring Ekonomi Daerah mampu:
- Mengelola data UMKM secara dinamis
- Menampilkan analisis ekonomi daerah
- Memberikan proyeksi pendapatan UMKM
- Menjadi solusi sederhana untuk monitoring ekonomi berbasis desktop

Aplikasi ini cocok digunakan sebagai **tugas praktikum**, **proyek akhir**, maupun **latihan pengembangan aplikasi Java Swing**.

---

## 👤 Pengembang 1
Nama: Ronald Aditya Prasindu Rahmadhan

NIM: 202410370110150

Kelas: 3-D

Program Studi: Informatika

## 👤 Pengembang 2
Nama: M. Yoga Adra Assajad

NIM: 202410370110150

Kelas: 3-D

Program Studi: Informatika

