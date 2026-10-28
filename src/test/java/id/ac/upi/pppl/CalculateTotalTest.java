package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CalculateTotalTest {

    private final BookingService service = new BookingService();

    @Test
    void calculateTotal_withTenPercentDiscount_returnsSubtotalMinusDiscount() {
        assertEquals(180000, service.calculateTotal(200000, 10));
    }

    @Test
    void calculateTotal_zeroPercent_returnsSubtotal() {
        assertEquals(200000, service.calculateTotal(200000, 0));
    }

    @Test
    void calculateTotal_fullPercent_returnsZero() {
        assertEquals(0, service.calculateTotal(99999, 100));
    }

    @Test
    void calculateTotal_invalidPercent_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotal(1000, 101));
    }

    @Test
    void calculateTotal_zeroSubtotal_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotal(0, 10));
    }
}
