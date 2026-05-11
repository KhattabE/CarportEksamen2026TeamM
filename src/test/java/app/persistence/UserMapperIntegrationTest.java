package app.persistence;

import app.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperIntegrationTest {

    private static UserMapper userMapper;

    @BeforeAll
    static void setup() {
        String user = "postgres";

        // Backslash skal skrives som \\ i Java strings
        String password = "}DPU2Y-h(=T£4E`F\\e6j@i]`n:mX,5g";

        String url = "jdbc:postgresql://134.209.233.109:5432/%s?currentSchema=public";
        String db = "carport";

        ConnectionPool connectionPool = ConnectionPool.getInstance(user, password, url, db);
        userMapper = new UserMapper(connectionPool);
    }


    // Tests that a new user can be created and found again by email
    @Test
    void testCreateUserAndGetUserByEmail() {
        String uniqueEmail = "testuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Test", "User", uniqueEmail, "1234", "12345678", "Testvej 1", "2800", "Lyngby", "customer", null);

        userMapper.createUser2(user);

        User foundUser = userMapper.getUserByEmail(uniqueEmail);

        assertNotNull(foundUser);
        assertEquals(uniqueEmail, foundUser.getEmail());
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());
        assertEquals("customer", foundUser.getRole());
    }


    // Tests that login works when email and password are correct
    @Test
    void testValidateLoginReturnsUserWhenPasswordIsCorrect() {
        String uniqueEmail = "loginuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Login", "User", uniqueEmail, "1234", "12345678", "Testvej 2", "2800", "Lyngby", "customer", null);

        userMapper.createUser2(user);

        User loggedInUser = userMapper.validateLogin(uniqueEmail, "1234");

        assertNotNull(loggedInUser);
        assertEquals(uniqueEmail, loggedInUser.getEmail());
        assertEquals("Login", loggedInUser.getFirstName());
    }


    // Tests that login fails when the password is wrong
    @Test
    void testValidateLoginReturnsNullWhenPasswordIsWrong() {
        String uniqueEmail = "wrongpassword_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Wrong", "Password", uniqueEmail, "1234", "12345678", "Testvej 3", "2800", "Lyngby", "customer", null);

        userMapper.createUser2(user);

        User loggedInUser = userMapper.validateLogin(uniqueEmail, "wrong-password");

        assertNull(loggedInUser);
    }


    // Tests that a user can be found by user_id after being created
    @Test
    void testGetUserByIdReturnsCorrectUser() {
        String uniqueEmail = "userid_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Id", "User", uniqueEmail, "1234", "12345678", "Testvej 4", "2800", "Lyngby", "customer", null);

        userMapper.createUser2(user);

        User foundByEmail = userMapper.getUserByEmail(uniqueEmail);

        assertNotNull(foundByEmail);

        User foundById = userMapper.getUserById(foundByEmail.getUserId());

        assertNotNull(foundById);
        assertEquals(foundByEmail.getUserId(), foundById.getUserId());
        assertEquals(uniqueEmail, foundById.getEmail());
    }
}