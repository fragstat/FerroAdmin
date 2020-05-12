package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.Request;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class MainController {

    private final RequestRepository requestRepository;

    public MainController(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
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
        return "index";


    }

    @GetMapping("/admin")
    public String admin(Model model){
        Iterable<Request> requests = requestRepository.findAll();
        model.addAttribute("req",requests);
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

}
