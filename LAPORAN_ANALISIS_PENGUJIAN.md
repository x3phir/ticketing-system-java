# Laporan Analisis Pengujian — Sistem Pemesanan Tiket Bioskop

**Mata kuliah:** Pengujian dan Pemeliharaan Perangkat Lunak (PPPL)
**Dokumen ini** melengkapi `README.md`. Semua isi di bawah merujuk source code final di `src/`.

---

## 0. Tiga method target analisis

| # | Signature final | Kontrak ringkas |
|---|---|---|
| 1 | `int calculateSubtotal(int ticketPrice, int quantity)` | Valid bila `ticketPrice >= 1` dan `quantity >= 1`; selain itu `IllegalArgumentException`. Mengembalikan `ticketPrice * quantity`. |
| 2 | `int calculateDiscount(int subtotal, int discountPercent)` | Valid bila `subtotal >= 1` dan `0 <= discountPercent <= 100`; selain itu `IllegalArgumentException`. Mengembalikan `subtotal * discountPercent / 100` (pembulatan integer ke bawah). |
| 3 | `Movie validateBooking(String movieId, int quantity)` | Film harus ada di katalog dan `1 <= quantity <= 10`; selain itu `IllegalArgumentException`. Mengembalikan `Movie` yang lolos validasi. |

Nama method yang sama dipakai di source code, unit test, BVA, decision table, dan laporan ini.

---

## A. Control Flow Graph (CFG)

### A.1 Method yang dianalisis (source final)

Dipilih `validateBooking` karena memiliki tiga percabangan berurutan yang mudah dipetakan.

```java
public Movie validateBooking(String movieId, int quantity) {
    Optional<Movie> found = movieCatalog.findById(movieId);
    if (found.isEmpty()) {
        throw new IllegalArgumentException("Film tidak tersedia: " + movieId);
    }
    if (quantity < MIN_TICKETS) {
        throw new IllegalArgumentException("Jumlah tiket minimal " + MIN_TICKETS);
    }
    if (quantity > MAX_TICKETS) {
        throw new IllegalArgumentException("Jumlah tiket maksimal " + MAX_TICKETS);
    }
    return found.get();
}
```

Lokasi: `src/main/java/id/ac/upi/pppl/BookingService.java`.

### A.2 Konvensi node

Setiap node adalah **satu statement atau satu keputusan (predicate)**, bukan gabungan blok.
Node `N10` (Exit) adalah penyatuan **exit normal (return) dan exit exceptional (throw)** —
semua `throw` dianggap mengalir ke satu titik keluar agar graph tetap terhubung (`P = 1`).

### A.3 Daftar node

| Node | Tipe | Isi |
|---|---|---|
| N1 | entry | Entry `validateBooking` |
| N2 | statement | `Optional<Movie> found = movieCatalog.findById(movieId);` |
| N3 | decision | `found.isEmpty()?` |
| N4 | statement | `throw new IllegalArgumentException("Film tidak tersedia: " + movieId);` |
| N5 | decision | `quantity < MIN_TICKETS?` |
| N6 | statement | `throw new IllegalArgumentException("Jumlah tiket minimal " + MIN_TICKETS);` |
| N7 | decision | `quantity > MAX_TICKETS?` |
| N8 | statement | `throw new IllegalArgumentException("Jumlah tiket maksimal " + MAX_TICKETS);` |
| N9 | statement | `return found.get();` |
| N10 | exit | Exit (normal maupun exceptional) |

**N = 10**

### A.4 Daftar edge

| Edge | Dari | Ke | Label |
|---|---|---|---|
| E1 | N1 | N2 | entry |
| E2 | N2 | N3 | sequential |
| E3 | N3 | N4 | true (film kosong) |
| E4 | N3 | N5 | false |
| E5 | N4 | N10 | throw |
| E6 | N5 | N6 | true (jumlah < 1) |
| E7 | N5 | N7 | false |
| E8 | N6 | N10 | throw |
| E9 | N7 | N8 | true (jumlah > 10) |
| E10 | N7 | N9 | false |
| E11 | N8 | N10 | throw |
| E12 | N9 | N10 | return |

**E = 12**

### A.5 Diagram

```
                    [N1 Entry]
                        |
                       [N2]  found = findById(movieId)
                        |
                       [N3]  found.isEmpty()?
                     /        \
              true  /          \  false
                  /              \
               [N4]             [N5]  quantity < MIN_TICKETS?
          throw "Film           /        \
          tidak tersedia"  true /          \ false
                  |            /            \
                  |          [N6]           [N7]  quantity > MAX_TICKETS?
                  |     throw "jumlah      /        \
                  |      minimal"     true /          \ false
                  |            |          /            \
                  |            |        [N8]          [N9]
                  |            |   throw "jumlah    return found.get()
                  |            |      maksimal"        |
                   \           |          |            /
                    \          |          |           /
                     \         |          |          /
                          [N10 Exit]
```

---

## B. Cyclomatic Complexity (CC)

Rumus: **V(G) = E − N + 2P**

- E = 12 (jumlah edge, lihat A.4)
- N = 10 (jumlah node, lihat A.3)
- P = 1 (satu method = satu graph terhubung)

**V(G) = 12 − 10 + 2(1) = 4**

### Pemeriksaan silang

Jumlah predicate (node decision: N3, N5, N7) = 3.
Untuk graph terhubung: **V(G) = predicate + 1 = 3 + 1 = 4**. Kedua metode cocok.

### Arti hasil (sederhana)

Nilai 4 berarti metode ini memiliki **4 jalur dasar independen** (4 kemungkinan hasil kontrol:
lolos ketiga pemeriksaan, gagal di pemeriksaan film, gagal di batas minimum, gagal di batas maksimum).
Angka ini dipakai sebagai panduan untuk memastikan semua jalur tercakup test — **bukan** batas universal
kualitas kode; tidak ada angka CC yang berlaku universal untuk semua proyek.

---

## C. Boundary Value Analysis (BVA) — tiga method

### C.1 `calculateSubtotal(int ticketPrice, int quantity)`

**Domain valid:** `ticketPrice >= 1`, `quantity >= 1` (batas atas pemesanan 1..10 ditegakkan di
`validateBooking`; method ini sendiri tidak membatasi atas). Nilai di luar domain → `IllegalArgumentException`.

| ID | Input | Expected | Alasan | Nama test JUnit |
|---|---|---|---|---|
| BS-01 | price=0, qty=1 | `IllegalArgumentException` | tepat di bawah batas minimum price (min−1) | `calculateSubtotal_zeroPrice_throwsIllegalArgumentException` |
| BS-02 | price=−1000, qty=1 | `IllegalArgumentException` | jauh di bawah batas (negatif) | `calculateSubtotal_negativePrice_throwsIllegalArgumentException` |
| BS-03 | price=1, qty=1 | `1` | tepat pada batas minimum kedua parameter (min) | `calculateSubtotal_minValidPriceAndQuantity_returnsOne` |
| BS-04 | price=2, qty=1 | `2` | tepat di atas batas minimum price (min+1) | `calculateSubtotal_priceOneAboveMinimum_returnsProduct` |
| BS-05 | price=50000, qty=0 | `IllegalArgumentException` | tepat di bawah batas minimum quantity (min−1) | `calculateSubtotal_zeroQuantity_throwsIllegalArgumentException` |
| BS-06 | price=50000, qty=−1 | `IllegalArgumentException` | negatif pada quantity | `calculateSubtotal_negativeQuantity_throwsIllegalArgumentException` |
| BS-07 | price=50000, qty=2 | `100000` | tepat di atas batas minimum quantity (min+1), kasus normal | `calculateSubtotal_normalPriceAndQuantity_returnsProduct` |
| BS-08 | price=50000, qty=10 | `500000` | batas maksimum jumlah tiket per pemesanan | `calculateSubtotal_maxBookingQuantity_returnsProduct` |
| BS-09 | price=0, qty=0 | `IllegalArgumentException` dengan pesan harga | kedua parameter invalid; memastikan pemeriksaan harga dievaluasi lebih dulu | `calculateSubtotal_invalidPriceAndQuantity_reportsPriceErrorFirst` |

### C.2 `calculateDiscount(int subtotal, int discountPercent)`

**Domain valid:** `subtotal >= 1`, `0 <= discountPercent <= 100`. Selain itu → `IllegalArgumentException`.

| ID | Input (subtotal, percent) | Expected | Alasan | Nama test JUnit |
|---|---|---|---|---|
| CD-01 | (0, 10) | `IllegalArgumentException` | tepat di bawah batas minimum subtotal (min−1) | `calculateDiscount_zeroSubtotal_throwsIllegalArgumentException` |
| CD-02 | (−1, 10) | `IllegalArgumentException` | negatif pada subtotal | `calculateDiscount_negativeSubtotal_throwsIllegalArgumentException` |
| CD-03 | (1, 100) | `1` | tepat pada batas minimum subtotal, sekaligus batas maksimum persen | `calculateDiscount_minValidSubtotalFullPercent_returnsSubtotal` |
| CD-04 | (200000, −1) | `IllegalArgumentException` | tepat di bawah batas minimum persen (min−1) | `calculateDiscount_negativePercent_throwsIllegalArgumentException` |
| CD-05 | (200000, 0) | `0` | tepat pada batas minimum persen (0%) | `calculateDiscount_zeroPercent_returnsZeroDiscount` |
| CD-06 | (199, 1) | `1` | tepat di atas batas minimum persen (1%), sekaligus memeriksa pembulatan | `calculateDiscount_onePercent_returnsFlooredAmount` |
| CD-07 | (1000, 99) | `990` | tepat di bawah batas maksimum persen (max−1) | `calculateDiscount_percentJustBelowMaximum_works` |
| CD-08 | (99999, 100) | `99999` | tepat pada batas maksimum persen (100%) | `calculateDiscount_fullPercent_returnsFullSubtotal` |
| CD-09 | (1000, 101) | `IllegalArgumentException` | tepat di atas batas maksimum persen (max+1) | `calculateDiscount_percentAboveMaximum_throwsIllegalArgumentException` |
| CD-10 | (101, 50) | `50` | memeriksa pembulatan integer ke bawah (50,5 → 50) | `calculateDiscount_truncatesFractionalResult` |
| CD-11 | (0, −1) | `IllegalArgumentException` dengan pesan subtotal | kedua parameter invalid; pemeriksaan subtotal lebih dulu | `calculateDiscount_invalidSubtotalAndPercent_reportsSubtotalErrorFirst` |

### C.3 `validateBooking(String movieId, int quantity)`

**Domain:** `movieId` kategorikal — nilai di dalam katalog `{M01..M04}` vs di luar katalog vs `null`;
`quantity` numerik valid `1..10`. Selain itu → `IllegalArgumentException`.

| ID | Input (movieId, quantity) | Expected | Alasan | Nama test JUnit |
|---|---|---|---|---|
| VB-01 | (`null`, 2) | `IllegalArgumentException` | batas domain kategorikal: nilai null (tidak dikenal) | `validateBooking_nullMovieId_throwsIllegalArgumentException` |
| VB-02 | ("M99", 2) | `IllegalArgumentException` | di luar domain kategorikal: id tidak ada di katalog | `validateBooking_unknownMovieId_throwsIllegalArgumentException` |
| VB-03 | ("M01", 0) | `IllegalArgumentException` | tepat di bawah batas minimum quantity (min−1) | `validateBooking_quantityBelowMinimum_throwsIllegalArgumentException` |
| VB-04 | ("M01", −1) | `IllegalArgumentException` | negatif pada quantity | `validateBooking_negativeQuantity_throwsIllegalArgumentException` |
| VB-05 | ("M02", 1) | `Movie` M02 | tepat pada batas minimum quantity (min) | `validateBooking_minQuantity_returnsMovie` |
| VB-06 | ("M01", 2) | `Movie` M01 | tepat di atas batas minimum (min+1), kasus normal | `validateBooking_knownIdAndValidQuantity_returnsMovie` |
| VB-07 | ("M03", 9) | `Movie` M03 | tepat di bawah batas maksimum (max−1) | `validateBooking_quantityJustBelowMaximum_returnsMovie` |
| VB-08 | ("M04", 10) | `Movie` M04 | tepat pada batas maksimum quantity (max) | `validateBooking_maxQuantity_returnsMovie` |
| VB-09 | ("M01", 11) | `IllegalArgumentException` | tepat di atas batas maksimum (max+1) | `validateBooking_quantityAboveMaximum_throwsIllegalArgumentException` |
| VB-10 | ("M99", 0) | `IllegalArgumentException` dengan pesan film | kombinasi dua kondisi invalid; pemeriksaan film dievaluasi lebih dulu | `validateBooking_unknownIdAndInvalidQuantity_reportsFilmErrorFirst` |

---

## D. Decision Table — tiga method

### D.1 `calculateSubtotal(int ticketPrice, int quantity)`

**Kondisi:**

- **K1:** `ticketPrice >= 1` ? (Ya / Tidak)
- **K2:** `quantity >= 1` ? (Ya / Tidak)

| Kondisi / Aturan | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| K1: ticketPrice >= 1 | Ya | Tidak | Ya | Tidak |
| K2: quantity >= 1 | Ya | — | Tidak | Tidak |
| **Expected action** | `return price * quantity` | `throw IAE` (harga) | `throw IAE` (jumlah) | `throw IAE` (harga — dicek pertama) |
| **Test terkait** | `calculateSubtotal_normalPriceAndQuantity_returnsProduct`, `calculateSubtotal_minValidPriceAndQuantity_returnsOne`, `calculateSubtotal_maxBookingQuantity_returnsProduct` | `calculateSubtotal_zeroPrice_throwsIllegalArgumentException`, `calculateSubtotal_negativePrice_throwsIllegalArgumentException` | `calculateSubtotal_zeroQuantity_throwsIllegalArgumentException`, `calculateSubtotal_negativeQuantity_throwsIllegalArgumentException` | `calculateSubtotal_invalidPriceAndQuantity_reportsPriceErrorFirst` |

Keterangan: tanda "—" pada R2 berarti hasil sudah ditentukan oleh K1 (K2 tidak relevan).
R4 bukan kombinasi mustahil — kedua parameter bisa sama-sama invalid — hasilnya tetap error harga
karena pemeriksaan harga dievaluasi lebih dulu dalam kode.

### D.2 `calculateDiscount(int subtotal, int discountPercent)`

**Kondisi:**

- **K1:** `subtotal >= 1` ? (Ya / Tidak)
- **K2:** `0 <= discountPercent <= 100` ? (Ya / Tidak)

| Kondisi / Aturan | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| K1: subtotal >= 1 | Ya | Tidak | Ya | Tidak |
| K2: persen dalam 0..100 | Ya | — | Tidak | Tidak |
| **Expected action** | `return subtotal * persen / 100` | `throw IAE` (subtotal) | `throw IAE` (persen) | `throw IAE` (subtotal — dicek pertama) |
| **Test terkait** | `calculateDiscount_normalSubtotalAndPercent_returnsTenPercentAmount`, `calculateDiscount_zeroPercent_returnsZeroDiscount`, `calculateDiscount_fullPercent_returnsFullSubtotal`, `calculateDiscount_percentJustBelowMaximum_works`, `calculateDiscount_onePercent_returnsFlooredAmount`, `calculateDiscount_truncatesFractionalResult`, `calculateDiscount_minValidSubtotalFullPercent_returnsSubtotal` | `calculateDiscount_zeroSubtotal_throwsIllegalArgumentException`, `calculateDiscount_negativeSubtotal_throwsIllegalArgumentException` | `calculateDiscount_negativePercent_throwsIllegalArgumentException`, `calculateDiscount_percentAboveMaximum_throwsIllegalArgumentException` | `calculateDiscount_invalidSubtotalAndPercent_reportsSubtotalErrorFirst` |

Keterangan: kombinasi "K1 Tidak & K2 Tidak" bukan mustahil (mis. subtotal 0 dengan persen −1);
hasilnya error subtotal karena pemeriksaan subtotal dievaluasi lebih dulu.

### D.3 `validateBooking(String movieId, int quantity)`

**Kondisi:**

- **K1:** film ditemukan di katalog ? (Ya / Tidak — mencakup `null` dan id tak dikenal)
- **K2:** `quantity >= 1` ? (Ya / Tidak)
- **K3:** `quantity <= 10` ? (Ya / Tidak)

| Kondisi / Aturan | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|
| K1: film ditemukan | Ya | Tidak | Ya | Ya | Tidak |
| K2: quantity >= 1 | Ya | — | Tidak | Ya | — |
| K3: quantity <= 10 | Ya | — | — | Tidak | — |
| **Expected action** | `return Movie` | `throw IAE` (film) | `throw IAE` (jumlah min) | `throw IAE` (jumlah max) | `throw IAE` (film — dicek pertama) |
| **Test terkait** | `validateBooking_knownIdAndValidQuantity_returnsMovie`, `validateBooking_minQuantity_returnsMovie`, `validateBooking_quantityJustBelowMaximum_returnsMovie`, `validateBooking_maxQuantity_returnsMovie` | `validateBooking_unknownMovieId_throwsIllegalArgumentException`, `validateBooking_nullMovieId_throwsIllegalArgumentException` | `validateBooking_quantityBelowMinimum_throwsIllegalArgumentException`, `validateBooking_negativeQuantity_throwsIllegalArgumentException` | `validateBooking_quantityAboveMaximum_throwsIllegalArgumentException` | `validateBooking_unknownIdAndInvalidQuantity_reportsFilmErrorFirst` |

**Kombinasi mustahil / tidak relevan:**

- **K2 = Tidak dan K3 = Ya** mustahil untuk bilangan bulat: `quantity < 1` selalu berarti
  `quantity <= 10`, karena `MIN_TICKETS (1) < MAX_TICKETS (10)`. Kombinasi ini tidak dibuat.
- Setelah K1 = Tidak, hasil sudah ditentukan (error film), sehingga K2/K3 ditandai "—"
  (tidak relevan), namun R5 tetap diuji untuk membuktikan urutan pemeriksaan.

---

## E. Jawaban teori JUnit

### E.1 Apa yang terjadi jika method test JUnit menggunakan return type `String`?

Pada JUnit Jupiter, method test biasa (bertanda `@Test`) harus berpenanda **`void`** dan tidak boleh
memiliki parameter. Jika sebuah method test diberi return type `String` (atau tipe lain selain `void`),
method tersebut **tidak memenuhi kontrak test biasa**, sehingga test engine tidak akan memakainya
sebagai test — metode tersebut tidak ditemukan/dijalankan sebagai test (atau engine dapat menolaknya
dengan galat pelanggaran kontrak, tergantung kondisi). Praktik yang benar: kembalikan `void`, lalu
pastikan kebenaran hasil dengan assertion (`assertEquals`, `assertThrows`, dll.), bukan dengan
mengembalikan nilai. (Catatan: ada anotasi khusus seperti `@TestFactory` yang memang mengembalikan
nilai, tetapi itu bukan `@Test` biasa.)

### E.2 Mengapa anotasi `@Test` penting?

`@Test` adalah penanda (metadata) yang memberi tahu JUnit bahwa sebuah method adalah test case yang
harus **ditemukan, didaftarkan, dan dieksekusi** oleh test engine. Tanpa anotasi tersebut, method
hanyalah method biasa — meskipun namanya diawali `test` dan isinya assertion, JUnit tidak akan
menjalankannya. Anotasi inilah yang membuat pemisahan antara method bantu (helper) dan method test
menjadi jelas, sehingga `mvn clean test` hanya menjalankan method yang benar-benar ditandai sebagai test.

### E.3 Bagaimana menguji method `protected` di Java/JUnit, dengan memperhatikan aturan visibility Java?

Urutan pilihan yang wajar:

1. **Uji melalui API publik terlebih dahulu.** Jika method `protected` dipanggil oleh method `public`
   dalam class yang sama, uji perilaku lewat method `public` tersebut. Ini paling stabil karena tidak
   terikat detail internal.
2. **Letakkan test class pada package yang sama** dengan class yang diuji. Dengan ini, method
   **package-private** (tanpa modifier) dapat diakses langsung dari test. Penting: ini berlaku untuk
   akses *package-protected*, **bukan** berarti akses `protected` otomatis terbuka ke seluruh isi
   package untuk semua konteks — perlu diingat aturan pewarisan Java.
3. **Gunakan subclass test** yang mewarisi class target (mis. `class TargetTest extends Target`),
   karena anggota `protected` dapat diakses dari subclass. Cara ini sah menurut aturan visibility Java
   tetapi menambah struktur pewarisan hanya untuk pengujian.
4. **Reflection** hanya jika tidak ada cara lain dan ada alasan khusus; hindari karena rapuh dan
   mematikan pemeriksaan kompilator.

Untuk proyek ini seluruh method yang diuji bersifat `public`, sehingga opsi 1 sudah cukup.

---

## F. Hasil verifikasi (nyata, dijalankan di lingkungan proyek)

Perintah: `mvn clean test` (Apache Maven 3.9.16, JDK 25 dengan `maven.compiler.release=17`).

```
Tests run: 55, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Rincian per class test:

| Class test | Jumlah test | Lulus |
|---|---|---|
| BookingServiceTest | 2 | 2 |
| CalculateDiscountTest | 12 | 12 |
| CalculateSubtotalTest | 10 | 10 |
| CalculateTotalTest | 5 | 5 |
| GetDiscountPercentTest | 4 | 4 |
| MovieCatalogTest | 6 | 6 |
| MovieTest | 6 | 6 |
| ValidateBookingTest | 10 | 10 |
| **Total** | **55** | **55** |

Aplikasi console juga diverifikasi dengan input: pemesanan normal (dengan dan tanpa diskon),
film tidak dikenal, jumlah tidak berupa angka, dan jumlah melebihi batas — semua menampilkan
pesan yang jelas tanpa crash.
