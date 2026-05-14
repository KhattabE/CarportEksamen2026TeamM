package app.controllers;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Material;
import app.entities.Quote;
import app.entities.User;
import app.persistence.MaterialMapper;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainController {

    public static void index(Context ctx) {
        ctx.render("index-Frontpage.html");
    }

    public static void adminDashboard(Context ctx) {
        ctx.attribute("currentPage", "dashboard");
        ctx.render("admin-dashboard.html");
    }

    public static void adminViewRequests(Context ctx) {
        ctx.attribute("currentPage", "requests");
        ctx.render("admin-view-requests.html");
    }

    public static void adminRequestDetails(Context ctx) {
        ctx.attribute("currentPage", "requests");
        ctx.render("admin-Request-Details.html");
    }

    public static void adminViewOrders(Context ctx) {
        ctx.attribute("currentPage", "orders");
        ctx.render("admin_view_orders.html");
    }

    public static void adminProductsAndPrice(Context ctx) {
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());

        List<Material> materials = materialMapper.getAllMaterials();

        ctx.attribute("currentPage", "products");
        ctx.attribute("materials", materials);

        ctx.render("admin-products-and-price.html");
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