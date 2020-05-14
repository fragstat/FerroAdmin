package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.ArchivedRequests;
import arsenal.metiz.AresenalMetiz.models.Database;
import arsenal.metiz.AresenalMetiz.models.Request;
import arsenal.metiz.AresenalMetiz.repo.ArchivedRequestsRepo;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;

@Controller
public class MainController {

    private final RequestRepository requestRepository;
    private final ArchivedRequestsRepo archivedRequestsRepo;

    public MainController(RequestRepository requestRepository, ArchivedRequestsRepo archivedRequestsRepo) {
        this.requestRepository = requestRepository;
        this.archivedRequestsRepo = archivedRequestsRepo;
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login2";
    }

    @GetMapping("/")
    public String ferroMain(Model model){
        return "index";
    }

    @PostMapping("/")
    public String ferroPost(@RequestParam String name,@RequestParam String email,@RequestParam String request, Model model){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm , dd MMMM yyyy");
        Request req = new Request(name, email, request, simpleDateFormat.format(date));
        requestRepository.save(req);

        String to = "sevalavin@edu.hse.ru";
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
        } catch (MessagingException mex){ mex.printStackTrace(); }


        return "index";
    }

    @PostMapping("/c")
    public String ferroCode(@RequestParam String code, Model model){
        if(code == null){
            return "index";
        }
        Database database = Database.getInstance();
        try {
            Map<String,Object> map = database.getDocumentAsMap(code);

            if (map != null && map.get("id").equals("Неверный код")) {
                model.addAttribute("id", "Неверный код");
                model.addAttribute("mark", "-");
                model.addAttribute("part", "-");
                model.addAttribute("plav","-");
                model.addAttribute("weight", "-");
                model.addAttribute("diameter", "-");
                model.addAttribute("customer", "-");
                model.addAttribute("src", "https://pk-izhsintez.ru/upload/iblock/ba6/ba6495b4efc9e3d9ae6d7fd2a36b4874.png");
            }else {
                assert map != null;
                model.addAttribute("id", map.get("id"));
                model.addAttribute("mark", map.get("mark"));
                model.addAttribute("part", map.get("part"));
                model.addAttribute("plav",map.get("plav"));
                model.addAttribute("weight", map.get("weight"));
                model.addAttribute("diameter", map.get("diameter"));
                model.addAttribute("customer", map.get("customer"));
                model.addAttribute("src", "https://i.yapx.ru/GU705t.png");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }

        return "result";
    }

    @GetMapping("/admin/requests/{id}/remove")
    public String requestRemove(@PathVariable("id") long id, Model model) {
        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> req = new ArrayList<>();
        request.ifPresent(req::add);
        ArchivedRequests archivedRequests = new ArchivedRequests(req.get(0).getName(),req.get(0).getEmail(),req.get(0).getRequest(),req.get(0).getDate());
        archivedRequestsRepo.save(archivedRequests);
        requestRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        Iterable<Request> requests = requestRepository.findAll();
        model.addAttribute("req",requests);
        Iterable<ArchivedRequests> archivedRequests = archivedRequestsRepo.findAll();
        model.addAttribute("reqA", archivedRequests);
        return "admin";
    }

    @GetMapping("/admin/requests/{id}")
    public String requestDetails(@PathVariable("id") long id, Model model) {
        if (!requestRepository.existsById(id)) {
            return "admin";
        }

        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> req = new ArrayList<>();
        request.ifPresent(req::add);
        model.addAttribute("request", req);
        return "request-details";

    }

    @GetMapping("/admin/requestsA/{id}")
    public String archivedRequestDetails(@PathVariable("id") long id, Model model) {
        if (!archivedRequestsRepo.existsById(id)) {
            return "admin";
        }

        Optional<ArchivedRequests> request = archivedRequestsRepo.findById(id);
        ArrayList<ArchivedRequests> req = new ArrayList<>();
        request.ifPresent(req::add);
        model.addAttribute("request", req);
        return "request-details";

    }

}
