package app.controllers;

import app.Main;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;
import app.entities.Quote;
import app.persistence.QuotesMapper;
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

        ctx.attribute("user", user);
        ctx.attribute("quotes", quotes);

        ctx.render("profile.html");
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void handleSignIn(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User user = userMapper.validateLogin(email, password);

        if (user == null) {
            ctx.attribute("error", "Wrong mail or password!");
            ctx.render("signin.html");
            return;
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.redirect("/profile");
    }

    public static void handleSignUp(Context ctx) {
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String phoneNumber = ctx.formParam("phoneNumber");
        String address = ctx.formParam("address");
        String postalCode = ctx.formParam("postalCode");
        String city = ctx.formParam("city");

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User existingUser = userMapper.getUserByEmail(email);

        if (existingUser != null) {
            ctx.attribute("error", "Der findes allerede en bruger med denne e-mail");
            ctx.render("signup.html");
            return;
        }

        User user = new User(0, firstName, lastName, email, password, phoneNumber, address, postalCode, city, "customer", null);

        userMapper.createUser2(user);

        ctx.redirect("/signin");
    }
}