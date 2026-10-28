# Sistem Pemesanan Tiket Bioskop

Proyek UTS mata kuliah **Pengujian dan Pemeliharaan Perangkat Lunak (PPPL)**.
Aplikasi console sederhana untuk memodelkan pemesanan tiket bioskop, dibangun sebagai objek
pengujian unit dengan **JUnit 5** serta bahan analisis **CFG, Cyclomatic Complexity,
Boundary Value Analysis, dan Decision Table**.

Fokus proyek: logika bisnis yang mudah dijelaskan dan bukti pengujian yang dapat diverifikasi —
bukan banyaknya fitur.

## 1. Prasyarat

- **JDK 17 atau lebih baru** (proyek dikompilasi dengan target `release 17`; verifikasi di lingkungan
  pengembangan memakai JDK 25).
- **Apache Maven 3.8 atau lebih baru**.
- Tidak perlu koneksi internet setelah dependency Maven tersedia di cache lokal.
- Tidak ada database, API eksternal, API key, payment gateway, atau framework web.

Cek versi:

```bash
java -version
mvn -version
```

## 2. Struktur folder

```
UTS/
├── AGENTS.ms                          # aturan kerja agent
├── IMPLEMENTATION_PLAN.md             # rencana implementasi
├── PRD.md                             # dokumen kebutuhan
├── README.md                          # file ini
├── LAPORAN_ANALISIS_PENGUJIAN.md      # CFG, CC, BVA, decision table, teori JUnit
├── pom.xml
└── src/
    ├── main/java/id/ac/upi/pppl/
    │   ├── BookingService.java        # logika bisnis (3 method target analisis)
    │   ├── Main.java                  # entry point console (hanya I/O)
    │   ├── Movie.java                 # domain film
    │   └── MovieCatalog.java          # data contoh statis + pencarian
    └── test/java/id/ac/upi/pppl/
        ├── BookingServiceTest.java
        ├── CalculateDiscountTest.java
        ├── CalculateSubtotalTest.java
        ├── CalculateTotalTest.java
        ├── GetDiscountPercentTest.java
        ├── MovieCatalogTest.java
        ├── MovieTest.java
        └── ValidateBookingTest.java
```

## 3. Aturan bisnis

1. Harga tiket harus lebih besar dari Rp0 (divalidasi di constructor `Movie`).
2. Jumlah tiket per pemesanan valid: **1 sampai 10** (`BookingService.MIN_TICKETS`..`MAX_TICKETS`).
3. Pilihan film harus merujuk film yang ada di katalog (`M01`–`M04`); selain itu dianggap tidak tersedia.
4. Diskon tidak boleh negatif atau melebihi 100%.
5. Nominal diskon = `subtotal * discountPercent / 100` (pembulatan integer ke bawah);
   total bayar = `subtotal - diskon`.
6. Total pembayaran tidak pernah negatif karena subtotal minimal 1 dan diskon maksimal 100%.
7. Aturan diskon contoh: **10% untuk pembelian minimal 5 tiket**, selain itu 0%.
8. Input invalid menghasilkan **`IllegalArgumentException`** dengan pesan yang menjelaskan penyebabnya.
   Input console yang bukan bilangan bulat ditangani `NumberFormatException` dengan pesan jelas —
   program tidak crash tanpa penjelasan.
9. Semua nilai uang memakai tipe `int` (Rupiah bulat).

### Tiga method target analisis (dipakai konsisten di kode, test, dan laporan)

| Signature | Kontrak |
|---|---|
| `int calculateSubtotal(int ticketPrice, int quantity)` | `ticketPrice >= 1`, `quantity >= 1`, selain itu `IllegalArgumentException` |
| `int calculateDiscount(int subtotal, int discountPercent)` | `subtotal >= 1`, `0 <= discountPercent <= 100`, selain itu `IllegalArgumentException` |
| `Movie validateBooking(String movieId, int quantity)` | film ada di katalog dan `1 <= quantity <= 10`, selain itu `IllegalArgumentException` |

## 4. Setup dan menjalankan

### 4.1 Menjalankan unit test

```bash
mvn clean test
```

Hasil verifikasi pada proyek ini (Maven 3.9.16, JDK 25, `release 17`):

```
Tests run: 55, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 4.2 Menjalankan aplikasi console

Cara 1 — tanpa membuat JAR:

```bash
mvn -q compile
java -cp target/classes id.ac.upi.pppl.Main
```

Cara 2 — membuat JAR yang sudah diberi `Main-Class` (plugin `maven-jar-plugin` di `pom.xml`):

```bash
mvn -q package
java -jar target/bioskop-ticket-booking-1.0.0.jar
```

Contoh interaksi (hasil dijalankan sungguhan):

```
=== Sistem Pemesanan Tiket Bioskop ===
Daftar film:
  M01 | Interstellar | Rp50000
  M02 | Pengabdi Setan 2 | Rp45000
  M03 | Dilan 1990 | Rp40000
  M04 | Laskar Pelangi | Rp35000
Aturan: jumlah tiket 1 s.d. 10, diskon 10% untuk minimal 5 tiket.

Masukkan ID film: M01
Masukkan jumlah tiket: 5

=== Ringkasan Transaksi ===
Film        : Interstellar
Harga tiket : Rp50000
Jumlah      : 5 tiket
Subtotal    : Rp250000
Diskon (10%) : Rp25000
Total bayar : Rp225000
```

Contoh penanganan input invalid:

```
Masukkan ID film: M99
Masukkan jumlah tiket: 2
Pemesanan gagal: Film tidak tersedia: M99

Masukkan ID film: M01
Masukkan jumlah tiket: abc
Input jumlah tiket tidak valid: harus bilangan bulat.

Masukkan ID film: M02
Masukkan jumlah tiket: 11
Pemesanan gagal: Jumlah tiket maksimal 10
```

> Catatan lingkungan: perintah di atas memakai `mvn` yang sudah terpasang pada sistem.
> Jika Maven belum terpasang, pasang dulu (mis. lewat installer resmi Apache Maven atau package
> manager) lalu pastikan `mvn -version` berhasil.

## 5. Cara pengujian

| Kategori | Contoh pada proyek ini |
|---|---|
| Jalur normal / berhasil | `calculateSubtotal_normalPriceAndQuantity_returnsProduct` |
| Batas valid (min/max) | `calculateSubtotal_minValidPriceAndQuantity_returnsOne`, `validateBooking_maxQuantity_returnsMovie` |
| Tepat di luar batas | `calculateDiscount_percentAboveMaximum_throwsIllegalArgumentException`, `validateBooking_quantityAboveMaximum_throwsIllegalArgumentException` |
| Data tidak tersedia | `validateBooking_unknownMovieId_throwsIllegalArgumentException` |
| Cabang keputusan | `calculateSubtotal_invalidPriceAndQuantity_reportsPriceErrorFirst` (urutan pemeriksaan) |

Semua test memakai assertion (`assertEquals`, `assertThrows`, `assertTrue`/`assertFalse`),
memiliki nama deskriptif `method_condition_expectedResult`, dan tidak bergantung pada urutan eksekusi.

Analisis lengkap (CFG, Cyclomatic Complexity, BVA tiga method, Decision Table tiga method,
jawaban teori JUnit) ada di **`LAPORAN_ANALISIS_PENGUJIAN.md`**.

## 6. Asumsi desain

- Jumlah tiket dibatasi **1–10** per pemesanan agar batas atas valid dapat diuji (BVA).
- Diskon contoh **10% untuk minimal 5 tiket** — aturan statis sederhana, bukan fitur promo nyata.
- Pemesanan kursi (seat) **tidak** diimplementasikan; PRD menyebutnya opsional. Kegagalan pemesanan
  diwakili oleh "film tidak tersedia" dan "jumlah di luar batas".
- `validateBooking` mengembalikan objek `Movie` yang lolos validasi agar `Main` tidak perlu
  mencari film dua kali.

## 7. Keterbatasan

- Tanpa database: daftar film statis, satu sesi hanya memproses satu transaksi, tidak ada penyimpanan riwayat.
- Tanpa login, API, GUI, payment gateway, dan refund.
- Perhitungan memakai `int`: angka yang sangat besar dapat melampaui rentang `int`
  (overflow) — di luar skenario proyek ini.
- Diskon memakai pembulatan integer ke bawah, jadi nominal diskon bisa kehilangan pecahan Rupiah.
- `Main` diuji secara manual lewat interaksi console; unit test berfokus pada logika bisnis
  di `BookingService`, `Movie`, dan `MovieCatalog`.
- Tidak ada pengujian konkurensi.
