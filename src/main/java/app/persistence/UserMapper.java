package app.persistence;

import app.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    private ConnectionPool connectionPool;

    public UserMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void createUser(User user) {
        String sql = """
        INSERT INTO users 
        (first_name, last_name, email, password_hash, phone, address, postal_code, city, role) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail().trim().toLowerCase());
            preparedStatement.setString(4, user.getPasswordHash());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPostalCode());
            preparedStatement.setString(8, user.getCity());
            preparedStatement.setString(9, user.getRole());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("An error has happened: " + sql);
        }
    }

    public User getUserByEmail(String email) {
        String sql = """
        SELECT * FROM users 
        WHERE LOWER(email) = LOWER(?)
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email.trim());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("An error has happened: " + sql);
        }

        return null;
    }

    public User getUserByPhone(String phone) {
        String sql = """
        SELECT * FROM users 
        WHERE phone = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, phone.trim());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("An error has happened: " + sql);
        }

        return null;
    }

    public User getUserById(int userId) {
        String sql = """
        SELECT * FROM users 
        WHERE user_id = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("An error has happened: " + sql);
        }

        return null;
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("postal_code"),
                rs.getString("city"),
                rs.getString("role"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    }
}