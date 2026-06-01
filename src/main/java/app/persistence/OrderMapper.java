package app.persistence;

import app.entities.Order;
import app.entities.ProfileOrder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private ConnectionPool connectionPool;

    public OrderMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Creates a paid order from an accepted quote.
    public Order createOrder(int quoteId) {
        String sql = """ 
               INSERT INTO orders (quote_id, order_status, payment_status, created_at, paid_at) 
               VALUES (?, 'IN_PROGRESS', 'PAID', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 
               """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, quoteId);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int newOrderId = generatedKeys.getInt(1);
                return getOrderById(newOrderId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not create order", sqle);
        }

        return null;
    }


    // Gets one order by order_id.
    public Order getOrderById(int orderId) {
        String sql = """
                     SELECT * FROM orders 
                      WHERE order_id = ?; 
                     """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, orderId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int quoteId = rs.getInt("quote_id");
                String orderStatus = rs.getString("order_status");
                String paymentStatus = rs.getString("payment_status");

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                Timestamp paidTimestamp = rs.getTimestamp("paid_at");

                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;
                LocalDateTime paidAt = paidTimestamp != null ? paidTimestamp.toLocalDateTime() : null;

                return new Order(orderId, quoteId, orderStatus, paymentStatus, createdAt, paidAt);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get order by id", sqle);
        }

        return null;
    }


    // Gets the order connected to a specific quote.
    public Order getOrderByQuoteId(int quoteId) {
        String sql = """
                SELECT * FROM orders 
                WHERE quote_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, quoteId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderStatus = rs.getString("order_status");
                String paymentStatus = rs.getString("payment_status");

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                Timestamp paidTimestamp = rs.getTimestamp("paid_at");

                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;
                LocalDateTime paidAt = paidTimestamp != null ? paidTimestamp.toLocalDateTime() : null;

                return new Order(orderId, quoteId, orderStatus, paymentStatus, createdAt, paidAt);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get order by quote id", sqle);
        }

        return null;
    }


    // Marks an existing order as paid.
    public void markOrderAsPaid(int orderId) {
        String sql = """
                UPDATE orders SET payment_status = 'PAID', order_status = 'IN_PROGRESS', paid_at = CURRENT_TIMESTAMP 
                WHERE order_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, orderId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No order found with id: " + orderId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not mark order as paid", sqle);
        }
    }

    // Gets all profile orders for one customer.
    public List<ProfileOrder> getProfileOrdersByUserId(int userId) {
        List<ProfileOrder> profileOrders = new ArrayList<>();

        String sql = """
                SELECT o.order_id, o.quote_id, o.order_status, o.payment_status, o.created_at, o.paid_at, q.total_price, cr.width_cm, cr.length_cm, cr.height_cm, cr.has_shed 
                FROM orders o 
                JOIN quotes q ON o.quote_id = q.quote_id 
                JOIN carport_requests cr ON q.request_id = cr.request_id
                WHERE cr.user_id = ? 
                ORDER BY o.created_at DESC;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int quoteId = rs.getInt("quote_id");
                String orderStatus = rs.getString("order_status");
                String paymentStatus = rs.getString("payment_status");

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                Timestamp paidTimestamp = rs.getTimestamp("paid_at");

                BigDecimal totalPrice = rs.getBigDecimal("total_price");

                int widthCm = rs.getInt("width_cm");
                int lengthCm = rs.getInt("length_cm");
                int heightCm = rs.getInt("height_cm");
                boolean hasShed = rs.getBoolean("has_shed");

                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;
                LocalDateTime paidAt = paidTimestamp != null ? paidTimestamp.toLocalDateTime() : null;

                ProfileOrder profileOrder = new ProfileOrder(orderId, quoteId, totalPrice, orderStatus, paymentStatus, createdAt, paidAt, widthCm, lengthCm, heightCm, hasShed);

                profileOrders.add(profileOrder);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get profile orders by user id", sqle);
        }

        return profileOrders;
    }
}