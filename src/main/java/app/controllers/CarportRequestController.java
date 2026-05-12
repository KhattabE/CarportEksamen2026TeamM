package app.controllers;

import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.User;
import app.persistence.CarportRequestMapper;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class CarportRequestController {

    public static void buildYourCarport(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", currentUser);
        model.put("message", ctx.consumeSessionAttribute("message"));

        ctx.render("build-your-carport.html", model);
    }

    public static void createCarportRequest(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.sessionAttribute("message", "You must be logged in to request a carport.");
            ctx.redirect("/signin");
            return;
        }

        try {
            int widthCm = Integer.parseInt(ctx.formParam("width_cm"));
            int lengthCm = Integer.parseInt(ctx.formParam("length_cm"));
            int heightCm = Integer.parseInt(ctx.formParam("height_cm"));

            boolean hasShed = ctx.formParam("has_shed") != null;

            int shedWidthCm = 0;
            int shedLengthCm = 0;

            if (hasShed) {
                String shedWidth = ctx.formParam("shed_width_cm");
                String shedLength = ctx.formParam("shed_length_cm");

                if (shedWidth != null && !shedWidth.isBlank()) {
                    shedWidthCm = Integer.parseInt(shedWidth);
                }

                if (shedLength != null && !shedLength.isBlank()) {
                    shedLengthCm = Integer.parseInt(shedLength);
                }
            }

            String roofType = ctx.formParam("roof_type");
            if (roofType == null || roofType.isBlank()) {
                roofType = "flat";
            }

            String customerComment = ctx.formParam("customer_comment");
            if (customerComment == null) {
                customerComment = "";
            }

            Carport carport = new Carport(widthCm, lengthCm, heightCm, hasShed, shedWidthCm, shedLengthCm, roofType);

            CarportRequest carportRequest = new CarportRequest(
                    0,
                    currentUser.getUserId(),
                    carport,
                    "PENDING",
                    customerComment,
                    null
            );

            CarportRequestMapper carportRequestMapper = new CarportRequestMapper(ConnectionPool.getInstance());

            carportRequestMapper.createCarportRequest(carportRequest);

            ctx.sessionAttribute("message", "Your carport request has been sent.");
            ctx.redirect("/build-your-carport");

        } catch (Exception e) {
            ctx.sessionAttribute("message", "Something went wrong. Please check your measurements.");
            ctx.redirect("/build-your-carport");
        }
    }
}
