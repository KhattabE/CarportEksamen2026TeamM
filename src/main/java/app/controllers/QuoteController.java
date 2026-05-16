package app.controllers;

import app.Main;
import app.entities.CarportRequest;
import app.entities.Material;
import app.entities.Quote;
import app.entities.QuoteMaterialLine;
import app.entities.User;
import app.persistence.CarportRequestMapper;
import app.persistence.MaterialMapper;
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

        Quote quote = new Quote(
                requestId,
                currentUser.getUserId(),
                result.getTotalPrice(),
                "SENT",
                "Automatisk genereret tilbud",
                LocalDate.now().plusDays(14)
        );

        Quote createdQuote = quotesMapper.createQuote(quote);

        List<QuoteMaterialLine> quoteMaterialLines = result.createQuoteMaterialLines(createdQuote.getQuoteId());

        quoteMaterialLineMapper.addQuoteMaterialLines(quoteMaterialLines);

        carportRequestMapper.updateRequestStatus(requestId, "QUOTE_SENT");

        ctx.redirect("/admin/requests");
    }
}