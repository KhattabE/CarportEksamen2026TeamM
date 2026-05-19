package app.controllers;

import app.Main;
import app.entities.ProfileOrder;
import app.entities.Quote;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.QuotesMapper;
import app.persistence.UserMapper;
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

        // Gets the customer's active quotes for "Mine tilbud"
        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        List<Quote> quotes = quotesMapper.getQuotesByUserId(user.getUserId());

        // Gets the customer's real orders for "Mine ordrer"
        OrderMapper orderMapper = new OrderMapper(Main.getConnectionPool());
        List<ProfileOrder> orders = orderMapper.getProfileOrdersByUserId(user.getUserId());

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
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User user = userMapper.getUserByEmail(email);

        if (user == null) {
            ctx.attribute("error", "Wrong mail or password!");
            ctx.render("signin.html");
            return;
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash());

        if(!result.verified){
            ctx.attribute("error", "Wrong mail or password!");
            ctx.render("signin.html");
            return;
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.redirect("/");
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

        String hashPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        ConnectionPool connectionPool = Main.getConnectionPool();
        UserMapper userMapper = new UserMapper(connectionPool);

        User existingUser = userMapper.getUserByEmail(email);

        if (existingUser != null) {
            ctx.attribute("error", "Der findes allerede en bruger med denne e-mail");
            ctx.render("signup.html");
            return;
        }

        User user = new User(0, firstName, lastName, email, hashPassword, phoneNumber, address, postalCode, city, "customer", null);

        userMapper.createUser(user);

        ctx.redirect("/signin");
    }
}