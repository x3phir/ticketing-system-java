package id.ac.upi.pppl;

import java.util.Scanner;

/**
 * Entry point console.
 * Tugasnya hanya membaca input dan menampilkan output;
 * seluruh logika bisnis ada di BookingService.
 */
public class Main {

    public static void main(String[] args) {
        MovieCatalog catalog = MovieCatalog.sample();
        BookingService bookingService = new BookingService(catalog);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sistem Pemesanan Tiket Bioskop ===");
        System.out.println("Daftar film:");
        for (Movie movie : catalog.getMovies()) {
            System.out.println("  " + movie);
        }
        System.out.println("Aturan: jumlah tiket " + BookingService.MIN_TICKETS
                + " s.d. " + BookingService.MAX_TICKETS
                + ", diskon " + BookingService.DISCOUNT_PERCENT
                + "% untuk minimal " + BookingService.DISCOUNT_MIN_QUANTITY + " tiket.");
        System.out.println();

        System.out.print("Masukkan ID film: ");
        String movieId = scanner.nextLine().trim();

        System.out.print("Masukkan jumlah tiket: ");
        String quantityInput = scanner.nextLine().trim();

        try {
            int quantity = Integer.parseInt(quantityInput);
            Movie movie = bookingService.validateBooking(movieId, quantity);
            int subtotal = bookingService.calculateSubtotal(movie.getTicketPrice(), quantity);
            int discountPercent = bookingService.getDiscountPercent(quantity);
            int discountAmount = bookingService.calculateDiscount(subtotal, discountPercent);
            int total = bookingService.calculateTotal(subtotal, discountPercent);

            System.out.println();
            System.out.println("=== Ringkasan Transaksi ===");
            System.out.println("Film        : " + movie.getTitle());
            System.out.println("Harga tiket : Rp" + movie.getTicketPrice());
            System.out.println("Jumlah      : " + quantity + " tiket");
            System.out.println("Subtotal    : Rp" + subtotal);
            System.out.println("Diskon (" + discountPercent + "%) : Rp" + discountAmount);
            System.out.println("Total bayar : Rp" + total);
        } catch (NumberFormatException e) {
            System.out.println("Input jumlah tiket tidak valid: harus bilangan bulat.");
        } catch (IllegalArgumentException e) {
            System.out.println("Pemesanan gagal: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
