package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.Request;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Controller
public class DatabaseController {

    @Autowired
    RequestRepository requestRepository;

    @PostMapping("/")
    public String ferroPost(@RequestParam String name, @RequestParam String email, @RequestParam String request, Model model) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm , dd MMMM yyyy");
        Request req = new Request(name, email, request, simpleDateFormat.format(date));
        requestRepository.save(req);

        String to = "info@arsenal-metiz.ru";
        String from = "serh.valavin@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "serh.valavin@gmail.com", "vAlavin2002");
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session); // email message

            message.setFrom(new InternetAddress(from)); // setting header fields

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Новый запрос от " + name + " с сайта ferro-trade.ru"); // subject line


            message.setText("Новый запрос от " + name + ". E-mail: " + email +
                    ". Текст запроса: " + request);


            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


        return "index";
    }

}
