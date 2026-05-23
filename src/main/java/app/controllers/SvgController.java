package app.controllers;

import app.entities.Carport;
import app.services.Svg;

public class SvgController {

    public static String createCarportSvg(Carport carport) {

        int lengthCm = carport.getLengthCm();
        int widthCm = carport.getWidthCm();

        int margin = 70;

        int svgWidth = lengthCm + 160;
        int svgHeight = widthCm + 170;

        Svg svg = new Svg(svgWidth, svgHeight, "0 0 " + svgWidth + " " + svgHeight);

        svg.addRectangle(margin, margin, lengthCm, widthCm, "stroke:black; fill:white; stroke-width:2;");

        svg.addRectangle(margin, margin + 10, lengthCm, 5, "stroke:black; fill:white; stroke-width:1;");

        svg.addRectangle(margin, margin + widthCm - 15, lengthCm, 5, "stroke:black; fill:white; stroke-width:1;");

        svg.addLine(margin, margin, margin + lengthCm, margin + widthCm, "stroke:black; stroke-dasharray:5 5;");

        svg.addLine(margin, margin + widthCm, margin + lengthCm, margin, "stroke:black; stroke-dasharray:5 5;");

        svg.addRectangle(margin + 10, margin + 5, 10, 10, "stroke:black; fill:white;");

        svg.addRectangle(margin + lengthCm / 2, margin + 5, 10, 10, "stroke:black; fill:white;");

        svg.addRectangle(margin + lengthCm - 20, margin + 5, 10, 10, "stroke:black; fill:white;");

        svg.addRectangle(margin + 10, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");

        svg.addRectangle(margin + lengthCm / 2, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");

        svg.addRectangle(margin + lengthCm - 20, margin + widthCm - 15, 10, 10, "stroke:black; fill:white;");

        int linePosition = margin + 60;

        while (linePosition < margin + lengthCm) {
            svg.addLine(linePosition, margin, linePosition, margin + widthCm, "stroke:black; stroke-width:1;");

            linePosition = linePosition + 60;
        }

        svg.addArrow(margin, margin + widthCm + 50, margin + lengthCm, margin + widthCm + 50, "stroke:black;");

        svg.addText(margin + lengthCm / 2, margin + widthCm + 85, lengthCm + " cm");

        svg.addArrow(margin - 35, margin, margin - 35, margin + widthCm, "stroke:black;");

        svg.addRotatedText(margin - 55, margin + widthCm / 2, -90, widthCm + " cm");

        return svg.toString();
    }
}