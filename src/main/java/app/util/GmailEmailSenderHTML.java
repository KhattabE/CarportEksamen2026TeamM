package app.util;

import app.config.ThymeleafConfig;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Properties;

public class GmailEmailSenderHTML {

    private final String username;
    private final String password;
    private final TemplateEngine templateEngine;

    public GmailEmailSenderHTML() {
        this.username = System.getenv("MAIL_USERNAME");
        this.password = System.getenv("MAIL_PASSWORD");

        if (username == null || password == null) {
            throw new IllegalStateException("MAIL_USERNAME og MAIL_PASSWORD miljøvariabler skal være sat.");
        }

        this.templateEngine = ThymeleafConfig.templateEngine();
    }

    public String renderTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        Message message = createBaseMessage(to, subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");

        Transport.send(message);
        System.out.println("HTML-mail sendt til " + to);
    }

    public void sendHtmlEmailWithPdfAttachment(String to, String subject, String htmlBody, byte[] pdfBytes, String pdfFileName) throws MessagingException {
        Message message = createBaseMessage(to, subject);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");

        MimeBodyPart pdfAttachmentPart = new MimeBodyPart();
        DataSource pdfDataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
        pdfAttachmentPart.setDataHandler(new DataHandler(pdfDataSource));
        pdfAttachmentPart.setFileName(pdfFileName);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);
        multipart.addBodyPart(pdfAttachmentPart);

        message.setContent(multipart);

        Transport.send(message);
        System.out.println("HTML-mail med PDF sendt til " + to);
    }

    private Message createBaseMessage(String to, String subject) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        return message;
    }
}
