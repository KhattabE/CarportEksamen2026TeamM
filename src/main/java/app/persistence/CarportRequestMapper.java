package app.persistence;

import app.entities.Carport;
import app.entities.CarportRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarportRequestMapper {

    private ConnectionPool connectionPool;

    public CarportRequestMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Creates a new carport request in the database.
    public CarportRequest createCarportRequest(CarportRequest carportRequest) {
        String sql = """
                INSERT INTO carport_requests
                (user_id, width_cm, length_cm, height_cm, has_shed,
                 shed_width_cm, shed_length_cm, roof_type, status, customer_comment, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', ?, CURRENT_TIMESTAMP);
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            Carport carport = carportRequest.getCarport();

            preparedStatement.setInt(1, carportRequest.getUserId());
            preparedStatement.setInt(2, carport.getWidthCm());
            preparedStatement.setInt(3, carport.getLengthCm());
            preparedStatement.setInt(4, carport.getHeightCm());
            preparedStatement.setBoolean(5, carport.isHasShed());

            // If there is no shed, these can just be 0 in your current Carport class
            preparedStatement.setInt(6, carport.getShedWidthCm());
            preparedStatement.setInt(7, carport.getShedLengthCm());

            preparedStatement.setString(8, carport.getRoofType());
            preparedStatement.setString(9, carportRequest.getCustomerComment());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int newRequestId = generatedKeys.getInt(1);

                return getCarportRequestById(newRequestId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not create carport request", sqle);
        }

        return null;
    }


    // Gets one specific carport request by request_id.
    public CarportRequest getCarportRequestById(int requestId) {
        String sql = """
                SELECT * FROM carport_requests
                WHERE request_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, requestId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                int widthCm = rs.getInt("width_cm");
                int lengthCm = rs.getInt("length_cm");
                int heightCm = rs.getInt("height_cm");
                boolean hasShed = rs.getBoolean("has_shed");

                int shedWidthCm = rs.getInt("shed_width_cm");
                int shedLengthCm = rs.getInt("shed_length_cm");

                String roofType = rs.getString("roof_type");
                String status = rs.getString("status");
                String customerComment = rs.getString("customer_comment");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Carport carport = new Carport(widthCm, lengthCm, heightCm, hasShed, shedWidthCm, shedLengthCm, roofType);

                return new CarportRequest(requestId, userId, carport, status, customerComment, createdAt);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get carport request by id", sqle);
        }

        return null;
    }


    // Gets all carport requests from one customer.
    public List<CarportRequest> getCarportRequestsByUserId(int userId) {
        List<CarportRequest> carportRequests = new ArrayList<>();

        String sql = """
                SELECT * FROM carport_requests
                WHERE user_id = ?
                ORDER BY request_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int requestId = rs.getInt("request_id");

                int widthCm = rs.getInt("width_cm");
                int lengthCm = rs.getInt("length_cm");
                int heightCm = rs.getInt("height_cm");
                boolean hasShed = rs.getBoolean("has_shed");

                int shedWidthCm = rs.getInt("shed_width_cm");
                int shedLengthCm = rs.getInt("shed_length_cm");

                String roofType = rs.getString("roof_type");
                String status = rs.getString("status");
                String customerComment = rs.getString("customer_comment");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Carport carport = new Carport(
                        widthCm,
                        lengthCm,
                        heightCm,
                        hasShed,
                        shedWidthCm,
                        shedLengthCm,
                        roofType
                );

                CarportRequest carportRequest = new CarportRequest(requestId, userId, carport, status, customerComment, createdAt);

                carportRequests.add(carportRequest);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get carport requests by user id", sqle);
        }

        return carportRequests;
    }


    // Gets all carport requests.
    public List<CarportRequest> getAllCarportRequests() {
        List<CarportRequest> carportRequests = new ArrayList<>();

        String sql = """
                SELECT * FROM carport_requests
                ORDER BY request_id;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                int userId = rs.getInt("user_id");

                int widthCm = rs.getInt("width_cm");
                int lengthCm = rs.getInt("length_cm");
                int heightCm = rs.getInt("height_cm");
                boolean hasShed = rs.getBoolean("has_shed");

                int shedWidthCm = rs.getInt("shed_width_cm");
                int shedLengthCm = rs.getInt("shed_length_cm");

                String roofType = rs.getString("roof_type");
                String status = rs.getString("status");
                String customerComment = rs.getString("customer_comment");

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Carport carport = new Carport(widthCm, lengthCm, heightCm, hasShed, shedWidthCm, shedLengthCm, roofType);

                CarportRequest carportRequest = new CarportRequest(requestId, userId, carport, status, customerComment, createdAt);

                carportRequests.add(carportRequest);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get all carport requests", sqle);
        }

        return carportRequests;
    }


    // Updates the status of a carport request.
    public void updateRequestStatus(int requestId, String status) {
        String sql = """
                UPDATE carport_requests
                SET status = ?
                WHERE request_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, requestId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No carport request found with id: " + requestId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not update carport request status", sqle);
        }
    }
}