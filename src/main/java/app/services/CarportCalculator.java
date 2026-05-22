package app.services;

import app.entities.Carport;
import app.entities.Material;

import java.util.ArrayList;
import java.util.List;

public class CarportCalculator {

    public CarportCalculationResult calculate(Carport carport, List<Material> materials) {
        List<CalculatedMaterialLine> materialLines = new ArrayList<>();

        int lengthCm = carport.getLengthCm();
        int widthCm = carport.getWidthCm();
        int heightCm = carport.getHeightCm();

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

        int postQuantity = calculatePostQuantity(lengthCm, carport.isHasShed());
        materialLines.add(new CalculatedMaterialLine(post97x97.getMaterialId(), post97x97.getName(), heightCm, postQuantity, post97x97.getPricePerUnit(), "Stolper til carport", true));

        int rafterQuantity = calculateRafterQuantity(lengthCm);
        materialLines.add(new CalculatedMaterialLine(rafter45x195.getMaterialId(), rafter45x195.getName(), widthCm, rafterQuantity, rafter45x195.getPricePerUnit(), "Spær på tværs af carporten", true));

        materialLines.add(new CalculatedMaterialLine(rafter45x195.getMaterialId(), rafter45x195.getName(), lengthCm, 2, rafter45x195.getPricePerUnit(), "Remme i begge sider", true));

        materialLines.add(new CalculatedMaterialLine(board25x200.getMaterialId(), board25x200.getName(), widthCm, 2, board25x200.getPricePerUnit(), "Sternbrædder for og bag", true));

        materialLines.add(new CalculatedMaterialLine(board25x125.getMaterialId(), board25x125.getName(), lengthCm, 2, board25x125.getPricePerUnit(), "Sternbrædder i siderne", true));

        int lathQuantity = calculateLathQuantity(lengthCm);
        materialLines.add(new CalculatedMaterialLine(lath38x73.getMaterialId(), lath38x73.getName(), widthCm, lathQuantity, lath38x73.getPricePerUnit(), "Lægter til tagkonstruktion", true));

        int reglarQuantity = calculateReglarQuantity(lengthCm);
        materialLines.add(new CalculatedMaterialLine(reglar45x95.getMaterialId(), reglar45x95.getName(), lengthCm, reglarQuantity, reglar45x95.getPricePerUnit(), "Reglar til ekstra afstivning", true));

        int roofPlateQuantity = calculateRoofPlateQuantity(lengthCm, widthCm);
        materialLines.add(new CalculatedMaterialLine(roofPlate.getMaterialId(), roofPlate.getName(), 600, roofPlateQuantity, roofPlate.getPricePerUnit(), "Tagplader", false));

        int plastmoScrewPackages = calculatePlastmoScrewPackages(roofPlateQuantity);
        materialLines.add(new CalculatedMaterialLine(plastmoScrews.getMaterialId(), plastmoScrews.getName(), 0, plastmoScrewPackages, plastmoScrews.getPricePerUnit(), "Bundskruer til tagplader", false));

        materialLines.add(new CalculatedMaterialLine(holeBand.getMaterialId(), holeBand.getName(), 0, 2, holeBand.getPricePerUnit(), "Hulbånd til vindafstivning", false));

        materialLines.add(new CalculatedMaterialLine(rightBracket.getMaterialId(), rightBracket.getName(), 0, rafterQuantity, rightBracket.getPricePerUnit(), "Universalbeslag højre til spær", false));

        materialLines.add(new CalculatedMaterialLine(leftBracket.getMaterialId(), leftBracket.getName(), 0, rafterQuantity, leftBracket.getPricePerUnit(), "Universalbeslag venstre til spær", false));

        materialLines.add(new CalculatedMaterialLine(screw45x60.getMaterialId(), screw45x60.getName(), 0, 1, screw45x60.getPricePerUnit(), "Skruer til træsamlinger", false));

        int bracketScrewPackages = calculateBracketScrewPackages(rafterQuantity);
        materialLines.add(new CalculatedMaterialLine(bracketScrews40x50.getMaterialId(), bracketScrews40x50.getName(), 0, bracketScrewPackages, bracketScrews40x50.getPricePerUnit(), "Beslagskruer til universalbeslag", false));

        int boltQuantity = postQuantity * 2;
        materialLines.add(new CalculatedMaterialLine(carriageBolt.getMaterialId(), carriageBolt.getName(), 0, boltQuantity, carriageBolt.getPricePerUnit(), "Bræddebolte til stolper og remme", false));

        int washerQuantity = boltQuantity;
        materialLines.add(new CalculatedMaterialLine(squareWasher.getMaterialId(), squareWasher.getName(), 0, washerQuantity, squareWasher.getPricePerUnit(), "Firkantskiver til bræddebolte", false));

        materialLines.add(new CalculatedMaterialLine(screw45x70.getMaterialId(), screw45x70.getName(), 0, 1, screw45x70.getPricePerUnit(), "Lange skruer til kraftigere samlinger", false));

        materialLines.add(new CalculatedMaterialLine(screw45x50.getMaterialId(), screw45x50.getName(), 0, 1, screw45x50.getPricePerUnit(), "Skruer til mindre samlinger", false));

        if (carport.isHasShed()) {
            int shedBoardQuantity = calculateShedBoardQuantity(carport.getShedWidthCm(), carport.getShedLengthCm(), heightCm);

            materialLines.add(new CalculatedMaterialLine(board19x100.getMaterialId(), board19x100.getName(), 540, shedBoardQuantity, board19x100.getPricePerUnit(), "Beklædning af skur", true));

            materialLines.add(new CalculatedMaterialLine(doorHandle.getMaterialId(), doorHandle.getName(), 0, 1, doorHandle.getPricePerUnit(), "Stalddørsgreb til skurdør", false));

            materialLines.add(new CalculatedMaterialLine(hinge.getMaterialId(), hinge.getName(), 0, 2, hinge.getPricePerUnit(), "T-hængsler til skurdør", false));

            materialLines.add(new CalculatedMaterialLine(angleBracket.getMaterialId(), angleBracket.getName(), 0, 8, angleBracket.getPricePerUnit(), "Vinkelbeslag til skur", false));
        }

        return new CarportCalculationResult(materialLines);
    }

    private int calculatePostQuantity(int lengthCm, boolean hasShed) {
        int postsPerSide = (int) Math.ceil(lengthCm / 300.0) + 1;
        int totalPosts = postsPerSide * 2;

        if (hasShed) {
            totalPosts = totalPosts + 2;
        }

        return totalPosts;
    }

    private int calculateRafterQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 60.0) + 1;
    }

    private int calculateLathQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 80.0) + 1;
    }

    private int calculateReglarQuantity(int lengthCm) {
        if (lengthCm <= 600) {
            return 2;
        }

        return 4;
    }

    private int calculateRoofPlateQuantity(int lengthCm, int widthCm) {
        int rows = (int) Math.ceil(lengthCm / 600.0);
        int platesPerRow = (int) Math.ceil(widthCm / 100.0);

        return rows * platesPerRow;
    }

    private int calculatePlastmoScrewPackages(int roofPlateQuantity) {
        int screwsNeeded = roofPlateQuantity * 12;
        return Math.max(1, (int) Math.ceil(screwsNeeded / 200.0));
    }

    private int calculateBracketScrewPackages(int rafterQuantity) {
        int screwsNeeded = rafterQuantity * 8;
        return Math.max(1, (int) Math.ceil(screwsNeeded / 250.0));
    }

    private int calculateShedBoardQuantity(int shedWidthCm, int shedLengthCm, int heightCm) {
        int shedPerimeter = (shedWidthCm + shedLengthCm) * 2;
        int boardRows = (int) Math.ceil(heightCm / 10.0);
        int totalBoardLength = shedPerimeter * boardRows;

        return (int) Math.ceil(totalBoardLength / 540.0);
    }

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
