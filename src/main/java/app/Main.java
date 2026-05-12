package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "}DPU2Y-h(=T£4E`F\\e6j@i]`n:mX,5g";
    private static final String URL = "jdbc:postgresql://134.209.233.109:5432/%s?currentSchema=public";
    private static final String DB = "carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");

            config.jetty.modifyServletContextHandler(
                    handler -> handler.setSessionHandler(SessionConfig.sessionConfig())
            );

            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));

            config.routes.get("/", MainController::index);

            config.routes.get("/signup", UserController::signUp);
            config.routes.post("/signup", UserController::handleSignUp);

            config.routes.get("/signin", UserController::signIn);
            config.routes.post("/signin", UserController::handleSignIn);

            config.routes.get("/profile", UserController::profile);
            config.routes.get("/profile/quote-details", MainController::profileQuoteDetails);
            config.routes.get("/logout", UserController::logout);

            config.routes.get("/build-your-carport", CarportRequestController::buildYourCarport);

            config.routes.get("/admin", MainController::adminDashboard);
            config.routes.get("/admin/requests", MainController::adminViewRequests);
            config.routes.get("/admin/request-details", MainController::adminRequestDetails);
            config.routes.get("/admin/orders", MainController::adminViewOrders);

        }).start(8080);
    }
}