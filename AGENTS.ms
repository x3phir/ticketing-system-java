# AGENTS — Aturan Kerja AI Coding Agent

> Dokumen ini sengaja disediakan dengan nama `AGENTS.ms` sesuai permintaan. Jika tool coding yang digunakan hanya membaca `AGENTS.md`, salin isi dokumen ini ke `AGENTS.md`.

## Misi

Bangun proyek Java sederhana bertema Sistem Pemesanan Tiket Bioskop untuk UTS kelompok mata kuliah Pengujian dan Pemeliharaan Perangkat Lunak (PPPL). Fokus utama adalah logika yang mudah dijelaskan dan bukti pengujian yang dapat diverifikasi, bukan banyaknya fitur.

## Stack dan batasan

- Java 17.
- Maven.
- JUnit 5 / JUnit Jupiter.
- Package dasar: `id.ac.upi.pppl` kecuali ada alasan yang jelas untuk mengubahnya.
- Aplikasi console tanpa database, API eksternal, framework web, API key, atau layanan berbayar.
- Dependency produksi sebaiknya tidak diperlukan; JUnit 5 cukup sebagai dependency test.
- Gunakan struktur standar Maven.
- Bahasa dokumentasi: Bahasa Indonesia. Nama class/method menggunakan bahasa Inggris yang jelas.

## Prinsip implementasi

1. Baca `PRD.md` dan `IMPLEMENTATION_PLAN.md` terlebih dahulu.
2. Implementasikan hanya kebutuhan yang tercantum di PRD.
3. Gunakan OOP secukupnya: class dan tanggung jawab jelas; hindari abstraksi berlebihan.
4. Validasi input pada tempat yang tepat dan dokumentasikan exception/hasil yang diharapkan.
5. Jangan diam-diam mengubah aturan bisnis, rentang batas, nama method target analisis, atau expected output.
6. Jika ada ambiguitas yang tidak menghambat implementasi, pilih solusi sederhana lalu tuliskan asumsi di README. Jangan menciptakan fitur tambahan.
7. Jangan membuat klaim bahwa test lulus sebelum menjalankan atau memverifikasi hasilnya.
8. Jangan mengarang log, output terminal, hasil coverage, CFG, atau nilai Cyclomatic Complexity.
9. Pastikan nilai uang menggunakan tipe integer/long untuk proyek ini, kecuali terdapat alasan yang dijelaskan.
10. Hindari state global dan ketergantungan test pada urutan eksekusi.

## Proses kerja wajib

Kerjakan satu tahap pada satu waktu dan tunjukkan hasil ringkas setelah setiap tahap. Jangan lanjut ke tahap berikutnya sebelum memeriksa konsistensi tahap sekarang.

### Tahap 1 — Perencanaan
- Tinjau PRD.
- Tetapkan class, tanggung jawab, tiga method inti untuk analisis, aturan input, dan output.
- Catat keputusan desain ringkas.

### Tahap 2 — Struktur Maven
- Buat `pom.xml`, package directory, source directory, dan test directory.
- Gunakan `maven.compiler.release` 17 dan dependency JUnit Jupiter dengan scope `test`.
- Pastikan konfigurasi Surefire dapat menjalankan JUnit 5.

### Tahap 3 — Source code
- Implementasikan model/domain class, service/logika pemesanan, dan `Main`.
- Jangan menaruh seluruh logika di `Main`.
- Pastikan pemilihan film, jumlah tiket, subtotal, diskon/total, dan validasi mudah dijalankan secara lokal.
- Jika memakai pemesanan kursi, pastikan perilaku double-booking jelas dan dapat diuji.

### Tahap 4 — Unit test
- Buat test JUnit 5 pada `src/test/java` dengan package yang sesuai.
- Gunakan assertion, bukan sekadar `System.out.println`.
- Uji jalur normal, input batas, input invalid, dan kondisi keputusan.
- Setiap test harus menguji perilaku yang spesifik dan memiliki nama yang deskriptif.

### Tahap 5 — Jalankan pemeriksaan
- Jalankan `mvn test`.
- Perbaiki kegagalan sebelum melanjutkan.
- Jangan menutupi kegagalan dengan menghapus test yang valid.
- Jika lingkungan tidak memungkinkan untuk menjalankan Maven, nyatakan hal itu dengan transparan dan berikan perintah yang bisa dijalankan pengguna.

### Tahap 6 — Dokumentasi teknis testing
- Tetapkan tiga method yang benar-benar ada dalam kode sebagai target BVA dan decision table.
- Buat tabel input, expected result/exception, batas, kondisi, dan test terkait.
- Buat CFG untuk satu method aktual.
- Hitung CC dengan `V(G) = E - N + 2P`; tampilkan nilai N, E, P, substitusi angka, dan hasil.
- Periksa kembali bahwa CFG sesuai urutan dan percabangan di source code. Tidak boleh mengadaptasi diagram dari kode lama atau pseudocode.

### Tahap 7 — Panduan dan hasil akhir
- Lengkapi `README.md` dengan prasyarat, struktur direktori, setup, perintah build/test, cara menjalankan `Main`, dan ringkasan fitur.
- Lengkapi jawaban teori: return type `String` pada test method, pentingnya `@Test`, dan cara menguji method `protected`.
- Tampilkan ringkasan file, keputusan, cara verifikasi, dan keterbatasan yang masih ada.

## Panduan kualitas test

- Test metode normal harus memiliki expected value eksplisit.
- Untuk input invalid, gunakan `assertThrows` bila kontrak method memang melempar exception.
- Gunakan `@ParameterizedTest` hanya bila menambah kejelasan; `@Test` biasa cukup.
- Test harus deterministik, cepat, dan dapat dijalankan offline setelah dependency tersedia di cache Maven.
- Nama test dapat mengikuti pola `method_condition_expectedResult`.
- Jangan mengasumsikan bahwa `@Test` adalah satu-satunya anotasi JUnit; jelaskan bahwa ini anotasi dasar untuk test biasa.
- Untuk pertanyaan method `protected`, jangan mengklaim bahwa semua test dapat mengaksesnya langsung. Perhatikan aturan visibility Java dan utamakan menguji perilaku melalui API publik bila memungkinkan.

## Aturan analisis UTS

### CFG dan Cyclomatic Complexity
- Pilih satu method yang memiliki keputusan/percabangan, tetapi tetap sederhana.
- Labeli node dan edge.
- Nyatakan apakah node mewakili statement atau blok dasar (basic block) dan gunakan konvensi tersebut secara konsisten.
- Hitung jumlah edge dan node berdasarkan diagram final.
- Untuk satu graph terhubung, gunakan `P = 1`.
- Berikan pemeriksaan silang dengan jumlah predicate + 1 bila sesuai.
- Jelaskan interpretasi CC dengan hati-hati; jangan menyebut angka tertentu sebagai batas universal kompleksitas.

### Boundary Value Analysis
Untuk masing-masing tiga method:
- Nyatakan domain input dan batas valid.
- Uji nilai di bawah batas, nilai tepat pada batas, nilai di atas batas, dan batas atas jika ada.
- Cantumkan expected result atau exception.
- Hindari menganggap semua parameter punya batas numerik jika parameter kategorikal; untuk kategori gunakan nilai tidak dikenal/valid yang relevan dan jelaskan sebagai uji batas domain.

### Decision Table
- Untuk setiap method, sebutkan kondisi, aturan, dan hasil/aksi.
- Pastikan semua kombinasi relevan dipertimbangkan.
- Jika kombinasi tidak mungkin atau tidak relevan, berikan keterangan.
- Setiap rule penting harus dipetakan ke satu atau lebih unit test.

## Definisi selesai (Definition of Done)

- Source code lengkap dan dapat dibaca.
- `pom.xml` valid.
- `mvn test` lulus atau blocker lingkungan diungkap jelas.
- README memungkinkan rekan satu kelompok menjalankan proyek tanpa instruksi lisan.
- Ada unit test untuk kondisi normal, batas, dan gagal.
- Tiga method BVA dan tiga method decision table sudah ditetapkan.
- CFG dan CC selaras dengan source code yang benar-benar dikumpulkan.
- Tidak ada placeholder penting yang disamarkan sebagai implementasi selesai.
