package id.ac.upi.pppl;

import java.util.Optional;

/**
 * Logika bisnis pemesanan tiket.
 * Semua method di sini dapat diuji tanpa console.
 *
 * Kontrak umum: input invalid menghasilkan IllegalArgumentException.
 */
public class BookingService {

    public static final int MIN_TICKET_PRICE = 1;
    public static final int MIN_TICKETS = 1;
    public static final int MAX_TICKETS = 10;
    public static final int MIN_SUBTOTAL = 1;
    public static final int MIN_DISCOUNT_PERCENT = 0;
    public static final int MAX_DISCOUNT_PERCENT = 100;
    public static final int DISCOUNT_MIN_QUANTITY = 5;
    public static final int DISCOUNT_PERCENT = 10;

    private final MovieCatalog movieCatalog;

    public BookingService() {
        this(MovieCatalog.sample());
    }

    public BookingService(MovieCatalog movieCatalog) {
        if (movieCatalog == null) {
            throw new IllegalArgumentException("Katalog film tidak boleh null");
        }
        this.movieCatalog = movieCatalog;
    }

    /**
     * Menghitung harga sebelum diskon.
     * Valid: ticketPrice &gt;= 1 dan quantity &gt;= 1, selain itu IllegalArgumentException.
     */
    public int calculateSubtotal(int ticketPrice, int quantity) {
        if (ticketPrice < MIN_TICKET_PRICE) {
            throw new IllegalArgumentException("Harga tiket minimal " + MIN_TICKET_PRICE);
        }
        if (quantity < MIN_TICKETS) {
            throw new IllegalArgumentException("Jumlah tiket minimal " + MIN_TICKETS);
        }
        return ticketPrice * quantity;
    }

    /**
     * Menghitung nominal diskon: subtotal * discountPercent / 100 (pembulatan integer ke bawah).
     * Valid: subtotal &gt;= 1 dan 0 &lt;= discountPercent &lt;= 100, selain itu IllegalArgumentException.
     */
    public int calculateDiscount(int subtotal, int discountPercent) {
        if (subtotal < MIN_SUBTOTAL) {
            throw new IllegalArgumentException("Subtotal minimal " + MIN_SUBTOTAL);
        }
        if (discountPercent < MIN_DISCOUNT_PERCENT || discountPercent > MAX_DISCOUNT_PERCENT) {
            throw new IllegalArgumentException(
                    "Persentase diskon harus antara " + MIN_DISCOUNT_PERCENT + " dan " + MAX_DISCOUNT_PERCENT);
        }
        return subtotal * discountPercent / 100;
    }

    /**
     * Menghitung total bayar: subtotal - diskon.
     * Mengikuti kontrak calculateDiscount.
     */
    public int calculateTotal(int subtotal, int discountPercent) {
        return subtotal - calculateDiscount(subtotal, discountPercent);
    }

    /**
     * Aturan diskon contoh: 10% untuk pembelian minimal 5 tiket, selain itu 0%.
     * Method ini hanya membaca aturan; validasi jumlah dilakukan validateBooking.
     */
    public int getDiscountPercent(int quantity) {
        return quantity >= DISCOUNT_MIN_QUANTITY ? DISCOUNT_PERCENT : 0;
    }

    /**
     * Memvalidasi pemesanan: film harus ada di katalog dan jumlah tiket 1..10.
     * Mengembalikan film yang lolos validasi.
     * Selain itu IllegalArgumentException.
     */
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
}
