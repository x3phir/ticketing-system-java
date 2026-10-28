package id.ac.upi.pppl;

/**
 * Domain film: identitas, judul, dan harga tiket.
 * Data harga divalidasi pada constructor.
 */
public class Movie {

    private final String movieId;
    private final String title;
    private final int ticketPrice;

    public Movie(String movieId, String title, int ticketPrice) {
        if (movieId == null || movieId.isBlank()) {
            throw new IllegalArgumentException("ID film tidak boleh kosong");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Judul film tidak boleh kosong");
        }
        if (ticketPrice <= 0) {
            throw new IllegalArgumentException("Harga tiket harus lebih besar dari 0");
        }
        this.movieId = movieId;
        this.title = title;
        this.ticketPrice = ticketPrice;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return movieId + " | " + title + " | Rp" + ticketPrice;
    }
}
