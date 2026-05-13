package app.controllers;

import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Quote;
import app.entities.User;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainController {

    public static void index(Context ctx) {
        ctx.render("index-Frontpage.html");
    }

    public static void adminDashboard(Context ctx) {
        ctx.render("admin-dashboard.html");
    }

    public static void adminViewRequests(Context ctx) {
        ctx.render("admin-view-requests.html");
    }

    public static void adminRequestDetails(Context ctx) {
        ctx.render("admin-Request-Details.html");
    }

    public static void adminViewOrders(Context ctx) {
        ctx.render("admin_view_orders.html");
    }

    public static void profileQuoteDetails(Context ctx) {

        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/signin");
            return;
        }

        Carport carport = new Carport(
                240,
                230,
                500,
                true,
                230,
                200,
                "Fladt tag"
        );

        CarportRequest carportRequest = new CarportRequest(
                1,
                user.getUserId(),
                carport,
                "PENDING",
                "Kan man gøre plads til en motorcykel ved siden af?",
                LocalDateTime.now()
        );

        Quote quote = new Quote(
                1,
                1,
                user.getUserId(),
                new BigDecimal("39395"),
                "AFVENTER BEHANDLING",
                "Sælger har justeret målene en smule.",
                LocalDate.now().plusDays(14),
                LocalDateTime.now(),
                null,
                null,
                null
        );

        ctx.attribute("user", user);
        ctx.attribute("carportRequest", carportRequest);
        ctx.attribute("quote", quote);

        ctx.render("profile-qoute-details.html");
    }
}