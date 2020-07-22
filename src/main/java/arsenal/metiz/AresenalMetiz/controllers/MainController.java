package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.ArchivedRequests;
import arsenal.metiz.AresenalMetiz.models.Database;
import arsenal.metiz.AresenalMetiz.models.Request;
import arsenal.metiz.AresenalMetiz.repo.ArchivedRequestsRepo;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
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

    @GetMapping("/products")
    public String ferroProd(Model model){
        return "products";
    }

    @PostMapping("/")
    public String ferroPost(@RequestParam String name,@RequestParam String email,@RequestParam String request, Model model){
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

    @GetMapping(value = "/admin/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] downloadQr(Model model) throws IOException {
        StringBuilder qr = new StringBuilder();
        char[] al = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
        for (int i = 0; i <= 14; i++) {
            qr.append(al[(int) (Math.random() * al.length)]);
        }
        BufferedImage image = null;
        URL url = new URL("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + qr);
        image = ImageIO.read(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] res = baos.toByteArray();
        return res;
    }

    @GetMapping("/admin/chem")
    public String chem(Model model) {
        model.addAttribute("C","-");
        model.addAttribute("Si","-");
        model.addAttribute("Mn","-");
        model.addAttribute("Cr","-");
        model.addAttribute("Ni","-");
        model.addAttribute("Cu","-");
        return "chem";
    }

    @PostMapping("/admin/chem")
    public String chemPost(@RequestParam String OD, @RequestParam String OT, @RequestParam String OC,
                           @RequestParam String OSi, @RequestParam String OMn,@RequestParam String OCr,
                           @RequestParam String ONi, @RequestParam String OCu, @RequestParam String IC,
                           @RequestParam String ISi, @RequestParam String IMn,@RequestParam String ICr,
                           @RequestParam String INi, @RequestParam String ICu, Model model) throws IOException {
        String[] inputs = {OD,OT,OC,OSi,OMn,OCr,ONi,OCu,IC,ISi,IMn,ICr,INi,ICu};
        Map<String,Double> map = Composition.calculate(inputs);
        model.addAttribute("C",map.get("C"));
        model.addAttribute("Si",map.get("Si"));
        model.addAttribute("Mn",map.get("Mn"));
        model.addAttribute("Cr",map.get("Cr"));
        model.addAttribute("Ni",map.get("Ni"));
        model.addAttribute("Cu",map.get("Cu"));


        return "chem";
    }


}
