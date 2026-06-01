package app.controllers;
import app.entities.QuoteMaterialLine;
import app.persistence.QuoteMaterialLineMapper;
import app.services.Svg;
import app.util.GmailEmailSenderHTML;

import app.Main;
import app.entities.Carport;
import app.entities.CarportRequest;
import app.entities.Material;
import app.entities.Order;
import app.entities.Quote;
import app.entities.User;
import app.persistence.CarportRequestMapper;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.QuotesMapper;
import app.services.CarportCalculationResult;
import app.services.CarportCalculator;
import io.javalin.http.Context;

import java.time.LocalDate;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.mail.MessagingException;

public class QuoteController {

    public static void rejectQuote(Context ctx) {
        String quoteIdString = ctx.formParam("quoteId");

        if (quoteIdString == null || quoteIdString.isBlank()) {
            ctx.redirect("/profile");
            return;
        }

        int quoteId = Integer.parseInt(quoteIdString);

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        quotesMapper.rejectQuote(quoteId);

        ctx.redirect("/profile");
    }

    public static void acceptQuote(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        String quoteIdString = ctx.formParam("quoteId");

        if (quoteIdString == null || quoteIdString.isBlank()) {
            ctx.redirect("/profile");
            return;
        }

        int quoteId = Integer.parseInt(quoteIdString);

        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());
        OrderMapper orderMapper = new OrderMapper(Main.getConnectionPool());
        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());
        QuoteMaterialLineMapper quoteMaterialLineMapper = new QuoteMaterialLineMapper(Main.getConnectionPool());
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());

        Quote quote = quotesMapper.getQuoteById(quoteId);

        if (quote == null) {
            ctx.redirect("/profile");
            return;
        }

        quotesMapper.acceptQuote(quoteId);

        Order existingOrder = orderMapper.getOrderByQuoteId(quoteId);

        boolean shouldSendPaymentEmail = false;

        if (existingOrder == null) {
            orderMapper.createOrder(quoteId);
            shouldSendPaymentEmail = true;
        } else if (!"PAID".equals(existingOrder.getPaymentStatus())) {
            orderMapper.markOrderAsPaid(existingOrder.getOrderId());
            shouldSendPaymentEmail = true;
        }

        if (shouldSendPaymentEmail) {
            try {
                CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(quote.getRequestId());

                if (carportRequest == null) {
                    throw new RuntimeException("Could not find carport request for quote id: " + quoteId);
                }

                Carport carport = carportRequest.getCarport();

                List<QuoteMaterialLine> quoteMaterialLines = quoteMaterialLineMapper.getQuoteMaterialLinesByQuoteId(quoteId);

                if (quoteMaterialLines.isEmpty()) {
                    List<Material> activeMaterials = materialMapper.getActiveMaterials();

                    CarportCalculator calculator = new CarportCalculator();
                    CarportCalculationResult result = calculator.calculate(carport, activeMaterials);

                    quoteMaterialLines = result.createQuoteMaterialLines(quoteId);
                    quoteMaterialLineMapper.addQuoteMaterialLines(quoteMaterialLines);
                }

                List<Map<String, Object>> materialLinesForEmail = new ArrayList<>();

                for (QuoteMaterialLine line : quoteMaterialLines) {
                    Material material = materialMapper.getMaterialById(line.getMaterialId());

                    Map<String, Object> emailLine = new HashMap<>();

                    if (material != null) {
                        emailLine.put("materialName", material.getName());
                    } else {
                        emailLine.put("materialName", "Ukendt materiale");
                    }

                    emailLine.put("usageDescription", line.getUsageDescription());
                    emailLine.put("formattedQuantity", line.getQuantity() + " " + line.getUnit());

                    if (line.getLengthCm() > 0) {
                        emailLine.put("formattedLength", line.getLengthCm() + " cm");
                    } else {
                        emailLine.put("formattedLength", "-");
                    }

                    materialLinesForEmail.add(emailLine);
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("customerName", currentUser.getFirstName());
                variables.put("quoteId", quote.getQuoteId());
                variables.put("totalPrice", quote.getTotalPrice());

                variables.put("hasShed", carport.isHasShed());
                variables.put("lengthCm", carport.getLengthCm());
                variables.put("widthCm", carport.getWidthCm());
                variables.put("heightCm", carport.getHeightCm());
                variables.put("roofType", carport.getRoofType());
                variables.put("shedWidthCm", carport.getShedWidthCm());
                variables.put("shedLengthCm", carport.getShedLengthCm());

                variables.put("materialLines", materialLinesForEmail);

                GmailEmailSenderHTML emailSender = new GmailEmailSenderHTML();

                String html = emailSender.renderTemplate("payment_confirmation_email", variables);

                Svg carportSvg = SvgController.createCarportSvgObject(carport);
                byte[] pdfBytes = carportSvg.toPdfBytes();

                emailSender.sendHtmlEmailWithPdfAttachment(currentUser.getEmail(), "Tak for din betaling - Fog Custom Carport", html, pdfBytes, "carport-tegning.pdf");
                emailSender.sendHtmlEmail(currentUser.getEmail(), "Tak for din betaling - Fog Custom Carport", html);

                System.out.println("Payment confirmation email sent to " + currentUser.getEmail());

            } catch (MessagingException e) {
                System.out.println("Could not send payment confirmation email");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Could not prepare payment confirmation email");
                e.printStackTrace();
            }
        }

        ctx.redirect("/profile");
    }
    public static void createQuoteFromRequest(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        String requestIdString = ctx.formParam("requestId");

        if (requestIdString == null || requestIdString.isBlank()) {
            ctx.redirect("/admin/requests");
            return;
        }

        int requestId = Integer.parseInt(requestIdString);

        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());
        MaterialMapper materialMapper = new MaterialMapper(Main.getConnectionPool());
        QuotesMapper quotesMapper = new QuotesMapper(Main.getConnectionPool());

        CarportRequest carportRequest = carportRequestMapper.getCarportRequestById(requestId);

        if (carportRequest == null) {
            ctx.redirect("/admin/requests");
            return;
        }

        List<Material> materials = materialMapper.getActiveMaterials();

        CarportCalculator calculator = new CarportCalculator();

        CarportCalculationResult result = calculator.calculate(carportRequest.getCarport(), materials);

        Quote quote = new Quote(requestId, currentUser.getUserId(), result.getTotalPrice(), "SENT", "Automatisk genereret tilbud", LocalDate.now().plusDays(14));

        quotesMapper.createQuote(quote);

        carportRequestMapper.updateRequestStatus(requestId, "QUOTE_SENT");

        ctx.redirect("/admin/requests");
    }

    public static String getPreviewImage(Carport carport) {
        String roofType = carport.getRoofType();
        boolean hasShed = carport.isHasShed();

        int width = carport.getWidthCm();
        boolean isDouble = width > 300;

        if (!isDouble && roofType.equals("Fladt tag") && !hasShed) {
            return "/images/enkeltCarportUdenSkurOgUdenRejsning.png";

        } else if (!isDouble && roofType.equals("Fladt tag") && hasShed) {
            return "/images/enkeltCarportMedSkurOgUdenRejsning.png";

        } else if (!isDouble && roofType.equals("Rejsning") && !hasShed) {
            return "/images/enkeltCarportUdenSkurMedRejsning.png";

        } else if (!isDouble && roofType.equals("Rejsning") && hasShed) {
            return "/images/enkeltCarportMedSkurOgMedRejsning.png";

        } else if (isDouble && roofType.equals("Fladt tag") && !hasShed) {
            return "/images/dobbeltCarportUdenSkurOgUdenRejsning.png";

        } else if (isDouble && roofType.equals("Fladt tag") && hasShed) {
            return "/images/dobbeltCarportMedSkurUdenRejsning.png";

        } else if (isDouble && roofType.equals("Rejsning") && !hasShed) {
            return "/images/dobbeltCarportUdenSkurOgRejsning.png";

        } else {
            return "/images/dobbeltCarportMedSkurOgRejsning.png";
        }
    }

    public static void rejectRequest(Context ctx) {
        String requestIdString = ctx.formParam("requestId");

        if (requestIdString == null || requestIdString.isBlank()) {
            ctx.redirect("/admin/requests");
            return;
        }

        int requestId = Integer.parseInt(requestIdString);

        CarportRequestMapper carportRequestMapper = new CarportRequestMapper(Main.getConnectionPool());

        carportRequestMapper.updateRequestStatus(requestId, "AFVIST");

        ctx.redirect("/admin/requests");
    }
}