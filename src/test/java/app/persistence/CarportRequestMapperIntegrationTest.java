package app.persistence;

import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarportRequestMapperIntegrationTest {

//    private static UserMapper userMapper;
//    private static CarportRequestMapper carportRequestMapper;
//
//    @BeforeAll
//    static void setup() {
//        String user = "postgres";
//
//        // Backslash skal skrives som \\ i Java strings
//        String password = "}DPU2Y-h(=T£4E`F\\e6j@i]`n:mX,5g";
//
//        String url = "jdbc:postgresql://134.209.233.109:5432/%s?currentSchema=public";
//        String db = "carport";
//
//        ConnectionPool connectionPool = ConnectionPool.getInstance(user, password, url, db);
//
//        userMapper = new UserMapper(connectionPool);
//        carportRequestMapper = new CarportRequestMapper(connectionPool);
//    }
//
//
//    // Tests that a customer can create a carport request and get it back from the database
//    @Test
//    void testCreateCarportRequestAndGetById() {
//        String uniqueEmail = "requestuser_" + UUID.randomUUID() + "@test.dk";
//
//        User user = new User(0, "Request", "User", uniqueEmail, "1234", "12345678", "Testvej 10", "2800", "Lyngby", "customer", null);
//
//        userMapper.createUser2(user);
//
//        User createdUser = userMapper.getUserByEmail(uniqueEmail);
//
//        assertNotNull(createdUser);
//
//        Carport carport = new Carport(300, 600, 230, true, 200, 300, "FLAT");
//
//        CarportRequest request = new CarportRequest(0, createdUser.getUserId(), carport, null, "Jeg ønsker en carport med skur", null);
//
//        CarportRequest createdRequest = carportRequestMapper.createCarportRequest(request);
//
//        assertNotNull(createdRequest);
//        assertTrue(createdRequest.getRequestId() > 0);
//        assertEquals(createdUser.getUserId(), createdRequest.getUserId());
//        assertEquals("PENDING", createdRequest.getStatus());
//        assertEquals("Jeg ønsker en carport med skur", createdRequest.getCustomerComment());
//
//        Carport createdCarport = createdRequest.getCarport();
//
//        assertEquals(300, createdCarport.getWidthCm());
//        assertEquals(600, createdCarport.getLengthCm());
//        assertEquals(230, createdCarport.getHeightCm());
//        assertTrue(createdCarport.isHasShed());
//        assertEquals(200, createdCarport.getShedWidthCm());
//        assertEquals(300, createdCarport.getShedLengthCm());
//        assertEquals("FLAT", createdCarport.getRoofType());
//    }
//
//
//    // Tests that all carport requests for one specific user can be found
//    @Test
//    void testGetCarportRequestsByUserId() {
//        String uniqueEmail = "requestlist_" + UUID.randomUUID() + "@test.dk";
//
//        User user = new User(0, "RequestList", "User", uniqueEmail, "1234", "12345678", "Testvej 11", "2800", "Lyngby", "customer", null);
//
//        userMapper.createUser2(user);
//
//        User createdUser = userMapper.getUserByEmail(uniqueEmail);
//
//        assertNotNull(createdUser);
//
//        Carport carport = new Carport(350, 700, 240, false, 0, 0, "FLAT");
//
//        CarportRequest request = new CarportRequest(0, createdUser.getUserId(), carport, null, "Test forespørgsel uden skur", null);
//
//        carportRequestMapper.createCarportRequest(request);
//
//        List<CarportRequest> requests = carportRequestMapper.getCarportRequestsByUserId(createdUser.getUserId());
//
//        assertNotNull(requests);
//        assertFalse(requests.isEmpty());
//
//        for (CarportRequest carportRequest : requests) {
//            assertEquals(createdUser.getUserId(), carportRequest.getUserId());
//        }
//    }
//
//
//    // Tests that a seller/admin can update the status of a carport request
//    @Test
//    void testUpdateRequestStatus() {
//        String uniqueEmail = "statusrequest_" + UUID.randomUUID() + "@test.dk";
//
//        User user = new User(0, "Status", "User", uniqueEmail, "1234", "12345678", "Testvej 12", "2800", "Lyngby", "customer", null);
//
//        userMapper.createUser2(user);
//
//        User createdUser = userMapper.getUserByEmail(uniqueEmail);
//
//        assertNotNull(createdUser);
//
//        Carport carport = new Carport(400, 800, 250, true, 250, 300, "FLAT");
//
//        CarportRequest request = new CarportRequest(0, createdUser.getUserId(), carport, null, "Status test request", null);
//
//        CarportRequest createdRequest = carportRequestMapper.createCarportRequest(request);
//
//        carportRequestMapper.updateRequestStatus(createdRequest.getRequestId(), "PROCESSED");
//
//        CarportRequest updatedRequest = carportRequestMapper.getCarportRequestById(createdRequest.getRequestId());
//
//        assertNotNull(updatedRequest);
//        assertEquals("PROCESSED", updatedRequest.getStatus());
//    }
}