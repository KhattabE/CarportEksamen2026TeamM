package app.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarportTest {

    // Tests that the Carport constructor saves all normal carport values correctly
    @Test
    void testCarportConstructorAndGetters() {
        Carport carport = new Carport(300, 600, 230, true, 200, 300, "FLAT");

        assertEquals(300, carport.getWidthCm());
        assertEquals(600, carport.getLengthCm());
        assertEquals(230, carport.getHeightCm());
        assertTrue(carport.isHasShed());
        assertEquals(200, carport.getShedWidthCm());
        assertEquals(300, carport.getShedLengthCm());
        assertEquals("FLAT", carport.getRoofType());
    }


    // Tests that setters update the values correctly
    @Test
    void testCarportSetters() {
        Carport carport = new Carport(300, 600, 230, false, 0, 0, "FLAT");

        carport.setWidthCm(400);
        carport.setLengthCm(700);
        carport.setHeightCm(250);
        carport.setHasShed(true);
        carport.setShedWidthCm(200);
        carport.setShedLengthCm(300);
        carport.setRoofType("RAISED");

        assertEquals(400, carport.getWidthCm());
        assertEquals(700, carport.getLengthCm());
        assertEquals(250, carport.getHeightCm());
        assertTrue(carport.isHasShed());
        assertEquals(200, carport.getShedWidthCm());
        assertEquals(300, carport.getShedLengthCm());
        assertEquals("RAISED", carport.getRoofType());
    }
}