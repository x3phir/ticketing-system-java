package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ValidateBookingTest {

    private final BookingService service = new BookingService();

    @Test
    void validateBooking_knownIdAndValidQuantity_returnsMovie() {
        Movie movie = service.validateBooking("M01", 2);
        assertEquals("M01", movie.getMovieId());
        assertEquals(50000, movie.getTicketPrice());
    }

    @Test
    void validateBooking_minQuantity_returnsMovie() {
        assertEquals("M02", service.validateBooking("M02", 1).getMovieId());
    }

    @Test
    void validateBooking_quantityJustBelowMaximum_returnsMovie() {
        assertEquals("M03", service.validateBooking("M03", 9).getMovieId());
    }

    @Test
    void validateBooking_maxQuantity_returnsMovie() {
        assertEquals("M04", service.validateBooking("M04", 10).getMovieId());
    }

    @Test
    void validateBooking_unknownMovieId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooking("M99", 2));
    }

    @Test
    void validateBooking_nullMovieId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooking(null, 2));
    }

    @Test
    void validateBooking_quantityBelowMinimum_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooking("M01", 0));
    }

    @Test
    void validateBooking_negativeQuantity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooking("M01", -1));
    }

    @Test
    void validateBooking_quantityAboveMaximum_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateBooking("M01", 11));
    }

    @Test
    void validateBooking_unknownIdAndInvalidQuantity_reportsFilmErrorFirst() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.validateBooking("M99", 0));
        assertEquals("Film tidak tersedia: M99", exception.getMessage());
    }
}
