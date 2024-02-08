package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.domain.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static netlife.devmasters.booking.constant.EmailConst.*;

@Service
public class EmailService {

    @Value("${netlife.email.username}")
    private String USERNAME;

    @Value("${netlife.email.password}")
    private String PASSWORD;
    private static final String host = "http://localhost:4200";

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
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return mailSender;
    }

    public void sendNewPasswordEmail(String firstName, String password, String email)
            throws MessagingException, IOException {
        String destinatarios[] = { email };
        InputStream sourceFile = this.getClass().getResourceAsStream("/template.html");
        String htmlTemplate = null;
        /*
         * sourceFile.toString();
         * "<!DOCTYPE html>\n" +
         * "<html>\n" +
         * "\n" +
         * "<head>\n" +
         * "    <title>Plataforma de reservas - NETLIFE</title>\n" +
         * "    <style>\n" +
         * "        body {\n" +
         * "            font-family: Arial, sans-serif;\n" +
         * "            background-color: #f4f4f4;\n" +
         * "            margin: 0;\n" +
         * "            padding: 0;\n" +
         * "        }\n" +
         * "\n" +
         * "        .container {\n" +
         * "            max-width: 600px;\n" +
         * "            margin: 0 auto;\n" +
         * "            padding: 20px;\n" +
         * "        }\n" +
         * "\n" +
         * "        header {\n" +
         * "            text-align: center;\n" +
         * "            margin-bottom: 20px;\n" +
         * "        }\n" +
         * "\n" +
         * "        header img {\n" +
         * "            max-width: 80%;\n" +
         * "            height: auto;\n" +
         * "        }\n" +
         * "\n" +
         * "        .container-content {\n" +
         * "            background-color: #ffffff;\n" +
         * "            padding: 20px;\n" +
         * "            border-radius: 5px;\n" +
         * "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
         * "        }\n" +
         * "\n" +
         * "        .container-content h2 {\n" +
         * "            margin-top: 0;\n" +
         * "            color: #333;\n" +
         * "        }\n" +
         * "\n" +
         * "        .container-content p {\n" +
         * "            margin-bottom: 20px;\n" +
         * "            color: #666;\n" +
         * "        }\n" +
         * "\n" +
         * "        .text-info {\n" +
         * "            color: #007bff;\n" +
         * "        }\n" +
         * "\n" +
         * "        .text-danger {\n" +
         * "            color: #ff4136;\n" +
         * "        }\n" +
         * "\n" +
         * "        .text-center {\n" +
         * "            text-align: center;\n" +
         * "        }\n" +
         * "\n" +
         * "        footer {\n" +
         * "            margin-top: 40px;\n" +
         * "            background-color: #f9f9f9;\n" +
         * "            padding: 20px;\n" +
         * "            border-radius: 5px;\n" +
         * "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
         * "            text-align: center;\n" +
         * "        }\n" +
         * "\n" +
         * "        hr {\n" +
         * "            background-color: rgba(0, 0, 0, .1);\n" +
         * "            border: 0;\n" +
         * "            height: 1px;\n" +
         * "        }\n" +
         * "    </style>\n" +
         * "</head>\n" +
         * "\n" +
         * "<body>\n" +
         * "    <div class=\"container\">\n" +
         * "        <header>\n" +
         * "            <img src=\"https://www.google.com/url?sa=i&url=https%3A%2F%2Fes.m.wikipedia.org%2Fwiki%2FArchivo%3ANetlife.png&psig=AOvVaw2qhNO8bkp6ghqRkYIt9z9S&ust=1701984300661000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCIjQreHf-4IDFQAAAAAdAAAAABAD\" alt=\"logo\">\n"
         * +
         * "        </header>\n" +
         * "        <div class=\"container-content\">\n" +
         * "            <h3>Plataforma de reservas - NETLIFE</h3>\n" +
         * "            <hr>\n" +
         * "\n" +
         * "        </div>\n" +
         * "        <div class=\"message\">\n" +
         * "            <h2>Recuperación de contraseña</h2>\n" +
         * "            <p>Estimado(a) ${usuario}</p>\n" +
         * "            <p>Hemos recibido una solicitud para generar la contraseña de tu cuenta.</p>\n"
         * +
         * "            <p>Tu nueva contraseña es: ${password}</p>\n" +
         * "        </div>\n" +
         * "        <footer class=\"footer\">\n" +
         * "            <p class=\"text-info\"> ✉\uFE0F Este correo electrónico fue generado automáticamente. Por favor, no respondas a\n"
         * +
         * "                este\n" +
         * "                mensaje.</p>\n" +
         * "            <br>\n" +
         * "            <p class=\"text-center text-danger\">© Todos los derechos reservados | <span>Netlife</span></p>\n"
         * +
         * "        </footer>\n" +
         * "    </div>\n" +
         * "</body>\n" +
         * "\n" +
         * "</html>";
         */
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sourceFile, StandardCharsets.UTF_8))) {
            htmlTemplate = reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            htmlTemplate = null;
        }
        htmlTemplate = htmlTemplate.replace("${usuario}", firstName);
        htmlTemplate = htmlTemplate.replace("${password}", password);

        MimeMessage message = this.createEmailHtml(destinatarios, EMAIL_SUBJECT_PASSWORD, htmlTemplate);
        JavaMailSender emailSender = getJavaMailSender();
        emailSender.send(message);

    }

    public void sendLinkRegister(RegisterRequest registerRequest) {
        Date fechaExpiracion = new Date();
        fechaExpiracion.setDate(fechaExpiracion.getDate() + 1);
        String enlace = host + "/auth/register?id_solicitud=" + registerRequest.getIdRegisterRequest();
        enlace += "&exp=" + fechaExpiracion.getTime();

        String mensaje = "Hola, tu proceso de registro en la plataforma de reservas de Netlife ha sido aprobado.\n\t" +
                "Para continuar con el proceso de registro, por favor ingresa al siguiente link:\n\t" +
                "<a href=\"" + enlace + "\">REGISTRO</a>" +
                "\n\tRecuerda que tienes 24 horas para completar el proceso de registro, de lo contrario deberás iniciar nuevamente el proceso.\n\t";
        sendMensajeTextGenerico(registerRequest.getPersonalData().getEmail(), EMAIL_REGISTER_LINK, mensaje);
    }

    public void sendMensajeTextGenerico(String email, String subject, String mensaje) {
        try {
            String[] emails = { email };
            if (email.contains(",") || email.contains(";")) {
                emails = email.split("[,;]");
            }
            InputStream sourceFile = this.getClass().getResourceAsStream("/templateGeneral.html");
            String htmlTemplate = null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(sourceFile, StandardCharsets.UTF_8))) {
                htmlTemplate = reader.lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
                htmlTemplate = null;
            }
            htmlTemplate = htmlTemplate.replace("${mensaje}", mensaje);
            MimeMessage message = this.createEmailHtml(emails, subject, htmlTemplate);
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
