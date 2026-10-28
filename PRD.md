# PRD — Sistem Pemesanan Tiket Bioskop

**Mata kuliah:** Pengujian dan Pemeliharaan Perangkat Lunak (PPPL)  
**Jenis proyek:** UTS Kelompok — eksplorasi unit testing  
**Teknologi:** Java 17, Maven, JUnit 5  
**Tingkat kompleksitas:** Sederhana  
**Status dokumen:** Dasar kebutuhan proyek

## 1. Ringkasan Produk

Sistem Pemesanan Tiket Bioskop adalah aplikasi Java berbasis console yang memodelkan proses dasar pemesanan tiket. Aplikasi dibuat sebagai proyek latihan Object-Oriented Programming (OOP) dan menjadi objek pengujian unit menggunakan JUnit 5.

Tujuan utama proyek ini bukan membuat aplikasi bioskop produksi, melainkan menyediakan logika bisnis yang cukup jelas untuk didemonstrasikan melalui unit test, Control Flow Graph (CFG), Cyclomatic Complexity, Boundary Value Testing, dan Decision Table Testing.

## 2. Tujuan

1. Memiliki source code Java yang sederhana, dapat dikompilasi, dan memiliki pemisahan class yang masuk akal.
2. Menunjukkan penggunaan Maven untuk mengelola build dan JUnit 5 untuk unit testing.
3. Menguji kondisi normal, batas input, serta kondisi gagal.
4. Menyediakan bahan analisis pengujian yang bisa dijelaskan anggota kelompok saat presentasi.
5. Menyediakan panduan instalasi, menjalankan aplikasi, dan menjalankan test.

## 3. Pengguna dan Peran

Proyek hanya memiliki satu pengguna utama: **operator/pengguna console** yang menjalankan program untuk mencoba simulasi pemesanan. Login, database, antarmuka web, pembayaran sungguhan, dan integrasi layanan eksternal tidak termasuk ruang lingkup.

## 4. Ruang Lingkup Fitur

### 4.1 Fitur dalam ruang lingkup

- Menampilkan pilihan film dan harga tiket contoh.
- Memilih film dari daftar sederhana.
- Memilih jumlah tiket.
- Menghitung subtotal.
- Menghitung diskon berdasarkan aturan yang terdokumentasi.
- Menghitung total pembayaran.
- Memvalidasi jumlah tiket dan pilihan film.
- Memodelkan pemilihan kursi sederhana dan mencegah kursi yang sama dipesan dua kali, bila implementasi seat booking digunakan.
- Menampilkan ringkasan pemesanan di console.
- Menyediakan unit test JUnit 5 untuk kondisi berhasil dan gagal.
- Menyediakan analisis CFG dan Cyclomatic Complexity untuk satu method yang memiliki alur kontrol yang mudah dipetakan.
- Menyediakan Boundary Value Testing untuk tiga method.
- Menyediakan Decision Table Testing untuk tiga method.

### 4.2 Di luar ruang lingkup

- Database atau penyimpanan pemesanan permanen.
- Login dan manajemen akun.
- Payment gateway, QRIS sungguhan, refund, dan transaksi uang sungguhan.
- Integrasi API bioskop atau data film real-time.
- Aplikasi web/mobile, GUI, atau deployment cloud.
- Konkurensi pemesanan multi-pengguna dan sistem distribusi kursi nyata.

## 5. Aturan Bisnis

Aturan berikut harus diterapkan secara konsisten dan dicatat pada README serta test:

1. Harga tiket harus lebih besar dari Rp0.
2. Jumlah tiket harus bilangan bulat positif.
3. Pilihan film harus merujuk pada film yang tersedia.
4. Diskon tidak boleh negatif atau melebihi 100%.
5. Jika diskon diberikan, total dihitung dengan rumus `subtotal - (subtotal * diskonPersen / 100)`.
6. Total pembayaran tidak boleh bernilai negatif.
7. Jika pemesanan kursi diimplementasikan, kursi yang sudah dipesan tidak dapat dipesan lagi.
8. Untuk nilai input yang tidak valid, method harus memberikan respons yang konsisten—disarankan `IllegalArgumentException` atau hasil validasi eksplisit. Pilih satu pendekatan per method dan uji perilakunya.
9. Gunakan nilai rupiah bertipe integer/long untuk menghindari kompleksitas pembulatan desimal yang tidak diperlukan pada proyek sederhana.

**Catatan desain:** Tim boleh memakai model film/tiket yang lebih sederhana atau class `Seat`/`CinemaBooking`, tetapi nama class dan method final harus konsisten di source code, test, CFG, BVA, dan decision table. Hindari membuat aturan yang berbeda antara dokumen dan implementasi.

## 6. Usulan Struktur Class

Struktur minimal yang disarankan:

- `Movie`: menyimpan ID/nama film dan harga tiket serta memvalidasi datanya.
- `BookingService` atau `CinemaBooking`: berisi logika pemesanan dan perhitungan.
- `Seat` (opsional): mewakili kursi dan status pemesanan.
- `Main`: entry point console.
- Test class yang sesuai untuk masing-masing class atau kelompok method.

Jangan menambahkan class hanya demi terlihat kompleks. Pilih abstraksi paling sederhana yang dapat menjelaskan logika bisnis dengan baik.

## 7. Target Tiga Method untuk Analisis Testing

Nama dan tanda tangan method akhir boleh disesuaikan, tetapi pilih **tiga method inti yang benar-benar ada di source code**. Rekomendasi:

1. `calculateSubtotal(int ticketPrice, int quantity)`
   - Menghitung harga sebelum diskon.
   - Batas penting: harga minimum valid, jumlah minimum valid, serta nilai tidak valid tepat di bawah batas.
2. `calculateDiscount(int subtotal, int discountPercent)` atau `calculateTotal(...)`
   - Menghitung diskon/total berdasarkan persentase.
   - Batas penting: diskon 0%, 1%, 99%, dan 100%; subtotal minimum valid.
3. `validateBooking(...)` atau `bookSeat(...)`
   - Memeriksa data pemesanan atau status kursi.
   - Kondisi penting: input valid/tidak valid, kursi tersedia/sudah terpesan, atau film tersedia/tidak tersedia.

Pilih nama method berdasarkan desain final dan gunakan nama yang sama di seluruh artefak. Jika method menggabungkan banyak aturan, sederhanakan agar analisis tidak sulit dipresentasikan.

## 8. Kebutuhan Unit Testing

Gunakan JUnit Jupiter (JUnit 5). Setiap test harus:

- Menggunakan anotasi `@Test` (serta anotasi JUnit lain hanya bila diperlukan).
- Memakai assertion yang relevan seperti `assertEquals`, `assertTrue`, `assertFalse`, `assertThrows`, atau `assertDoesNotThrow`.
- Menguji hasil benar untuk jalur normal.
- Menguji kondisi batas.
- Menguji input tidak valid.
- Memiliki nama yang menjelaskan kondisi dan hasil yang diharapkan.
- Tidak bergantung pada urutan eksekusi test atau data di komputer tertentu.

Test harus dapat dijalankan dengan perintah `mvn test`. Jangan membuat test yang hanya mencetak hasil tanpa assertion.

## 9. Butir Teori JUnit yang Harus Dijawab

Laporan/presentasi kelompok perlu menjawab:

1. Apa yang terjadi jika method test JUnit menggunakan return type `String`? Jelaskan bahwa test method JUnit Jupiter umumnya harus mengembalikan `void`; return type selain `void` tidak memenuhi kontrak test biasa dan dapat membuat test engine menolak method tersebut.
2. Mengapa anotasi `@Test` penting? Jelaskan bahwa anotasi ini menandai method sebagai test yang harus ditemukan dan dieksekusi oleh JUnit.
3. Bagaimana menguji method `protected`? Jelaskan opsi yang wajar: menguji perilaku melalui public API terlebih dahulu; jika perlu menguji langsung, gunakan test class pada package yang sama (akses package-protected tetap bukan berarti akses `protected` lintas package untuk semua konteks) atau subclass test sesuai aturan akses Java. Hindari reflection kecuali ada alasan khusus.

## 10. Analisis Wajib dalam Laporan

### 10.1 Control Flow Graph (CFG)

- Pilih satu method yang memiliki percabangan.
- Sertakan potongan source code final.
- Gambar node, edge, titik masuk, titik keluar, dan percabangan.
- Beri label setiap node/edge agar mudah dirujuk.
- CFG harus sesuai persis dengan source code.

### 10.2 Cyclomatic Complexity (CC)

Hitung kompleksitas dari CFG dengan rumus:

`V(G) = E - N + 2P`

dengan:
- `E` = jumlah edge,
- `N` = jumlah node,
- `P` = jumlah komponen terhubung; untuk satu method/flow graph biasanya `P = 1`.

Untuk satu CFG terhubung, rumus yang ekuivalen adalah `V(G) = jumlah predicate/keputusan + 1`. Jelaskan metode hitung yang digunakan dan arti hasilnya. Jangan menyatakan suatu angka tanpa menunjukkan hitungan.

### 10.3 Boundary Value Testing (BVT/BVA) — tiga method

Untuk setiap dari tiga method target:
- Cantumkan parameter dan rentang valid.
- Identifikasi batas minimum/maksimum yang relevan.
- Pilih nilai di bawah batas, tepat di batas, dan di atas batas bila masuk akal.
- Tuliskan input, expected result/exception, dan alasan.
- Pastikan setiap test case dipetakan ke test JUnit yang benar.

### 10.4 Decision Table — tiga method

Untuk setiap dari tiga method target:
- Identifikasi kondisi/aturan yang menentukan hasil.
- Buat tabel kombinasi kondisi dan expected action/result.
- Jangan membuat kombinasi yang tidak relevan secara bisnis; tandai kondisi sebagai “tidak relevan” bila keputusan sebelumnya sudah menentukan hasil.
- Pastikan setiap kolom/rule memiliki test case JUnit atau jelaskan mengapa tidak diuji.

## 11. Build dan Pengembangan

- Java 17.
- Maven untuk dependency dan build.
- JUnit Jupiter 5 untuk test.
- Struktur standar Maven: `src/main/java` dan `src/test/java`.
- Nama package harus konsisten; gunakan package contoh `id.ac.upi.pppl`.
- Dokumentasi menjalankan program minimal mencakup `java -version`, `mvn -version`, `mvn test`, dan cara menjalankan `Main`.
- Tidak boleh bergantung pada API key, koneksi internet runtime, atau layanan berbayar.

## 12. Kriteria Penerimaan

Proyek dianggap memenuhi sasaran jika:

- [ ] Source code Java lengkap dengan entry point yang dapat dijalankan.
- [ ] `pom.xml` valid dan menggunakan JUnit 5.
- [ ] Struktur folder mengikuti standar Maven.
- [ ] `mvn test` berjalan dan seluruh test lulus.
- [ ] Test memiliki assertion dan mencakup jalur normal, batas, serta input tidak valid.
- [ ] Panduan setup dan eksekusi tersedia di README.
- [ ] Ada jawaban tertulis untuk tiga pertanyaan teori JUnit.
- [ ] CFG disediakan untuk method nyata dalam kode.
- [ ] CC dihitung dari CFG dan hitungannya diperlihatkan.
- [ ] BVA mencakup tiga method yang benar-benar ada.
- [ ] Decision table mencakup tiga method yang benar-benar ada.
- [ ] Nama method, aturan bisnis, expected result, diagram, dan test konsisten.
- [ ] Tidak ada fitur besar di luar kebutuhan UTS yang membuat proyek sulit dijelaskan.

## 13. Artefak yang Dikumpulkan

Sesuaikan nama akhir dengan format dosen:

- Source code proyek Maven lengkap.
- `README.md` berisi prasyarat, setup, cara menjalankan aplikasi, cara menjalankan test.
- Unit test JUnit 5.
- Laporan PDF kelompok (`UTS_PPPL_KelompokXX.pdf`) dengan identitas anggota, metodologi, hasil test, CFG, CC, BVA, decision table, jawaban teori JUnit, kesimpulan.
- Presentasi (`UTS_PPPL_KelompokXX.ppt` atau format yang diminta dosen) yang menjelaskan sistem dan bukti pengujiannya.

Sebelum pengumpulan, konfirmasikan kembali format/nama file kepada instruksi resmi dosen.
