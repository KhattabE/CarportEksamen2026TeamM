package app.controllers;

import app.Main;
import app.persistence.QuotesMapper;
import io.javalin.http.Context;

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
}