package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GetDiscountPercentTest {

    private final BookingService service = new BookingService();

    @Test
    void getDiscountPercent_minQuantity_returnsZeroPercent() {
        assertEquals(0, service.getDiscountPercent(1));
    }

    @Test
    void getDiscountPercent_quantityJustBelowThreshold_returnsZeroPercent() {
        assertEquals(0, service.getDiscountPercent(4));
    }

    @Test
    void getDiscountPercent_thresholdQuantity_returnsTenPercent() {
        assertEquals(10, service.getDiscountPercent(5));
    }

    @Test
    void getDiscountPercent_maxQuantity_returnsTenPercent() {
        assertEquals(10, service.getDiscountPercent(10));
    }
}
