package app.persistence;

import app.entities.QuoteMaterialLine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuoteMaterialLineMapper {

    private ConnectionPool connectionPool;

    public QuoteMaterialLineMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Adds multiple material lines to one quote.
    public void addQuoteMaterialLines(List<QuoteMaterialLine> quoteMaterialLines) {
        String sql = """
                INSERT INTO quote_material_lines
                (quote_id, material_id, length_cm, quantity, unit_price, unit, usage_description)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            for (QuoteMaterialLine quoteMaterialLine : quoteMaterialLines) {
                preparedStatement.setInt(1, quoteMaterialLine.getQuoteId());
                preparedStatement.setInt(2, quoteMaterialLine.getMaterialId());
                preparedStatement.setObject(3, quoteMaterialLine.getLengthCm());
                preparedStatement.setBigDecimal(4, quoteMaterialLine.getQuantity());
                preparedStatement.setBigDecimal(5, quoteMaterialLine.getUnitPrice());
                preparedStatement.setString(6, quoteMaterialLine.getUnit());
                preparedStatement.setString(7, quoteMaterialLine.getUsageDescription());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not add quote material lines", sqle);
        }
    }


    // Gets all material lines for one quote.
    public List<QuoteMaterialLine> getQuoteMaterialLinesByQuoteId(int quoteId) {
        List<QuoteMaterialLine> quoteMaterialLines = new ArrayList<>();

        String sql = """
                SELECT * FROM quote_material_lines
                WHERE quote_id = ?
                ORDER BY quote_line_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, quoteId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int quoteLineId = rs.getInt("quote_line_id");
                int materialId = rs.getInt("material_id");
                Integer lengthCm = (Integer) rs.getObject("length_cm");
                BigDecimal quantity = rs.getBigDecimal("quantity");
                BigDecimal unitPrice = rs.getBigDecimal("unit_price");
                String unit = rs.getString("unit");
                String usageDescription = rs.getString("usage_description");

                QuoteMaterialLine quoteMaterialLine = new QuoteMaterialLine(quoteLineId, quoteId, materialId, lengthCm, quantity, unitPrice, unit, usageDescription);

                quoteMaterialLines.add(quoteMaterialLine);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get quote material lines by quote id", sqle);
        }

        return quoteMaterialLines;
    }
}