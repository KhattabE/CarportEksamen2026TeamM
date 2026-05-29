package app.persistence;

import app.Main;
import app.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperIntegrationTest {

    private static UserMapper userMapper;

    @BeforeAll
    static void setup() {
        userMapper = new UserMapper(Main.getConnectionPool());
    }

    // Tests that a new user can be created and found again by email
    @Test
    void testCreateUserAndGetUserByEmail() {
        String uniqueEmail = "testuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Test", "User", uniqueEmail, "1234", "12345678", "Testvej 1", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User foundUser = userMapper.getUserByEmail(uniqueEmail);

        assertNotNull(foundUser);
        assertEquals(uniqueEmail.toLowerCase(), foundUser.getEmail());
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());
        assertEquals("customer", foundUser.getRole());
    }

    // Tests that getUserByEmail works even if the email has different casing
    @Test
    void testGetUserByEmailIgnoresCase() {
        String uniqueEmail = "caseuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Case", "User", uniqueEmail, "1234", "12345678", "Testvej 2", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User foundUser = userMapper.getUserByEmail(uniqueEmail.toUpperCase());

        assertNotNull(foundUser);
        assertEquals(uniqueEmail.toLowerCase(), foundUser.getEmail());
        assertEquals("Case", foundUser.getFirstName());
    }

    // Tests that a user can be found by phone number
    @Test
    void testGetUserByPhoneReturnsCorrectUser() {
        String uniqueEmail = "phoneuser_" + UUID.randomUUID() + "@test.dk";
        String uniquePhone = "99" + UUID.randomUUID().toString().substring(0, 6);

        User user = new User(0, "Phone", "User", uniqueEmail, "1234", uniquePhone, "Testvej 3", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User foundUser = userMapper.getUserByPhone(uniquePhone);

        assertNotNull(foundUser);
        assertEquals(uniquePhone, foundUser.getPhone());
        assertEquals(uniqueEmail.toLowerCase(), foundUser.getEmail());
    }

    // Tests that a user can be found by user_id after being created
    @Test
    void testGetUserByIdReturnsCorrectUser() {
        String uniqueEmail = "userid_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Id", "User", uniqueEmail, "1234", "12345678", "Testvej 4", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User foundByEmail = userMapper.getUserByEmail(uniqueEmail);

        assertNotNull(foundByEmail);

        User foundById = userMapper.getUserById(foundByEmail.getUserId());

        assertNotNull(foundById);
        assertEquals(foundByEmail.getUserId(), foundById.getUserId());
        assertEquals(uniqueEmail.toLowerCase(), foundById.getEmail());
    }

    // Tests that null is returned when no user exists with that email
    @Test
    void testGetUserByEmailReturnsNullWhenUserDoesNotExist() {
        String uniqueEmail = "doesnotexist_" + UUID.randomUUID() + "@test.dk";

        User foundUser = userMapper.getUserByEmail(uniqueEmail);

        assertNull(foundUser);
    }
}