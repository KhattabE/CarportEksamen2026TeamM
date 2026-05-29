package app.persistence;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Quote;
import app.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuotesMapperIntegrationTest {

    private static UserMapper userMapper;
    private static CarportRequestMapper carportRequestMapper;
    private static QuotesMapper quotesMapper;

    @BeforeAll
    static void setup() {
        ConnectionPool connectionPool = Main.getConnectionPool();
        userMapper = new UserMapper(connectionPool);
        carportRequestMapper = new CarportRequestMapper(connectionPool);
        quotesMapper = new QuotesMapper(connectionPool);
    }

    // Tests that a quote can be created and found again by quote id
    @Test
    void testCreateQuoteAndGetById() {
        CarportRequest request = createTestCarportRequest();

        Quote quote = new Quote(request.getRequestId(), request.getUserId(), new BigDecimal("9999.95"), "SENT", "Integration test quote", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);
        Quote foundQuote = quotesMapper.getQuoteById(createdQuote.getQuoteId());

        assertNotNull(createdQuote);
        assertNotNull(foundQuote);
        assertTrue(createdQuote.getQuoteId() > 0);
        assertEquals(createdQuote.getQuoteId(), foundQuote.getQuoteId());
        assertEquals(request.getRequestId(), foundQuote.getRequestId());
        assertEquals(request.getUserId(), foundQuote.getSellerId());
        assertEquals(0, new BigDecimal("9999.95").compareTo(foundQuote.getTotalPrice()));
        assertEquals("SENT", foundQuote.getStatus());
        assertEquals("Integration test quote", foundQuote.getSellerComment());
        assertNotNull(foundQuote.getCreatedAt());
    }

    // Tests that a quote can be found by request id
    @Test
    void testGetQuoteByRequestId() {
        CarportRequest request = createTestCarportRequest();

        Quote quote = new Quote(request.getRequestId(), request.getUserId(), new BigDecimal("8888.95"), "SENT", "Quote by request id test", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);
        Quote foundQuote = quotesMapper.getQuoteByRequestId(request.getRequestId());

        assertNotNull(createdQuote);
        assertNotNull(foundQuote);
        assertEquals(createdQuote.getQuoteId(), foundQuote.getQuoteId());
        assertEquals(request.getRequestId(), foundQuote.getRequestId());
        assertEquals("SENT", foundQuote.getStatus());
    }

    // Tests that a quote can be accepted
    @Test
    void testAcceptQuoteChangesStatus() {
        CarportRequest request = createTestCarportRequest();

        Quote quote = new Quote(request.getRequestId(), request.getUserId(), new BigDecimal("7777.95"), "SENT", "Accept quote test", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);

        quotesMapper.acceptQuote(createdQuote.getQuoteId());

        Quote acceptedQuote = quotesMapper.getQuoteById(createdQuote.getQuoteId());

        assertNotNull(acceptedQuote);
        assertEquals("ACCEPTED", acceptedQuote.getStatus());
        assertNotNull(acceptedQuote.getAcceptedAt());
    }

    // Tests that a quote can be rejected
    @Test
    void testRejectQuoteChangesStatus() {
        CarportRequest request = createTestCarportRequest();

        Quote quote = new Quote(request.getRequestId(), request.getUserId(), new BigDecimal("6666.95"), "SENT", "Reject quote test", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);

        quotesMapper.rejectQuote(createdQuote.getQuoteId());

        Quote rejectedQuote = quotesMapper.getQuoteById(createdQuote.getQuoteId());

        assertNotNull(rejectedQuote);
        assertEquals("REJECTED", rejectedQuote.getStatus());
        assertNotNull(rejectedQuote.getRejectedAt());
    }

    // Tests that quotes for one customer can be loaded
    @Test
    void testGetQuotesByUserId() {
        CarportRequest request = createTestCarportRequest();

        Quote quote = new Quote(request.getRequestId(), request.getUserId(), new BigDecimal("5555.95"), "SENT", "Quote list test", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);

        List<Quote> quotes = quotesMapper.getQuotesByUserId(request.getUserId());

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());

        boolean foundQuote = false;

        for (Quote q : quotes) {
            if (q.getQuoteId() == createdQuote.getQuoteId()) {
                foundQuote = true;
            }
        }

        assertTrue(foundQuote);
    }

    private CarportRequest createTestCarportRequest() {
        String uniqueEmail = "quoteuser_" + UUID.randomUUID() + "@test.dk";

        User user = new User(0, "Quote", "User", uniqueEmail, "1234", "12345678", "Testvej 20", "2800", "Lyngby", "customer", null);

        userMapper.createUser(user);

        User createdUser = userMapper.getUserByEmail(uniqueEmail);

        Carport carport = new Carport(300, 600, 230, true, 200, 300, "FLAT");

        CarportRequest request = new CarportRequest(0, createdUser.getUserId(), carport, null, "Quote integration request", null);

        return carportRequestMapper.createCarportRequest(request);
    }
}