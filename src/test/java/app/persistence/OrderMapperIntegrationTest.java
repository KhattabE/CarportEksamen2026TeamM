package app.persistence;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Order;
import app.entities.ProfileOrder;
import app.entities.Quote;
import app.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperIntegrationTest {

    private static UserMapper userMapper;
    private static CarportRequestMapper carportRequestMapper;
    private static QuotesMapper quotesMapper;
    private static OrderMapper orderMapper;

    @BeforeAll
    static void setup() {
        ConnectionPool connectionPool = Main.getConnectionPool();
        userMapper = new UserMapper(connectionPool);
        carportRequestMapper = new CarportRequestMapper(connectionPool);
        quotesMapper = new QuotesMapper(connectionPool);
        orderMapper = new OrderMapper(connectionPool);
    }

    // Tests that an order can be created from a quote
    @Test
    void testCreateOrderFromQuote() {
        Quote quote = createTestQuote();

        Order order = orderMapper.createOrder(quote.getQuoteId());

        assertNotNull(order);
        assertTrue(order.getOrderId() > 0);
        assertEquals(quote.getQuoteId(), order.getQuoteId());
        assertNotNull(order.getCreatedAt());
    }

    // Tests that an order can be found by order id
    @Test
    void testGetOrderById() {
        Quote quote = createTestQuote();

        Order createdOrder = orderMapper.createOrder(quote.getQuoteId());
        Order foundOrder = orderMapper.getOrderById(createdOrder.getOrderId());

        assertNotNull(foundOrder);
        assertEquals(createdOrder.getOrderId(), foundOrder.getOrderId());
        assertEquals(quote.getQuoteId(), foundOrder.getQuoteId());
    }

    // Tests that an order can be found by quote id
    @Test
    void testGetOrderByQuoteId() {
        Quote quote = createTestQuote();

        Order createdOrder = orderMapper.createOrder(quote.getQuoteId());
        Order foundOrder = orderMapper.getOrderByQuoteId(quote.getQuoteId());

        assertNotNull(foundOrder);
        assertEquals(createdOrder.getOrderId(), foundOrder.getOrderId());
        assertEquals(quote.getQuoteId(), foundOrder.getQuoteId());
    }

    // Tests that an order can be marked as paid
    @Test
    void testMarkOrderAsPaid() {
        Quote quote = createTestQuote();

        Order createdOrder = orderMapper.createOrder(quote.getQuoteId());

        orderMapper.markOrderAsPaid(createdOrder.getOrderId());

        Order paidOrder = orderMapper.getOrderById(createdOrder.getOrderId());

        assertNotNull(paidOrder);
        assertEquals("PAID", paidOrder.getPaymentStatus());
        assertEquals("IN_PROGRESS", paidOrder.getOrderStatus());
        assertNotNull(paidOrder.getPaidAt());
    }


    // Tests that profile orders for one customer can be loaded
    @Test
    void testGetProfileOrdersByUserId() {
        Quote quote = createTestQuote();

        Order createdOrder = orderMapper.createOrder(quote.getQuoteId());

        CarportRequest request = carportRequestMapper.getCarportRequestById(quote.getRequestId());

        List<ProfileOrder> profileOrders = orderMapper.getProfileOrdersByUserId(request.getUserId());

        assertNotNull(profileOrders);
        assertFalse(profileOrders.isEmpty());

        boolean foundProfileOrder = false;

        for (ProfileOrder profileOrder : profileOrders) {
            if (profileOrder.getOrderId() == createdOrder.getOrderId()) {
                foundProfileOrder = true;
                assertEquals(quote.getQuoteId(), profileOrder.getQuoteId());
                assertEquals(0, quote.getTotalPrice().compareTo(profileOrder.getTotalPrice()));
                assertEquals(300, profileOrder.getWidthCm());
                assertEquals(600, profileOrder.getLengthCm());
                assertEquals(230, profileOrder.getHeightCm());
                assertTrue(profileOrder.isHasShed());
            }
        }

        assertTrue(foundProfileOrder);
    }


    private Quote createTestQuote() {
        String uniqueEmail = "orderuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Order", "User", uniqueEmail, "1234", "12345678", "Testvej 30", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User createdUser = userMapper.getUserByEmail(uniqueEmail);

        Carport carport = new Carport(300, 600, 230, true, 200, 300, "FLAT");

        CarportRequest request = new CarportRequest(0, createdUser.getUserId(), carport, null, "Order integration request", null);

        CarportRequest createdRequest = carportRequestMapper.createCarportRequest(request);

        Quote quote = new Quote(createdRequest.getRequestId(), createdUser.getUserId(), new BigDecimal("5555.95"), "ACCEPTED", "Order integration quote", LocalDate.now().plusDays(14));

        return quotesMapper.createQuote(quote);
    }
}