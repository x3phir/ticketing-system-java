package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MovieTest {

    @Test
    void movie_validData_keepsAllFields() {
        Movie movie = new Movie("M01", "Interstellar", 50000);
        assertEquals("M01", movie.getMovieId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals(50000, movie.getTicketPrice());
    }

    @Test
    void movie_minValidPrice_works() {
        Movie movie = new Movie("M99", "Film Murah", 1);
        assertEquals(1, movie.getTicketPrice());
    }

    @Test
    void movie_zeroPrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie("M99", "Film Gratis", 0));
    }

    @Test
    void movie_negativePrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie("M99", "Film Aneh", -5000));
    }

    @Test
    void movie_blankId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie("  ", "Film Tanpa Id", 40000));
    }

    @Test
    void movie_blankTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie("M99", "", 40000));
    }
}
