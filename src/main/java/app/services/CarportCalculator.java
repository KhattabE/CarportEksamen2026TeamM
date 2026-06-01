package app.services;

import app.entities.Carport;
import app.entities.Material;

import java.util.ArrayList;
import java.util.List;

public class CarportCalculator {

    public CarportCalculationResult calculate(Carport carport, List<Material> materials) {

        // Her laver vi en tom liste til alle materialer
        List<CalculatedMaterialLine> materialLines = new ArrayList<>();

        // Her henter vi carportens mål
        int lengthCm = carport.getLengthCm();
        int widthCm = carport.getWidthCm();
        int heightCm = carport.getHeightCm();

        // Her finder vi de materialer fra listen, som vi skal bruge
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

        // Her beregner vi hvor mange stolper der skal bruges
        int postQuantity = calculatePostQuantity(lengthCm, carport.isHasShed());

        // Her tilføjer vi stolper til listen
        materialLines.add(new CalculatedMaterialLine(
                post97x97.getMaterialId(),
                post97x97.getName(),
                post97x97.getUnit(),
                heightCm,
                postQuantity,
                post97x97.getPricePerUnit(),
                "Stolper til carport",
                true));

        // Her beregner vi hvor mange spær der skal bruges
        int rafterQuantity = calculateRafterQuantity(lengthCm);

        // Her tilføjer vi spær til listen
        materialLines.add(new CalculatedMaterialLine(
                rafter45x195.getMaterialId(),
                rafter45x195.getName(),
                rafter45x195.getUnit(),
                widthCm,
                rafterQuantity,
                rafter45x195.getPricePerUnit(),
                "Spær på tværs af carporten",
                true));

        // Her tilføjer vi remme til begge sider
        materialLines.add(new CalculatedMaterialLine(
                rafter45x195.getMaterialId(),
                rafter45x195.getName(),
                rafter45x195.getUnit(),
                lengthCm,
                2,
                rafter45x195.getPricePerUnit(),
                "Remme i begge sider",
                true));

        // Her tilføjer vi brædder foran og bagved
        materialLines.add(new CalculatedMaterialLine(
                board25x200.getMaterialId(),
                board25x200.getName(),
                board25x200.getUnit(),
                widthCm,
                2,
                board25x200.getPricePerUnit(),
                "Sternbrædder for og bag",
                true));

        // Her tilføjer vi brædder i siderne
        materialLines.add(new CalculatedMaterialLine(
                board25x125.getMaterialId(),
                board25x125.getName(),
                board25x125.getUnit(),
                lengthCm,
                2,
                board25x125.getPricePerUnit(),
                "Sternbrædder i siderne",
                true));

        // Her beregner vi hvor mange lægter der skal bruges
        int lathQuantity = calculateLathQuantity(lengthCm);

        // Her tilføjer vi lægter til listen
        materialLines.add(new CalculatedMaterialLine(
                lath38x73.getMaterialId(),
                lath38x73.getName(),
                lath38x73.getUnit(),
                widthCm,
                lathQuantity,
                lath38x73.getPricePerUnit(),
                "Lægter til tagkonstruktion",
                true));

        // Her beregner vi hvor mange ekstra stykker træ der skal bruges
        int reglarQuantity = calculateReglarQuantity(lengthCm);

        // Her tilføjer vi de ekstra stykker træ til listen
        materialLines.add(new CalculatedMaterialLine(
                reglar45x95.getMaterialId(),
                reglar45x95.getName(),
                reglar45x95.getUnit(),
                lengthCm,
                reglarQuantity,
                reglar45x95.getPricePerUnit(),
                "Reglar til ekstra afstivning",
                true));

        // Her beregner vi hvor mange tagplader der skal bruges
        int roofPlateQuantity = calculateRoofPlateQuantity(lengthCm, widthCm);

        // Her tilføjer vi tagplader til listen
        materialLines.add(new CalculatedMaterialLine(
                roofPlate.getMaterialId(),
                roofPlate.getName(),
                roofPlate.getUnit(),
                600,
                roofPlateQuantity,
                roofPlate.getPricePerUnit(),
                "Tagplader",
                false));

        // Her beregner vi hvor mange pakker skruer til tagplader der skal bruges
        int plastmoScrewPackages = calculatePlastmoScrewPackages(roofPlateQuantity);

        // Her tilføjer vi skruerne til listen
        materialLines.add(new CalculatedMaterialLine(
                plastmoScrews.getMaterialId(),
                plastmoScrews.getName(),
                plastmoScrews.getUnit(),
                0,
                plastmoScrewPackages,
                plastmoScrews.getPricePerUnit(),
                "Bundskruer til tagplader",
                false));

        // Her tilføjer vi hulbånd
        materialLines.add(new CalculatedMaterialLine(
                holeBand.getMaterialId(),
                holeBand.getName(),
                holeBand.getUnit(),
                0,
                2,
                holeBand.getPricePerUnit(),
                "Hulbånd til vindafstivning",
                false));

        // Her tilføjer vi højre beslag
        materialLines.add(new CalculatedMaterialLine(
                rightBracket.getMaterialId(),
                rightBracket.getName(),
                rightBracket.getUnit(),
                0,
                rafterQuantity,
                rightBracket.getPricePerUnit(),
                "Universalbeslag højre til spær",
                false));

        // Her tilføjer vi venstre beslag
        materialLines.add(new CalculatedMaterialLine(
                leftBracket.getMaterialId(),
                leftBracket.getName(),
                leftBracket.getUnit(),
                0,
                rafterQuantity,
                leftBracket.getPricePerUnit(),
                "Universalbeslag venstre til spær",
                false));

        // Her tilføjer vi skruer til træ
        materialLines.add(new CalculatedMaterialLine(
                screw45x60.getMaterialId(),
                screw45x60.getName(),
                screw45x60.getUnit(),
                0,
                1,
                screw45x60.getPricePerUnit(),
                "Skruer til træsamlinger",
                false));

        // Her beregner vi hvor mange pakker beslagskruer der skal bruges
        int bracketScrewPackages = calculateBracketScrewPackages(rafterQuantity);

        // Her tilføjer vi beslagskruer til listen
        materialLines.add(new CalculatedMaterialLine(
                bracketScrews40x50.getMaterialId(),
                bracketScrews40x50.getName(),
                bracketScrews40x50.getUnit(),
                0,
                bracketScrewPackages,
                bracketScrews40x50.getPricePerUnit(),
                "Beslagskruer til universalbeslag",
                false));

        // Her beregner vi hvor mange bolte der skal bruges
        int boltQuantity = postQuantity * 2;

        // Her tilføjer vi bolte til listen
        materialLines.add(new CalculatedMaterialLine(
                carriageBolt.getMaterialId(),
                carriageBolt.getName(),
                carriageBolt.getUnit(),
                0,
                boltQuantity,
                carriageBolt.getPricePerUnit(),
                "Bræddebolte til stolper og remme",
                false));

        // Skiverne skal have samme antal som boltene
        int washerQuantity = boltQuantity;

        // Her tilføjer vi skiver til listen
        materialLines.add(new CalculatedMaterialLine(
                squareWasher.getMaterialId(),
                squareWasher.getName(),
                squareWasher.getUnit(),
                0,
                washerQuantity,
                squareWasher.getPricePerUnit(),
                "Firkantskiver til bræddebolte",
                false));

        // Her tilføjer vi lange skruer
        materialLines.add(new CalculatedMaterialLine(
                screw45x70.getMaterialId(),
                screw45x70.getName(),
                screw45x70.getUnit(),
                0,
                1,
                screw45x70.getPricePerUnit(),
                "Lange skruer til kraftigere samlinger",
                false));

        // Her tilføjer vi mindre skruer
        materialLines.add(new CalculatedMaterialLine(
                screw45x50.getMaterialId(),
                screw45x50.getName(),
                screw45x50.getUnit(),
                0,
                1,
                screw45x50.getPricePerUnit(),
                "Skruer til mindre samlinger",
                false));

        // Hvis kunden har valgt skur tilføjer vi ekstra materialer
        if (carport.isHasShed()) {

            // Her beregner vi hvor mange brædder der skal bruges til skuret
            int shedBoardQuantity = calculateShedBoardQuantity(
                    carport.getShedWidthCm(),
                    carport.getShedLengthCm(),
                    heightCm);

            // Her tilføjer vi brædder til skuret
            materialLines.add(new CalculatedMaterialLine(
                    board19x100.getMaterialId(),
                    board19x100.getName(),
                    board19x100.getUnit(),
                    540,
                    shedBoardQuantity,
                    board19x100.getPricePerUnit(),
                    "Beklædning af skur",
                    true));

            // Her tilføjer vi dørgreb
            materialLines.add(new CalculatedMaterialLine(
                    doorHandle.getMaterialId(),
                    doorHandle.getName(),
                    doorHandle.getUnit(),
                    0,
                    1,
                    doorHandle.getPricePerUnit(),
                    "Stalddørsgreb til skurdør",
                    false));

            // Her tilføjer vi hængsler
            materialLines.add(new CalculatedMaterialLine(
                    hinge.getMaterialId(),
                    hinge.getName(),
                    hinge.getUnit(),
                    0,
                    2,
                    hinge.getPricePerUnit(),
                    "T-hængsler til skurdør",
                    false));

            // Her tilføjer vi beslag til skuret
            materialLines.add(new CalculatedMaterialLine(
                    angleBracket.getMaterialId(),
                    angleBracket.getName(),
                    angleBracket.getUnit(),
                    0,
                    8,
                    angleBracket.getPricePerUnit(),
                    "Vinkelbeslag til skur",
                    false));
        }

        // Her returnerer vi resultatet med alle materialer og totalpris
        return new CarportCalculationResult(materialLines);
    }

    // Beregner antal stolper
    private int calculatePostQuantity(int lengthCm, boolean hasShed) {
        int postsPerSide = (int) Math.ceil(lengthCm / 300.0) + 1;
        int totalPosts = postsPerSide * 2;

        if (hasShed) {
            totalPosts = totalPosts + 2;
        }

        return totalPosts;
    }

    // Beregner antal spær
    private int calculateRafterQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 60.0) + 1;
    }

    // Beregner antal lægter
    private int calculateLathQuantity(int lengthCm) {
        return (int) Math.ceil(lengthCm / 80.0) + 1;
    }

    // Beregner antal ekstra stykker træ
    private int calculateReglarQuantity(int lengthCm) {
        if (lengthCm <= 600) {
            return 2;
        }

        return 4;
    }

    // Beregner antal tagplader
    private int calculateRoofPlateQuantity(int lengthCm, int widthCm) {
        int rows = (int) Math.ceil(lengthCm / 600.0);
        int platesPerRow = (int) Math.ceil(widthCm / 100.0);

        return rows * platesPerRow;
    }

    // Beregner antal pakker skruer til tagplader
    private int calculatePlastmoScrewPackages(int roofPlateQuantity) {
        int screwsNeeded = roofPlateQuantity * 12;

        return Math.max(1, (int) Math.ceil(screwsNeeded / 200.0));
    }

    // Beregner antal pakker beslagskruer
    private int calculateBracketScrewPackages(int rafterQuantity) {
        int screwsNeeded = rafterQuantity * 8;

        return Math.max(1, (int) Math.ceil(screwsNeeded / 250.0));
    }

    // Beregner antal brædder til skuret
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