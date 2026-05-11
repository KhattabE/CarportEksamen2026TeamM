package app.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {

    // Tests the full constructor used when reading a quote from the database
    @Test
    void testFullQuoteConstructorAndGetters() {
        LocalDate validUntil = LocalDate.now().plusDays(14);
        LocalDateTime createdAt = LocalDateTime.now();

        Quote quote = new Quote(1, 2, 3, new BigDecimal("15995.00"), "DRAFT", "Tilbud oprettet", validUntil, createdAt, null, null, null);

        assertEquals(1, quote.getQuoteId());
        assertEquals(2, quote.getRequestId());
        assertEquals(3, quote.getSellerId());
        assertEquals(new BigDecimal("15995.00"), quote.getTotalPrice());
        assertEquals("DRAFT", quote.getStatus());
        assertEquals("Tilbud oprettet", quote.getSellerComment());
        assertEquals(validUntil, quote.getValidUntil());
        assertEquals(createdAt, quote.getCreatedAt());
        assertNull(quote.getSentAt());
        assertNull(quote.getAcceptedAt());
        assertNull(quote.getRejectedAt());
    }


    // Tests the smaller constructor used before inserting a new quote into the database
    @Test
    void testCreateQuoteConstructor() {
        LocalDate validUntil = LocalDate.now().plusDays(14);

        Quote quote = new Quote(2, 3, new BigDecimal("15995.00"), "DRAFT", "Tilbud oprettet", validUntil);

        assertEquals(2, quote.getRequestId());
        assertEquals(3, quote.getSellerId());
        assertEquals(new BigDecimal("15995.00"), quote.getTotalPrice());
        assertEquals("DRAFT", quote.getStatus());
        assertEquals("Tilbud oprettet", quote.getSellerComment());
        assertEquals(validUntil, quote.getValidUntil());
    }
}