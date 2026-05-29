package app.services;

import app.entities.Carport;
import app.entities.Material;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarportCalculatorTest {

    // Tests that calculator returns material lines and total price for a normal carport
    @Test
    void testCalculateCarportWithoutShed() {
        Carport carport = new Carport(300, 600, 230, false, 0, 0, "Fladt tag");
        CarportCalculator calculator = new CarportCalculator();

        CarportCalculationResult result = calculator.calculate(carport, createMaterials());

        assertNotNull(result);
        assertFalse(result.getMaterialLines().isEmpty());
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);

        for (CalculatedMaterialLine line : result.getMaterialLines()) {
            assertNotEquals("Beklædning af skur", line.getUsageDescription());
        }
    }

    // Tests that shed materials are included when the carport has a shed
    @Test
    void testCalculateCarportWithShedAddsShedMaterials() {
        Carport carport = new Carport(300, 600, 230, true, 200, 300, "Fladt tag");
        CarportCalculator calculator = new CarportCalculator();

        CarportCalculationResult result = calculator.calculate(carport, createMaterials());

        boolean hasShedBoardLine = false;

        for (CalculatedMaterialLine line : result.getMaterialLines()) {
            if ("Beklædning af skur".equals(line.getUsageDescription())) {
                hasShedBoardLine = true;
            }
        }

        assertTrue(hasShedBoardLine);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    // Tests that calculator throws an error if a required material is missing
    @Test
    void testCalculateThrowsExceptionWhenMaterialIsMissing() {
        Carport carport = new Carport(300, 600, 230, false, 0, 0, "Fladt tag");
        CarportCalculator calculator = new CarportCalculator();

        assertThrows(RuntimeException.class, () -> calculator.calculate(carport, new ArrayList<>()));
    }

    private List<Material> createMaterials() {
        List<Material> materials = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        materials.add(new Material(1, "25x200 mm. trykimp. Brædt", "Test", "Træ & Tagplader", "stk", new BigDecimal("149.95"), true, now, now));
        materials.add(new Material(2, "25x125 mm. trykimp. Brædt", "Test", "Træ & Tagplader", "stk", new BigDecimal("89.95"), true, now, now));
        materials.add(new Material(3, "38x73 mm. Lægte ubh.", "Test", "Træ & Tagplader", "stk", new BigDecimal("49.95"), true, now, now));
        materials.add(new Material(4, "45x95 mm. Reglar ub.", "Test", "Træ & Tagplader", "stk", new BigDecimal("69.95"), true, now, now));
        materials.add(new Material(5, "45x195 mm. spærtræ ubh.", "Test", "Træ & Tagplader", "stk", new BigDecimal("189.95"), true, now, now));
        materials.add(new Material(6, "97x97 mm. trykimp. Stolpe", "Test", "Træ & Tagplader", "stk", new BigDecimal("249.95"), true, now, now));
        materials.add(new Material(7, "19x100 mm. trykimp. Brædt", "Test", "Træ & Tagplader", "stk", new BigDecimal("39.95"), true, now, now));
        materials.add(new Material(8, "Plastmo Ecolite blåtonet", "Test", "Træ & Tagplader", "stk", new BigDecimal("229.95"), true, now, now));
        materials.add(new Material(9, "plastmo bundskruer 200 stk.", "Test", "Beslag & Skruer", "pakke", new BigDecimal("89.95"), true, now, now));
        materials.add(new Material(10, "hulbånd 1x20 mm. 10 mtr.", "Test", "Beslag & Skruer", "rulle", new BigDecimal("129.95"), true, now, now));
        materials.add(new Material(11, "universal 190 mm højre", "Test", "Beslag & Skruer", "stk", new BigDecimal("24.95"), true, now, now));
        materials.add(new Material(12, "universal 190 mm venstre", "Test", "Beslag & Skruer", "stk", new BigDecimal("24.95"), true, now, now));
        materials.add(new Material(13, "4,5 x 60 mm. skruer 200 stk.", "Test", "Beslag & Skruer", "pakke", new BigDecimal("79.95"), true, now, now));
        materials.add(new Material(14, "4,0 x 50 mm. beslagskruer 250 stk.", "Test", "Beslag & Skruer", "pakke", new BigDecimal("99.95"), true, now, now));
        materials.add(new Material(15, "bræddebolt 10 x 120 mm.", "Test", "Beslag & Skruer", "stk", new BigDecimal("14.95"), true, now, now));
        materials.add(new Material(16, "firkantskiver 40x40x11mm", "Test", "Beslag & Skruer", "stk", new BigDecimal("4.95"), true, now, now));
        materials.add(new Material(17, "4,5 x 70 mm. Skruer 400 stk.", "Test", "Beslag & Skruer", "pk.", new BigDecimal("119.95"), true, now, now));
        materials.add(new Material(18, "4,5 x 50 mm. Skruer 300 stk.", "Test", "Beslag & Skruer", "pk.", new BigDecimal("99.95"), true, now, now));
        materials.add(new Material(19, "stalddørsgreb 50x75", "Test", "Beslag & Skruer", "sæt", new BigDecimal("149.95"), true, now, now));
        materials.add(new Material(20, "t hængsel 390 mm", "Test", "Beslag & Skruer", "stk", new BigDecimal("79.95"), true, now, now));
        materials.add(new Material(21, "vinkelbeslag 35", "Test", "Beslag & Skruer", "stk", new BigDecimal("9.95"), true, now, now));

        return materials;
    }
}