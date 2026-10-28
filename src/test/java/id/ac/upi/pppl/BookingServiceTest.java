package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BookingServiceTest {

    @Test
    void bookingService_defaultConstructor_usesSampleCatalog() {
        BookingService service = new BookingService();
        assertEquals("M01", service.validateBooking("M01", 1).getMovieId());
    }

    @Test
    void bookingService_nullCatalog_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new BookingService(null));
    }
}
