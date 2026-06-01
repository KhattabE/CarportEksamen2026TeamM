package app.services;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Svg {

    private final StringBuilder svg;

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
        svg.append(escapeXml(text));
        svg.append("</text>");
    }

    public void addRotatedText(int x, int y, int rotation, String text) {

        svg.append("<text ");
        svg.append("x='").append(x).append("' ");
        svg.append("y='").append(y).append("' ");
        svg.append("text-anchor='middle' ");
        svg.append("transform='rotate(").append(rotation).append(" ").append(x).append(" ").append(y).append(")' ");
        svg.append("style='fill:black; font-size:18px;'>");
        svg.append(escapeXml(text));
        svg.append("</text>");
    }

    public byte[] toPdfBytes() throws IOException, TranscoderException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDFTranscoder transcoder = new PDFTranscoder();
            TranscoderInput input = new TranscoderInput(new StringReader(toString()));
            TranscoderOutput output = new TranscoderOutput(outputStream);

            transcoder.transcode(input, output);
            return outputStream.toByteArray();
        }
    }

    public void saveAsPdf(Path outputPath) throws IOException, TranscoderException {
        Files.write(outputPath, toPdfBytes());
    }

    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    @Override
    public String toString() {
        return svg.toString() + "</svg>";
    }
}
