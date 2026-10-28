package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CalculateDiscountTest {

    private final BookingService service = new BookingService();

    @Test
    void calculateDiscount_normalSubtotalAndPercent_returnsTenPercentAmount() {
        assertEquals(20000, service.calculateDiscount(200000, 10));
    }

    @Test
    void calculateDiscount_minValidSubtotalFullPercent_returnsSubtotal() {
        assertEquals(1, service.calculateDiscount(1, 100));
    }

    @Test
    void calculateDiscount_zeroPercent_returnsZeroDiscount() {
        assertEquals(0, service.calculateDiscount(200000, 0));
    }

    @Test
    void calculateDiscount_onePercent_returnsFlooredAmount() {
        assertEquals(1, service.calculateDiscount(199, 1));
    }

    @Test
    void calculateDiscount_percentJustBelowMaximum_works() {
        assertEquals(990, service.calculateDiscount(1000, 99));
    }

    @Test
    void calculateDiscount_fullPercent_returnsFullSubtotal() {
        assertEquals(99999, service.calculateDiscount(99999, 100));
    }

    @Test
    void calculateDiscount_truncatesFractionalResult() {
        assertEquals(50, service.calculateDiscount(101, 50));
    }

    @Test
    void calculateDiscount_zeroSubtotal_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(0, 10));
    }

    @Test
    void calculateDiscount_negativeSubtotal_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(-1, 10));
    }

    @Test
    void calculateDiscount_negativePercent_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(1000, -1));
    }

    @Test
    void calculateDiscount_percentAboveMaximum_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(1000, 101));
    }

    @Test
    void calculateDiscount_invalidSubtotalAndPercent_reportsSubtotalErrorFirst() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.calculateDiscount(0, -1));
        assertEquals("Subtotal minimal 1", exception.getMessage());
    }
}
