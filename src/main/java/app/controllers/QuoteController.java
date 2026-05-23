package app.controllers;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Material;
import app.entities.Order;
import app.entities.Quote;
import app.entities.User;
import app.persistence.CarportRequestMapper;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.QuotesMapper;
import app.services.CarportCalculationResult;
import app.services.CarportCalculator;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

public class QuoteController {

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

        quotesMapper.acceptQuote(quoteId);

        Order existingOrder = orderMapper.getOrderByQuoteId(quoteId);

        if (existingOrder == null) {
            orderMapper.createOrder(quoteId);
        } else if (!"PAID".equals(existingOrder.getPaymentStatus())) {
            orderMapper.markOrderAsPaid(existingOrder.getOrderId());
        }

        ctx.redirect("/profile");
    }

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

        CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(requestId);

        if (carportRequest == null) {
            ctx.redirect("/admin/requests");
            return;
        }

        List<Material> materials = materialMapper.getActiveMaterials();

        CarportCalculator calculator = new CarportCalculator();

        CarportCalculationResult result = calculator.calculate(
                carportRequest.getCarport(),
                materials
        );

        Quote quote = new Quote(
                requestId,
                currentUser.getUserId(),
                result.getTotalPrice(),
                "SENT",
                "Automatisk genereret tilbud",
                LocalDate.now().plusDays(14)
        );

        quotesMapper.createQuote(quote);

        carportRequestMapper.updateRequestStatus(requestId, "QUOTE_SENT");

        ctx.redirect("/admin/requests");
    }

    public static String getPreviewImage(Carport carport) {
        String roofType = carport.getRoofType();
        boolean hasShed = carport.isHasShed();

        int width = carport.getWidthCm();
        boolean isDouble = width > 300;

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

    public static void rejectRequest(Context ctx) {
        String requestIdString = ctx.formParam("requestId");

        if (requestIdString == null || requestIdString.isBlank()) {
            ctx.redirect("/admin/requests");
            return;
        }

        int requestId = Integer.parseInt(requestIdString);

        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());

        carportRequestMapper.updateRequestStatus(requestId, "AFVIST");

        ctx.redirect("/admin/requests");
    }
}