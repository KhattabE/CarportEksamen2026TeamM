package app.controllers;

import io.javalin.http.Context;

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
}