package app.controllers;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Material;
import app.entities.Quote;
import app.entities.User;
import app.persistence.CarportRequestMapper;
import app.persistence.MaterialMapper;
import app.persistence.QuotesMapper;
import app.persistence.UserMapper;
import app.services.CarportCalculationResult;
import app.services.CarportCalculator;
import io.javalin.http.Context;

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
        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());

        List<CarportRequest> requests = carportRequestMapper.getAllCarportRequests();

        ctx.attribute("currentPage", "requests");
        ctx.attribute("requests", requests);

        ctx.render("admin-view-requests.html");
    }

    public static void adminRequestDetails(Context ctx) {
        String requestIdString = ctx.queryParam("requestId");

        if (requestIdString == null || requestIdString.isBlank()) {
            ctx.redirect("/admin/requests");
            return;
        }

        int requestId = Integer.parseInt(requestIdString);

        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());
        UserMapper userMapper = new UserMapper(Main.getConnectionPool());
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());

        CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(requestId);

        if (carportRequest == null) {
            ctx.redirect("/admin/requests");
            return;
        }

        User user = userMapper.getUserById(carportRequest.getUserId());

        CarportCalculator calculator = new CarportCalculator();

        CarportCalculationResult calculationResult = calculator.calculate(
                carportRequest.getCarport(),
                materialMapper.getActiveMaterials()
        );

        ctx.attribute("currentPage", "requests");
        ctx.attribute("carportRequest", carportRequest);
        ctx.attribute("user", user);
        ctx.attribute("calculationResult", calculationResult);

        ctx.render("admin-Request-Details.html");
    }

    public static void adminProductsAndPrice(Context ctx) {
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());

        List<Material> materials = materialMapper.getAllMaterials();

        ctx.attribute("currentPage", "products");
        ctx.attribute("materials", materials);

        ctx.render("admin-products-and-price.html");
    }

    public static void profileQuoteDetails(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        String quoteIdString = ctx.queryParam("quoteId");

        if (quoteIdString == null || quoteIdString.isBlank()) {
            ctx.redirect("/profile");
            return;
        }

        int quoteId = Integer.parseInt(quoteIdString);

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());
        UserMapper userMapper = new UserMapper(Main.getConnectionPool());

        Quote quote = quotesMapper.getQuoteById(quoteId);

        if (quote == null) {
            ctx.redirect("/profile");
            return;
        }

        CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(quote.getRequestId());

        if (carportRequest == null) {
            ctx.redirect("/profile");
            return;
        }

        User user = userMapper.getUserById(carportRequest.getUserId());

        String previewImage = QuoteController.getPreviewImage(carportRequest.getCarport());
        quote.setPreviewImage(previewImage);

        Carport carport = carportRequest.getCarport();

        String svg = SvgController.createCarportSvg(carport);

        ctx.attribute("user", user);
        ctx.attribute("carportRequest", carportRequest);
        ctx.attribute("quote", quote);
        ctx.attribute("svg", svg);

        ctx.render("profile-qoute-details.html");
    }
}