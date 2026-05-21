package app.controllers;

import app.Main;
import app.entities.CarportRequest;
import app.entities.ProfileOrder;
import app.entities.Quote;
import app.entities.User;
import app.persistence.*;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.javalin.http.Context;

import java.util.List;

public class UserController {

    public static void signIn(Context ctx) {
        ctx.render("signin.html");
    }

    public static void signUp(Context ctx) {
        ctx.render("signup.html");
    }

    public static void profile(Context ctx) {
        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/signin");
            return;
        }

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        List<Quote> quotes = quotesMapper.getQuotesByUserId(user.getUserId());
        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());

        for (Quote quote : quotes) {
            CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(quote.getRequestId());
            String previewImage = QuoteController.getPreviewImage(carportRequest.getCarport());
            quote.setPreviewImage(previewImage);
        }

        OrderMapper orderMapper = new OrderMapper(Main.getConnectionPool());
        List<ProfileOrder> orders = orderMapper.getProfileOrdersByUserId(user.getUserId());

        for (ProfileOrder order : orders) {
            Quote quote = quotesMapper.getQuoteById(order.getQuoteId());
            CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(quote.getRequestId());
            String previewImage = QuoteController.getPreviewImage(carportRequest.getCarport());
            order.setPreviewImage(previewImage);
        }


        ctx.attribute("user", user);
        ctx.attribute("quotes", quotes);
        ctx.attribute("orders", orders);

        ctx.render("profile.html");
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void handleSignIn(Context ctx) {
        String email = ctx.formParam("email").trim().toLowerCase();
        String password = ctx.formParam("password");

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User user = userMapper.getUserByEmail(email);

        if (user == null) {
            ctx.attribute("error", "Forkert mail eller adgangskode");
            ctx.render("signin.html");
            return;
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash());

        if (!result.verified) {
            ctx.attribute("error", "Forkert mail eller adgangskode");
            ctx.render("signin.html");
            return;
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.redirect("/");
    }

    public static void handleSignUp(Context ctx) {
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email").trim().toLowerCase();
        String password = ctx.formParam("password");
        String phoneNumber = ctx.formParam("phoneNumber").trim().replace(" ", "");
        String address = ctx.formParam("address");
        String postalCode = ctx.formParam("postalCode");
        String city = ctx.formParam("city");

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User existingUserByEmail = userMapper.getUserByEmail(email);

        if (existingUserByEmail != null) {
            ctx.attribute("emailError", "E-mail findes allerede");
            ctx.render("signup.html");
            return;
        }

        User existingUserByPhone = userMapper.getUserByPhone(phoneNumber);

        if (existingUserByPhone != null) {
            ctx.attribute("phoneError", "Nummeret findes allerede");
            ctx.render("signup.html");
            return;
        }

        String hashPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        User user = new User(
                0,
                firstName,
                lastName,
                email,
                hashPassword,
                phoneNumber,
                address,
                postalCode,
                city,
                "customer",
                null
        );

        userMapper.createUser(user);

        ctx.redirect("/signin");
    }
}