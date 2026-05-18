package app.persistence;

import app.entities.Quote;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuotesMapper {

    private ConnectionPool connectionPool;

    public QuotesMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    // Creates a new quote in the database
    public Quote createQuote(Quote quote) {
        String sql = """
                INSERT INTO quotes
                (request_id, seller_id, total_price, status, seller_comment, valid_until, sent_at)
                VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, quote.getRequestId());
            preparedStatement.setInt(2, quote.getSellerId());
            preparedStatement.setBigDecimal(3, quote.getTotalPrice());
            preparedStatement.setString(4, quote.getStatus());
            preparedStatement.setString(5, quote.getSellerComment());
            preparedStatement.setDate(6, Date.valueOf(quote.getValidUntil()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int newQuoteId = generatedKeys.getInt(1);
                return getQuoteById(newQuoteId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not create quote", sqle);
        }

        return null;
    }


    // Gets one quote by quote_id
    public Quote getQuoteById(int quoteId) {
        String sql = """
                SELECT *
                FROM quotes
                WHERE quote_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, quoteId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int requestId = rs.getInt("request_id");
                int sellerId = rs.getInt("seller_id");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                String status = rs.getString("status");
                String sellerComment = rs.getString("seller_comment");

                Date validUntilDate = rs.getDate("valid_until");
                LocalDate validUntil = validUntilDate != null ? validUntilDate.toLocalDate() : null;

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;

                Timestamp sentTimestamp = rs.getTimestamp("sent_at");
                Timestamp acceptedTimestamp = rs.getTimestamp("accepted_at");
                Timestamp rejectedTimestamp = rs.getTimestamp("rejected_at");

                LocalDateTime sentAt = sentTimestamp != null ? sentTimestamp.toLocalDateTime() : null;
                LocalDateTime acceptedAt = acceptedTimestamp != null ? acceptedTimestamp.toLocalDateTime() : null;
                LocalDateTime rejectedAt = rejectedTimestamp != null ? rejectedTimestamp.toLocalDateTime() : null;

                return new Quote(
                        quoteId,
                        requestId,
                        sellerId,
                        totalPrice,
                        status,
                        sellerComment,
                        validUntil,
                        createdAt,
                        sentAt,
                        acceptedAt,
                        rejectedAt
                );
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get quote by id", sqle);
        }

        return null;
    }


    // Gets the quote connected to a specific carport request
    public Quote getQuoteByRequestId(int requestId) {
        String sql = """
                SELECT *
                FROM quotes
                WHERE request_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, requestId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int quoteId = rs.getInt("quote_id");
                int sellerId = rs.getInt("seller_id");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                String status = rs.getString("status");
                String sellerComment = rs.getString("seller_comment");

                Date validUntilDate = rs.getDate("valid_until");
                LocalDate validUntil = validUntilDate != null ? validUntilDate.toLocalDate() : null;

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;

                Timestamp sentTimestamp = rs.getTimestamp("sent_at");
                Timestamp acceptedTimestamp = rs.getTimestamp("accepted_at");
                Timestamp rejectedTimestamp = rs.getTimestamp("rejected_at");

                LocalDateTime sentAt = sentTimestamp != null ? sentTimestamp.toLocalDateTime() : null;
                LocalDateTime acceptedAt = acceptedTimestamp != null ? acceptedTimestamp.toLocalDateTime() : null;
                LocalDateTime rejectedAt = rejectedTimestamp != null ? rejectedTimestamp.toLocalDateTime() : null;

                return new Quote(
                        quoteId,
                        requestId,
                        sellerId,
                        totalPrice,
                        status,
                        sellerComment,
                        validUntil,
                        createdAt,
                        sentAt,
                        acceptedAt,
                        rejectedAt
                );
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get quote by request id", sqle);
        }

        return null;
    }


    // Marks a quote as sent
    public void sendQuote(int quoteId) {
        String sql = """
                UPDATE quotes
                SET status = 'SENT',
                    sent_at = CURRENT_TIMESTAMP
                WHERE quote_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, quoteId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No quote found with id: " + quoteId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not send quote", sqle);
        }
    }


    // Marks a quote as accepted
    public void acceptQuote(int quoteId) {
        String sql = """
                UPDATE quotes
                SET status = 'ACCEPTED',
                    accepted_at = CURRENT_TIMESTAMP
                WHERE quote_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, quoteId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No quote found with id: " + quoteId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not accept quote", sqle);
        }
    }


    // Marks a quote as rejected
    public void rejectQuote(int quoteId) {
        String sql = """
                UPDATE quotes
                SET status = 'REJECTED',
                    rejected_at = CURRENT_TIMESTAMP
                WHERE quote_id = ?;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, quoteId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No quote found with id: " + quoteId);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not reject quote", sqle);
        }
    }


    // Gets all quotes that belong to one customer
    // ACCEPTED quotes are not shown under "Mine tilbud" anymore
    public List<Quote> getQuotesByUserId(int userId) {
        List<Quote> quotes = new ArrayList<>();

        String sql = """
                SELECT q.*
                FROM quotes q
                JOIN carport_requests cr ON q.request_id = cr.request_id
                WHERE cr.user_id = ?
                AND q.status <> 'ACCEPTED'
                ORDER BY q.created_at DESC;
                """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int quoteId = rs.getInt("quote_id");
                int requestId = rs.getInt("request_id");
                int sellerId = rs.getInt("seller_id");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                String status = rs.getString("status");
                String sellerComment = rs.getString("seller_comment");

                Date validUntilDate = rs.getDate("valid_until");
                LocalDate validUntil = validUntilDate != null ? validUntilDate.toLocalDate() : null;

                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;

                Timestamp sentTimestamp = rs.getTimestamp("sent_at");
                Timestamp acceptedTimestamp = rs.getTimestamp("accepted_at");
                Timestamp rejectedTimestamp = rs.getTimestamp("rejected_at");

                LocalDateTime sentAt = sentTimestamp != null ? sentTimestamp.toLocalDateTime() : null;
                LocalDateTime acceptedAt = acceptedTimestamp != null ? acceptedTimestamp.toLocalDateTime() : null;
                LocalDateTime rejectedAt = rejectedTimestamp != null ? rejectedTimestamp.toLocalDateTime() : null;

                Quote quote = new Quote(
                        quoteId,
                        requestId,
                        sellerId,
                        totalPrice,
                        status,
                        sellerComment,
                        validUntil,
                        createdAt,
                        sentAt,
                        acceptedAt,
                        rejectedAt
                );

                quotes.add(quote);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException("Could not get quotes by user id", sqle);
        }

        return quotes;
    }
}