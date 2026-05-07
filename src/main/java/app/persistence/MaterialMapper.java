package app.persistence;

import app.entities.Material;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {

    private ConnectionPool connectionPool;

    public MaterialMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Gets all materials from the database.
    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();

        String sql = """
                SELECT * FROM materials
                ORDER BY material_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {

            // Goes through every row returned from the database
            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String unit = rs.getString("unit");
                BigDecimal pricePerUnit = rs.getBigDecimal("price_per_unit");
                boolean active = rs.getBoolean("active");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                // Creates a Material object from the database row
                Material material = new Material(materialId, name, description, category, unit, pricePerUnit, active, createdAt, updatedAt);

                // Adds the material object to the list
                materials.add(material);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get all materials", sqle);
        }

        return materials;
    }


    // Gets only active materials from the database.
    public List<Material> getActiveMaterials() {
        List<Material> materials = new ArrayList<>();

        String sql = """
                SELECT * FROM materials
                WHERE active = true
                ORDER BY material_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {

            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String unit = rs.getString("unit");
                BigDecimal pricePerUnit = rs.getBigDecimal("price_per_unit");
                boolean active = rs.getBoolean("active");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                Material material = new Material(materialId, name, description, category, unit, pricePerUnit, active, createdAt, updatedAt);

                materials.add(material);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get active materials", sqle);
        }

        return materials;
    }


    // Gets one specific material by its material_id.
    public Material getMaterialById(int materialId) {
        String sql = """
                SELECT * FROM materials
                WHERE material_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            // Sets the material_id in the SQL query
            preparedStatement.setInt(1, materialId);

            ResultSet rs = preparedStatement.executeQuery();

            // If a material with this id exists, create and return a Material object
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String unit = rs.getString("unit");
                BigDecimal pricePerUnit = rs.getBigDecimal("price_per_unit");
                boolean active = rs.getBoolean("active");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                return new Material(materialId, name, description, category, unit, pricePerUnit, active, createdAt, updatedAt);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get material by id", sqle);
        }


        return null;
    }


    // Updates only the price of a material.
    public void updateMaterialPrice(int materialId, BigDecimal newPrice) {
        String sql = """
                UPDATE materials
                SET price_per_unit = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE material_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            // Sets the new price and chooses which material to update
            preparedStatement.setBigDecimal(1, newPrice);
            preparedStatement.setInt(2, materialId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No material found with id: " + materialId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not update material price", sqle);
        }
    }



    // Gets all materials from one specific category.
    public List<Material> getMaterialsByCategory(String category) {
        List<Material> materials = new ArrayList<>();

        String sql = """
                SELECT * FROM materials
                WHERE category = ?
                ORDER BY material_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {


            preparedStatement.setString(1, category);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String materialCategory = rs.getString("category");
                String unit = rs.getString("unit");
                BigDecimal pricePerUnit = rs.getBigDecimal("price_per_unit");
                boolean active = rs.getBoolean("active");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                Material material = new Material(materialId, name, description, materialCategory, unit, pricePerUnit, active, createdAt, updatedAt);

                materials.add(material);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get materials by category", sqle);
        }

        return materials;
    }
}