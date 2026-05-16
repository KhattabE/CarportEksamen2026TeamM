package app.services;

import app.entities.Carport;
import app.entities.Material;

import java.util.ArrayList;
import java.util.List;

public class CarportCalculator {

    // Calculates materials and total price based on the customer's carport information
    public CarportCalculationResult calculate(Carport carport, List<Material> materials) {
        List<CalculatedMaterialLine> materialLines = new ArrayList<>();

        int lengthCm = carport.getLengthCm();
        int widthCm = carport.getWidthCm();
        int heightCm = carport.getHeightCm();

        // Gets all materials from the database list
        Material board25x200 = findMaterialByName(materials, "25x200");
        Material board25x125 = findMaterialByName(materials, "25x125");
        Material lath38x73 = findMaterialByName(materials, "38x73");
        Material reglar45x95 = findMaterialByName(materials, "45x95");
        Material rafter45x195 = findMaterialByName(materials, "45x195");
        Material post97x97 = findMaterialByName(materials, "97x97");
        Material board19x100 = findMaterialByName(materials, "19x100");
        Material roofPlate = findMaterialByName(materials, "Plastmo Ecolite");
        Material plastmoScrews = findMaterialByName(materials, "bundskruer");
        Material holeBand = findMaterialByName(materials, "hulbånd");
        Material rightBracket = findMaterialByName(materials, "højre");
        Material leftBracket = findMaterialByName(materials, "venstre");
        Material screw45x60 = findMaterialByName(materials, "4,5 x 60");
        Material bracketScrews40x50 = findMaterialByName(materials, "4,0 x 50");
        Material carriageBolt = findMaterialByName(materials, "bræddebolt");
        Material squareWasher = findMaterialByName(materials, "firkantskiver");
        Material screw45x70 = findMaterialByName(materials, "4,5 x 70");
        Material screw45x50 = findMaterialByName(materials, "4,5 x 50");
        Material doorHandle = findMaterialByName(materials, "stalddørsgreb");
        Material hinge = findMaterialByName(materials, "t hængsel");
        Material angleBracket = findMaterialByName(materials, "vinkelbeslag");

        // 1. Stolper
        int postQuantity = calculatePostQuantity(lengthCm, carport.isHasShed());

        materialLines.add(new CalculatedMaterialLine(post97x97.getMaterialId(), heightCm, postQuantity, post97x97.getPricePerUnit(), "Stolper til carport"));

        // 2. Spær
        int rafterQuantity = calculateRafterQuantity(lengthCm);

        materialLines.add(new CalculatedMaterialLine(rafter45x195.getMaterialId(), widthCm, rafterQuantity, rafter45x195.getPricePerUnit(), "Spær på tværs af carporten"));

        // 3. Remme i siderne
        materialLines.add(new CalculatedMaterialLine(rafter45x195.getMaterialId(), lengthCm, 2, rafter45x195.getPricePerUnit(), "Remme i begge sider"));

        // 4. Sternbrædder for og bag
        materialLines.add(new CalculatedMaterialLine(board25x200.getMaterialId(), widthCm, 2, board25x200.getPricePerUnit(), "Sternbrædder for og bag"));

        // 5. Sternbrædder i siderne
        materialLines.add(new CalculatedMaterialLine(board25x125.getMaterialId(), lengthCm, 2, board25x125.getPricePerUnit(), "Sternbrædder i siderne"));

        // 6. Lægter
        int lathQuantity = calculateLathQuantity(lengthCm);

        materialLines.add(new CalculatedMaterialLine(lath38x73.getMaterialId(), widthCm, lathQuantity, lath38x73.getPricePerUnit(), "Lægter til tagkonstruktion"));

        // 7. Reglar
        int reglarQuantity = calculateReglarQuantity(lengthCm);

        materialLines.add(new CalculatedMaterialLine(reglar45x95.getMaterialId(), lengthCm, reglarQuantity, reglar45x95.getPricePerUnit(), "Reglar til ekstra afstivning"));

        // 8. Tagplader
        int roofPlateQuantity = calculateRoofPlateQuantity(lengthCm, widthCm);

        materialLines.add(new CalculatedMaterialLine(roofPlate.getMaterialId(), 600, roofPlateQuantity, roofPlate.getPricePerUnit(), "Tagplader"));

        // 9. Plastmo bundskruer
        int plastmoScrewPackages = calculatePlastmoScrewPackages(roofPlateQuantity);

        materialLines.add(new CalculatedMaterialLine(plastmoScrews.getMaterialId(), 0, plastmoScrewPackages, plastmoScrews.getPricePerUnit(), "Bundskruer til tagplader"));

        // 10. Hulbånd
        materialLines.add(new CalculatedMaterialLine(holeBand.getMaterialId(), 0, 2, holeBand.getPricePerUnit(), "Hulbånd til vindafstivning"));

        // 11. Universalbeslag højre
        materialLines.add(new CalculatedMaterialLine(rightBracket.getMaterialId(), 0, rafterQuantity, rightBracket.getPricePerUnit(), "Universalbeslag højre til spær"));

        // 12. Universalbeslag venstre
        materialLines.add(new CalculatedMaterialLine(leftBracket.getMaterialId(), 0, rafterQuantity, leftBracket.getPricePerUnit(), "Universalbeslag venstre til spær"));

        // 13. 4,5 x 60 skruer
        materialLines.add(new CalculatedMaterialLine(screw45x60.getMaterialId(), 0, 1, screw45x60.getPricePerUnit(), "Skruer til træsamlinger"));

        // 14. Beslagskruer
        int bracketScrewPackages = calculateBracketScrewPackages(rafterQuantity);

        materialLines.add(new CalculatedMaterialLine(bracketScrews40x50.getMaterialId(), 0, bracketScrewPackages, bracketScrews40x50.getPricePerUnit(), "Beslagskruer til universalbeslag"));

        // 15. Bræddebolte
        int boltQuantity = postQuantity * 2;

        materialLines.add(new CalculatedMaterialLine(carriageBolt.getMaterialId(), 0, boltQuantity, carriageBolt.getPricePerUnit(), "Bræddebolte til stolper og remme"));

        // 16. Firkantskiver
        int washerQuantity = boltQuantity;

        materialLines.add(new CalculatedMaterialLine(squareWasher.getMaterialId(), 0, washerQuantity, squareWasher.getPricePerUnit(), "Firkantskiver til bræddebolte"));

        // 17. 4,5 x 70 skruer
        materialLines.add(new CalculatedMaterialLine(screw45x70.getMaterialId(), 0, 1, screw45x70.getPricePerUnit(), "Lange skruer til kraftigere samlinger"));

        // 18. 4,5 x 50 skruer
        materialLines.add(new CalculatedMaterialLine(screw45x50.getMaterialId(), 0, 1, screw45x50.getPricePerUnit(), "Skruer til mindre samlinger"));

        // 19-21. Skur-materialer bruges kun hvis kunden vælger skur
        if (carport.isHasShed()) {
            int shedBoardQuantity = calculateShedBoardQuantity(carport.getShedWidthCm(), carport.getShedLengthCm(), heightCm);

            materialLines.add(new CalculatedMaterialLine(board19x100.getMaterialId(), 540, shedBoardQuantity, board19x100.getPricePerUnit(), "Beklædning af skur"));

            materialLines.add(new CalculatedMaterialLine(doorHandle.getMaterialId(), 0, 1, doorHandle.getPricePerUnit(), "Stalddørsgreb til skurdør"));

            materialLines.add(new CalculatedMaterialLine(hinge.getMaterialId(), 0, 2, hinge.getPricePerUnit(), "T-hængsler til skurdør"));

            materialLines.add(new CalculatedMaterialLine(angleBracket.getMaterialId(), 0, 8, angleBracket.getPricePerUnit(), "Vinkelbeslag til skur"));
        }

        return new CarportCalculationResult(materialLines);
    }

    // Calculates posts based on carport length and shed
    private int calculatePostQuantity(int lengthCm, boolean hasShed) {
        int postsPerSide = (int) Math.ceil(lengthCm / 300.0) + 1;
        int totalPosts = postsPerSide * 2;

        if (hasShed) {
            totalPosts = totalPosts + 2;
        }

        return totalPosts;
    }

    // Calculates rafters with around 60 cm spacing
    private int calculateRafterQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 60.0) + 1;
    }

    // Calculates laths with around 80 cm spacing
    private int calculateLathQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 80.0) + 1;
    }

    // Calculates extra reglar
    private int calculateReglarQuantity(int lengthCm) {
        if (lengthCm <= 600) {
            return 2;
        }

        return 4;
    }

    // Calculates roof plates based on length and width
    private int calculateRoofPlateQuantity(int lengthCm, int widthCm) {
        int rows = (int) Math.ceil(lengthCm / 600.0);
        int platesPerRow = (int) Math.ceil(widthCm / 100.0);

        return rows * platesPerRow;
    }

    // Calculates Plastmo screw packages
    private int calculatePlastmoScrewPackages(int roofPlateQuantity) {
        int screwsNeeded = roofPlateQuantity * 12;
        return Math.max(1, (int) Math.ceil(screwsNeeded / 200.0));
    }

    // Calculates screw packages for brackets
    private int calculateBracketScrewPackages(int rafterQuantity) {
        int screwsNeeded = rafterQuantity * 8;
        return Math.max(1, (int) Math.ceil(screwsNeeded / 250.0));
    }

    // Calculates shed boards
    private int calculateShedBoardQuantity(int shedWidthCm, int shedLengthCm, int heightCm) {
        int shedPerimeter = (shedWidthCm + shedLengthCm) * 2;
        int boardRows = (int) Math.ceil(heightCm / 10.0);
        int totalBoardLength = shedPerimeter * boardRows;

        return (int) Math.ceil(totalBoardLength / 540.0);
    }

    // Finds material from database list by checking part of the material name
    private Material findMaterialByName(List<Material> materials, String searchText) {
        for (Material material : materials) {
            String materialName = material.getName().toLowerCase();
            String search = searchText.toLowerCase();

            if (materialName.contains(search)) {
                return material;
            }
        }

        throw new RuntimeException("Material not found: " + searchText);
    }
}