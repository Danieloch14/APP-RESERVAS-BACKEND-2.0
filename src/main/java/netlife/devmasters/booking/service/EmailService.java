package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static netlife.devmasters.booking.constant.EmailConst.*;

@Service
public class EmailService {

    @Value("${netlife.email.username}")
    private String USERNAME;

    @Value("${netlife.email.password}")
    private String PASSWORD;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(EMAIL_SMTP_SERVER);
        mailSender.setPort(DEFAULT_PORT);

        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ehlo", "true");

        return mailSender;
    }

    public void validateCodeEmail(String firstName, String codigo, String email) throws MessagingException, IOException {
        String[] destinatarios = {email};
        if (email.contains(",") || email.contains(";")) {
            destinatarios = email.split("[,;]");
        }
        String Path = //RUTA_PLANTILLAS +
                "template-pecbdmq-validacion.html";
        String htmlTemplate = readFile(Path);
        htmlTemplate = htmlTemplate.replace("${firstName}", firstName);
        htmlTemplate = htmlTemplate.replace("${codigo}", codigo);
        MimeMessage message = this.createEmailHtml(destinatarios, EMAIL_SUBJECT_PASSWORD, htmlTemplate);
        JavaMailSender emailSender = getJavaMailSender();
        emailSender.send(message);

    }

    public void sendMensajeTextGenerico(String email, String subject, String mensaje) {
        try {
            String[] emails = {email};
            if (email.contains(",") || email.contains(";")) {
                emails = email.split("[,;]");
            }

            String Path =// RUTA_PLANTILLAS +
                    "template-pecbdmq-general.html";
            String htmlTemplate = readFile(Path);
            htmlTemplate = htmlTemplate.replace("${mensaje}", mensaje);
            MimeMessage message = this.createEmailHtml(emails, subject, htmlTemplate);
            JavaMailSender emailSender = getJavaMailSender();
            emailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendMensajeHtmlGenerico(String[] email, String subject, String mensaje) {
        try {

            String Path = //RUTA_PLANTILLAS +
                    "template-pecbdmq-general-html.html";
            String htmlTemplate = readFile(Path);
            htmlTemplate = htmlTemplate.replace("${mensaje}", mensaje);
            MimeMessage message = this.createEmailHtml(email, subject, htmlTemplate);
            JavaMailSender emailSender = getJavaMailSender();
            emailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private MimeMessage createEmailHtml(String[] destinatarios, String subject, String textoHtml)
            throws MessagingException {
        MimeMessage message = getJavaMailSender().createMimeMessage();
        InternetAddress fromAddress = new InternetAddress(USERNAME);
        message.setFrom(fromAddress);

        List<InternetAddress> recipientList = new ArrayList<>();
        for (String destinatario : destinatarios) {
            recipientList.add(new InternetAddress(destinatario));
        }

        message.setRecipients(MimeMessage.RecipientType.TO, recipientList.toArray(new InternetAddress[0]));
        message.setSubject(subject);

        message.setContent(textoHtml, "text/html; charset=utf-8");
        return message;

    }

    private String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

