package app.controllers;

import app.entities.Carport;
import app.services.Svg;

public class SvgController {

    public static String createCarportSvg(Carport carport) {
        return createCarportSvgObject(carport).toString();
    }

    public static Svg createCarportSvgObject(Carport carport) {
        // Henter carportens mål
        int lengthCm = carport.getLengthCm();
        int widthCm = carport.getWidthCm();

        // Luft rundt om tegningen så der er plads til mål og pile
        int margin = 70;

        // Størrelsen på hele SVGET
        int svgWidth = lengthCm + 160;
        int svgHeight = widthCm + 170;

        // Opretter selve SVG tegningen
        Svg svg = new Svg(svgWidth, svgHeight, "0 0 " + svgWidth + " " + svgHeight);

        // Ydre ramme af carporten
        svg.addRectangle(margin, margin, lengthCm, widthCm, "stroke:black; fill:white; stroke-width:2;");

        // Øverste rem
        svg.addRectangle(margin, margin + 10, lengthCm, 5, "stroke:black; fill:white; stroke-width:1;");

        // Nederste rem
        svg.addRectangle(margin, margin + widthCm - 15, lengthCm, 5, "stroke:black; fill:white; stroke-width:1;");

        // Diagonal linje fra øverste venstre til nederste højre
        svg.addLine(margin, margin, margin + lengthCm, margin + widthCm, "stroke:black; stroke-dasharray:5 5;");

        // Diagonal linje fra nederste venstre til øverste højre
        svg.addLine(margin, margin + widthCm, margin + lengthCm, margin, "stroke:black; stroke-dasharray:5 5;");

        // Stolper i øverste side
        svg.addRectangle(margin + 10, margin + 5, 10, 10, "stroke:black; fill:white;");
        svg.addRectangle(margin + lengthCm / 2, margin + 5, 10, 10, "stroke:black; fill:white;");
        svg.addRectangle(margin + lengthCm - 20, margin + 5, 10, 10, "stroke:black; fill:white;");

        // Stolper i nederste side
        svg.addRectangle(margin + 10, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");
        svg.addRectangle(margin + lengthCm / 2, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");
        svg.addRectangle(margin + lengthCm - 20, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");

        // Startposition for spær
        int linePosition = margin + 60;

        // Tegner spær for hver 60 cm
        while (linePosition < margin + lengthCm) {
            svg.addLine(linePosition, margin, linePosition, margin + widthCm, "stroke:black; stroke-width:1;");

            linePosition = linePosition + 60;
        }

        // Pil der viser længden
        svg.addArrow(margin, margin + widthCm + 50, margin + lengthCm, margin + widthCm + 50, "stroke:black;");

        // Tekst med længdemål
        svg.addText(margin + lengthCm / 2, margin + widthCm + 85, lengthCm + " cm");

        // Pil der viser bredden
        svg.addArrow(margin - 35, margin, margin - 35, margin + widthCm, "stroke:black;");

        // Tekst med breddemål
        svg.addRotatedText(margin - 55, margin + widthCm / 2, -90, widthCm + " cm");

        return svg;
        // Returnerer SVGen som tekst så den kan vises i HTML
        return svg.toString();
    }
}