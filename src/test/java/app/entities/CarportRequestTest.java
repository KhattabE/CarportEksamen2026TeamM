package app.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CarportRequestTest {

    // Tests that a CarportRequest correctly stores user id, carport, status and comment
    @Test
    void testCarportRequestConstructorAndGetters() {
        Carport carport = new Carport(300, 600, 230, true, 200, 300, "FLAT");

        LocalDateTime createdAt = LocalDateTime.now();

        CarportRequest request = new CarportRequest(1, 10, carport, "PENDING", "Jeg ønsker en carport med skur", createdAt);

        assertEquals(1, request.getRequestId());
        assertEquals(10, request.getUserId());
        assertEquals(carport, request.getCarport());
        assertEquals("PENDING", request.getStatus());
        assertEquals("Jeg ønsker en carport med skur", request.getCustomerComment());
        assertEquals(createdAt, request.getCreatedAt());
    }


    // Tests that the request status can be changed
    @Test
    void testSetStatus() {
        Carport carport = new Carport(300, 600, 230, false, 0, 0, "FLAT");

        CarportRequest request = new CarportRequest(1, 10, carport, "PENDING", "Ingen kommentar", LocalDateTime.now());

        request.setStatus("PROCESSED");

        assertEquals("PROCESSED", request.getStatus());
    }
}