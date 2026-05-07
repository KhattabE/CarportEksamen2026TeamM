package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {

    public static void signIn(Context ctx) {
        ctx.render("signin.html");
    }

    public static void signUp(Context ctx) {
        ctx.render("signup.html");
    }

    public static void handleSignIn(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        UserMapper userMapper = new UserMapper(connectionPool);
        User user = userMapper.validateLogin(email, password);

        if (user == null) {
            ctx.attribute("error", "Forkert email eller adgangskode");
            ctx.render("signin.html");
            return;
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.redirect("/");
    }

    public static void handleSignUp(Context ctx, ConnectionPool connectionPool) {
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String phoneNumber = ctx.formParam("phoneNumber");
        String address = ctx.formParam("address");
        String postalCode = ctx.formParam("postalCode");
        String city = ctx.formParam("city");

        UserMapper userMapper = new UserMapper(connectionPool);

        User existingUser = userMapper.getUserByEmail(email);

        if (existingUser != null) {
            ctx.attribute("error", "Der findes allerede en bruger med denne e-mail");
            ctx.render("signup.html");
            return;
        }

        User user = new User(
                0,
                firstName,
                lastName,
                email,
                password,
                phoneNumber,
                address,
                postalCode,
                city,
                "customer",
                null
        );

        userMapper.createUser2(user);

        ctx.redirect("/signin");
    }
}