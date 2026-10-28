# Implementation Plan — Sistem Pemesanan Tiket Bioskop

## Prinsip pelaksanaan

Bangun proyek dalam tahap kecil. Setelah setiap tahap, tinjau source code dan hasilnya sebelum berpindah ke tahap berikutnya. Prioritas: benar, mudah dipahami, bisa diuji, dan sesuai rubrik UTS.

## Tahap 1 — Tetapkan aturan dan tiga method analisis

**Pekerjaan**
- Sepakati alur: pilih film → masukkan jumlah tiket → hitung subtotal → hitung diskon/total → tampilkan ringkasan.
- Tentukan aturan harga, jumlah minimal tiket, batas diskon, serta perilaku input invalid.
- Tetapkan tiga method yang akan digunakan pada BVA dan Decision Table.

**Rekomendasi awal**
1. `calculateSubtotal(int ticketPrice, int quantity)`
2. `calculateDiscount(int subtotal, int discountPercent)` atau satu method final `calculateTotal(...)`
3. `validateBooking(...)` atau `bookSeat(...)`

Pilih tepat tiga method yang paling mudah dijelaskan dan konsisten gunakan nama yang sama dalam kode, test, tabel, dan laporan.

**Kriteria selesai**
- Aturan bisnis ditulis jelas.
- Tidak ada dua aturan yang saling bertentangan.
- Semua anggota kelompok memahami tiga method target.

## Tahap 2 — Buat kerangka Maven

**File/folder**
- `pom.xml`
- `src/main/java/id/ac/upi/pppl/`
- `src/test/java/id/ac/upi/pppl/`
- `README.md`

**Pekerjaan**
- Konfigurasikan Java 17.
- Tambahkan JUnit Jupiter versi stabil dengan scope `test`.
- Pastikan Maven Surefire menjalankan JUnit 5.

**Verifikasi**
```bash
java -version
mvn -version
mvn test
```
Pada kerangka kosong, test belum tentu tersedia; setelah test dibuat, pastikan Maven mendeteksinya.

## Tahap 3 — Implementasikan domain dan logika bisnis

**Pekerjaan**
- Buat class domain yang bertanggung jawab menyimpan data film/tiket.
- Buat service untuk validasi, subtotal, diskon/total, dan pemesanan.
- Buat `Main` sebagai console entry point.
- Pisahkan input/output console dari logika yang perlu di-unit-test.

**Verifikasi manual**
- Jalankan skenario pemesanan normal.
- Coba jumlah tiket tidak valid.
- Coba film tidak tersedia.
- Coba nilai diskon di batas minimum dan maksimum.
- Pastikan output dapat dipahami.

## Tahap 4 — Buat unit test JUnit 5

**Minimum kategori test**
- Jalur normal: data valid menghasilkan subtotal/total yang diharapkan.
- Input batas: nilai minimum/maksimum yang sah.
- Input invalid: nilai di luar domain atau item tidak tersedia.
- Keputusan: masing-masing kondisi dalam aturan bisnis utama.

**Teknik**
- `assertEquals` untuk hasil hitung.
- `assertThrows` untuk exception invalid.
- `assertTrue` / `assertFalse` untuk hasil boolean.
- `assertDoesNotThrow` bila memang membantu menjelaskan kontrak.

**Verifikasi**
```bash
mvn clean test
```
Catat jumlah test yang ditemukan, jumlah yang lulus, dan bukti hasil dari output Maven. Jangan menulis angka yang belum diverifikasi.

## Tahap 5 — Analisis Control Flow Graph dan Cyclomatic Complexity

**Pekerjaan**
- Pilih satu method dengan percabangan yang jelas.
- Salin versi final method ke dokumen laporan atau lampiran.
- Pecah alur menjadi node/edge dan gambar CFG.
- Hitung CC: `V(G) = E - N + 2P`, dengan `P = 1` untuk graph tunggal.
- Lakukan pemeriksaan silang dengan `jumlah predicate + 1`, jika sesuai.

**Output**
- Source method.
- Diagram CFG yang diberi label.
- Daftar node dan edge.
- Perhitungan N, E, P, dan V(G).
- Penjelasan singkat apakah struktur mudah dipahami dan alasannya.

**Peringatan**
Jika kode berubah, tinjau ulang CFG dan CC. Jangan menggunakan perhitungan dari implementasi sebelumnya.

## Tahap 6 — Boundary Value Analysis untuk tiga method

Untuk setiap method:
1. Tuliskan parameter dan domain input.
2. Tentukan batas valid.
3. Pilih nilai di bawah batas, tepat pada batas, dan di atas batas.
4. Tuliskan hasil yang diharapkan atau exception.
5. Kaitkan setiap kasus ke nama test JUnit.

**Tabel yang disiapkan**
| ID | Method | Parameter | Input | Expected result | Alasan batas | Nama test |
|---|---|---|---|---|---|---|

Pastikan untuk nilai yang valid dan invalid, perilaku yang ditulis sesuai kontrak source code.

## Tahap 7 — Decision Table untuk tiga method

Untuk setiap method:
- Daftar kondisi yang memengaruhi hasil.
- Buat rule/kolom untuk kombinasi relevan.
- Isi expected result/action untuk tiap rule.
- Tandai kondisi “tidak relevan” jika aturan sebelumnya membuatnya tidak perlu dipertimbangkan.
- Hubungkan setiap rule penting ke test JUnit.

**Template**
| Kondisi/Aturan | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| Kondisi A | Ya | Ya | Tidak | Tidak |
| Kondisi B | Ya | Tidak | Ya | Tidak |
| Expected action | ... | ... | ... | ... |
| Test terkait | ... | ... | ... | ... |

Jangan memasukkan kondisi atau keputusan yang tidak ada dalam source code final.

## Tahap 8 — Jawaban teori JUnit

Siapkan penjelasan singkat untuk presentasi:
1. Return type `String` pada test method JUnit Jupiter: method test biasa umumnya harus `void`; return type `String` tidak sesuai kontrak dan dapat ditolak oleh engine.
2. `@Test`: menandai method sebagai test yang dapat ditemukan/dijalankan JUnit.
3. Method `protected`: pilihan yang baik biasanya menguji perilaku melalui public API; akses langsung dapat diatur melalui package/subclass sesuai aturan Java. Jelaskan batas aksesnya, jangan menyederhanakan secara keliru.

## Tahap 9 — Dokumentasi dan simulasi presentasi

**README harus mencakup**
- Deskripsi aplikasi dan fitur.
- Prasyarat Java/Maven.
- Struktur folder.
- Cara menjalankan `mvn clean test`.
- Cara menjalankan class `Main` (sesuaikan dengan struktur final).
- Aturan bisnis dan keterbatasan.

**Simulasi presentasi**
- Demonstrasikan satu pemesanan normal.
- Demonstrasikan unit test dan hasil Maven.
- Jelaskan satu CFG dan hitungan CC.
- Tunjukkan contoh BVA dan decision table.
- Jawab tiga pertanyaan teori JUnit.

## Tahap 10 — Pemeriksaan akhir dan paket pengumpulan

Checklist:
- [ ] Source code final dan struktur folder rapi.
- [ ] Semua test relevan lulus.
- [ ] Output Maven sesuai dengan laporan.
- [ ] CFG dan CC menggambarkan source code final.
- [ ] BVA dan decision table membahas tiga method yang sama.
- [ ] Tidak ada data atau hasil uji yang dibuat-buat.
- [ ] README dapat diikuti anggota kelompok lain.
- [ ] PDF dan bahan presentasi menggunakan identitas kelompok yang benar.
- [ ] Nama file mengikuti instruksi resmi dosen.
