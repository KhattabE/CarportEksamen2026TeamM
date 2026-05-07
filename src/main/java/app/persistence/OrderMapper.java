package app.persistence;

import app.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderMapper {

    private ConnectionPool connectionPool;

    public OrderMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Creates a new order from an accepted quote.
    // This should be used after the customer accepts a quote.
    // The order starts as WAITING_FOR_PAYMENT and UNPAID.
    public Order createOrder(int quoteId) {
        String sql = """
                INSERT INTO orders (quote_id, order_status, payment_status, created_at) VALUES (?, 'WAITING_FOR_PAYMENT', 'UNPAID', CURRENT_TIMESTAMP);
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


    // Gets one order by order_id (useful when you need to show one specific order)
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

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Timestamp paidTimestamp = rs.getTimestamp("paid_at");
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

                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Timestamp paidTimestamp = rs.getTimestamp("paid_at");
                LocalDateTime paidAt = paidTimestamp != null ? paidTimestamp.toLocalDateTime() : null;

                return new Order(orderId, quoteId, orderStatus, paymentStatus, createdAt, paidAt);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get order by quote id", sqle);
        }

        return null;
    }


    // Marks an order as paid.
    public void markOrderAsPaid(int orderId) {
        String sql = """
                UPDATE orders
                SET payment_status = 'PAID',
                    order_status = 'IN_PROGRESS',
                    paid_at = CURRENT_TIMESTAMP
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


    // Updates the order status.
    public void updateOrderStatus(int orderId, String orderStatus) {
        String sql = """
                UPDATE orders
                SET order_status = ?
                WHERE order_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, orderStatus);
            preparedStatement.setInt(2, orderId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No order found with id: " + orderId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not update order status", sqle);
        }
    }
}