package app.controllers;

import app.Main;
import app.entities.*;
import app.persistence.CarportRequestMapper;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.QuoteMaterialLineMapper;
import app.persistence.QuotesMapper;
import app.services.CarportCalculationResult;
import app.services.CarportCalculator;
import io.javalin.http.Context;


import java.time.LocalDate;
import java.util.List;

public class QuoteController {

    // Rejects a quote when the customer clicks the "Afvis" button
    public static void rejectQuote(Context ctx) {
        String quoteIdString = ctx.formParam("quoteId");

        if (quoteIdString == null || quoteIdString.isBlank()) {
            ctx.redirect("/profile");
            return;
        }

        int quoteId = Integer.parseInt(quoteIdString);

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        quotesMapper.rejectQuote(quoteId);

        ctx.redirect("/profile");
    }

    // Accepts a quote, creates an order, and sends the customer back to profile
    public static void acceptQuote(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        String quoteIdString = ctx.formParam("quoteId");

        if (quoteIdString == null || quoteIdString.isBlank()) {
            ctx.redirect("/profile");
            return;
        }

        int quoteId = Integer.parseInt(quoteIdString);

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        OrderMapper orderMapper = new OrderMapper(Main.getConnectionPool());

        Quote quote = quotesMapper.getQuoteById(quoteId);

        if (quote == null) {
            ctx.redirect("/profile");
            return;
        }

        // Marks the quote as accepted
        quotesMapper.acceptQuote(quoteId);

        // Creates a paid order, or updates an existing unpaid order
        Order existingOrder = orderMapper.getOrderByQuoteId(quoteId);

        if (existingOrder == null) {
            orderMapper.createOrder(quoteId);
        } else if (!"PAID".equals(existingOrder.getPaymentStatus())) {
            orderMapper.markOrderAsPaid(existingOrder.getOrderId());
        }

        ctx.redirect("/profile");
    }

    // Creates a quote from a customer's carport request
    public static void createQuoteFromRequest(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        String requestIdString = ctx.formParam("requestId");

        if (requestIdString == null || requestIdString.isBlank()) {
            ctx.redirect("/admin/requests");
            return;
        }

        int requestId = Integer.parseInt(requestIdString);

        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());
        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        QuoteMaterialLineMapper quoteMaterialLineMapper = new QuoteMaterialLineMapper(Main.getConnectionPool());

        CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(requestId);

        if (carportRequest == null) {
            ctx.redirect("/admin/requests");
            return;
        }

        List<Material> materials = materialMapper.getActiveMaterials();

        CarportCalculator calculator = new CarportCalculator();
        CarportCalculationResult result = calculator.calculate(carportRequest.getCarport(), materials);

        Quote quote = new Quote(requestId, currentUser.getUserId(), result.getTotalPrice(), "SENT", "Automatisk genereret tilbud", LocalDate.now().plusDays(14));

        Quote createdQuote = quotesMapper.createQuote(quote);

        List<QuoteMaterialLine> quoteMaterialLines = result.createQuoteMaterialLines(createdQuote.getQuoteId());

        quoteMaterialLineMapper.addQuoteMaterialLines(quoteMaterialLines);

        carportRequestMapper.updateRequestStatus(requestId, "QUOTE_SENT");

        ctx.redirect("/admin/requests");
    }

    public static String getPreviewImage(Carport carport) {
        String roofType = carport.getRoofType();
        boolean hasShed = carport.isHasShed();

        int width = carport.getWidthCm();
        boolean isDouble = width > 300; // instead of carport type

        if (!isDouble && roofType.equals("Fladt tag") && !hasShed) {
            return "/images/enkeltCarportUdenSkurOgUdenRejsning.png";
        } else if (!isDouble && roofType.equals("Fladt tag") && hasShed) {
            return "/images/enkeltCarportMedSkurOgUdenRejsning.png";
        } else if (!isDouble && roofType.equals("Rejsning") && !hasShed) {
            return "/images/enkeltCarportUdenSkurMedRejsning.png";
        } else if (!isDouble && roofType.equals("Rejsning") && hasShed) {
            return "/images/enkeltCarportMedSkurOgMedRejsning.png";
        } else if (isDouble && roofType.equals("Fladt tag") && !hasShed) {
            return "/images/dobbeltCarportUdenSkurOgUdenRejsning.png";
        } else if (isDouble && roofType.equals("Fladt tag") && hasShed) {
            return "/images/dobbeltCarportMedSkurUdenRejsning.png";
        } else if (isDouble && roofType.equals("Rejsning") && !hasShed) {
            return "/images/dobbeltCarportUdenSkurOgRejsning.png";
        } else {
            return "/images/dobbeltCarportMedSkurOgRejsning.png";

        }
    }

}