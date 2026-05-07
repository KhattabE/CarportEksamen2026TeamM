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

    // creates a new user in the database
    public void createUser(User user) {
        String sql = """
        INSERT INTO users (first_name, last_name, email, password_hash, role) VALUES (?,?,?,?,?)
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPasswordHash());
            preparedStatement.setString(5, user.getRole());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // creates a new user in the database - updated this but it is not final yet
    public void createUser2(User user) {
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
            preparedStatement.setString(3, user.getEmail());
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


    // finds a user by email (used for login)
    public User getUserByEmail(String email) {
        String sql = """
        SELECT * FROM users WHERE email = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String userEmail = rs.getString("email");
                String passwordHash = rs.getString("password_hash");
                String role = rs.getString("role");

                return new User(id, firstName, lastName, userEmail, passwordHash, role);
            }

        } catch (SQLException e) {
            System.out.println(sql);
        }

        return null;
    }


    // checks if login is valid (email + password)
    public User validateLogin(String email, String password) {
        String sql = """
        SELECT * FROM users WHERE email = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String passwordHash = rs.getString("password_hash");

                // simple check (later you can hash properly)
                if (passwordHash.equals(password)) {
                    int id = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String userEmail = rs.getString("email");
                    String role = rs.getString("role");

                    return new User(id, firstName, lastName, userEmail, passwordHash, role);
                }
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }


    // gets a user by id (used for sessions)
    public User getUserById(int userId) {
        String sql = """
        SELECT * FROM users WHERE user_id = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String passwordHash = rs.getString("password_hash");
                String role = rs.getString("role");

                return new User(id, firstName, lastName, email, passwordHash, role);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }
}
