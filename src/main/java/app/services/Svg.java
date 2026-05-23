package app.services;

public class Svg {

    private StringBuilder svg;

    public Svg(int width, int height, String viewBox) {

        svg = new StringBuilder();

        svg.append("<svg ");
        svg.append("width='").append(width).append("' ");
        svg.append("height='").append(height).append("' ");
        svg.append("viewBox='").append(viewBox).append("' ");
        svg.append("xmlns='http://www.w3.org/2000/svg'>");

        svg.append("<defs>");

        svg.append("<marker ");
        svg.append("id='beginArrow' ");
        svg.append("markerWidth='12' ");
        svg.append("markerHeight='12' ");
        svg.append("refX='0' ");
        svg.append("refY='6' ");
        svg.append("orient='auto'>");
        svg.append("<path d='M 0,6 L 12,12 L 12,0' style='fill:black;' />");
        svg.append("</marker>");

        svg.append("<marker ");
        svg.append("id='endArrow' ");
        svg.append("markerWidth='12' ");
        svg.append("markerHeight='12' ");
        svg.append("refX='12' ");
        svg.append("refY='6' ");
        svg.append("orient='auto'>");
        svg.append("<path d='M 0,12 L 12,6 L 0,0' style='fill:black;' />");
        svg.append("</marker>");

        svg.append("</defs>");
    }

    public void addRectangle(int x, int y, int width, int height, String style) {

        svg.append("<rect ");
        svg.append("x='").append(x).append("' ");
        svg.append("y='").append(y).append("' ");
        svg.append("width='").append(width).append("' ");
        svg.append("height='").append(height).append("' ");
        svg.append("style='").append(style).append("' ");
        svg.append("/>");
    }

    public void addLine(int x1, int y1, int x2, int y2, String style) {

        svg.append("<line ");
        svg.append("x1='").append(x1).append("' ");
        svg.append("y1='").append(y1).append("' ");
        svg.append("x2='").append(x2).append("' ");
        svg.append("y2='").append(y2).append("' ");
        svg.append("style='").append(style).append("' ");
        svg.append("/>");
    }

    public void addArrow(int x1, int y1, int x2, int y2, String style) {

        svg.append("<line ");
        svg.append("x1='").append(x1).append("' ");
        svg.append("y1='").append(y1).append("' ");
        svg.append("x2='").append(x2).append("' ");
        svg.append("y2='").append(y2).append("' ");
        svg.append("style='").append(style).append("' ");
        svg.append("marker-start='url(#beginArrow)' ");
        svg.append("marker-end='url(#endArrow)' ");
        svg.append("/>");
    }

    public void addText(int x, int y, String text) {

        svg.append("<text ");
        svg.append("x='").append(x).append("' ");
        svg.append("y='").append(y).append("' ");
        svg.append("text-anchor='middle' ");
        svg.append("style='fill:black; font-size:18px;'>");
        svg.append(text);
        svg.append("</text>");
    }

    public void addRotatedText(int x, int y, int rotation, String text) {

        svg.append("<text ");
        svg.append("x='").append(x).append("' ");
        svg.append("y='").append(y).append("' ");
        svg.append("text-anchor='middle' ");
        svg.append("transform='rotate(").append(rotation).append(" ").append(x).append(" ").append(y).append(")' ");
        svg.append("style='fill:black; font-size:18px;'>");
        svg.append(text);
        svg.append("</text>");
    }

    @Override
    public String toString() {
        return svg.toString() + "</svg>";
    }
}