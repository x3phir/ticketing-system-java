package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CalculateSubtotalTest {

    private final BookingService service = new BookingService();

    @Test
    void calculateSubtotal_normalPriceAndQuantity_returnsProduct() {
        assertEquals(100000, service.calculateSubtotal(50000, 2));
    }

    @Test
    void calculateSubtotal_minValidPriceAndQuantity_returnsOne() {
        assertEquals(1, service.calculateSubtotal(1, 1));
    }

    @Test
    void calculateSubtotal_priceOneAboveMinimum_returnsProduct() {
        assertEquals(2, service.calculateSubtotal(2, 1));
    }

    @Test
    void calculateSubtotal_quantityOneAboveMinimum_returnsProduct() {
        assertEquals(150000, service.calculateSubtotal(50000, 3));
    }

    @Test
    void calculateSubtotal_maxBookingQuantity_returnsProduct() {
        assertEquals(500000, service.calculateSubtotal(50000, 10));
    }

    @Test
    void calculateSubtotal_zeroPrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(0, 1));
    }

    @Test
    void calculateSubtotal_negativePrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(-1000, 1));
    }

    @Test
    void calculateSubtotal_zeroQuantity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(50000, 0));
    }

    @Test
    void calculateSubtotal_negativeQuantity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(50000, -1));
    }

    @Test
    void calculateSubtotal_invalidPriceAndQuantity_reportsPriceErrorFirst() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.calculateSubtotal(0, 0));
        assertEquals("Harga tiket minimal 1", exception.getMessage());
    }
}
