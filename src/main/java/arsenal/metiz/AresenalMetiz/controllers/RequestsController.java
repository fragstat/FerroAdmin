package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.ArchivedRequests;
import arsenal.metiz.AresenalMetiz.models.Request;
import arsenal.metiz.AresenalMetiz.repo.ArchivedRequestsRepo;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class RequestsController {

    @Autowired
    RequestRepository requestRepository;
    ArchivedRequestsRepo archivedRequestsRepo;

    @GetMapping("/admin/requests/{id}/remove")
    public String requestRemove(@PathVariable("id") long id, Model model) {
        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> req = new ArrayList<>();
        request.ifPresent(req::add);
        ArchivedRequests archivedRequests = new ArchivedRequests(req.get(0).getName(), req.get(0).getEmail(), req.get(0).getRequest(), req.get(0).getDate());
        archivedRequestsRepo.save(archivedRequests);
        requestRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/requests")
    public String requests(Model model) {
        Iterable<Request> requests = requestRepository.findAll();
        model.addAttribute("req", requests);
        Iterable<ArchivedRequests> archivedRequests = archivedRequestsRepo.findAll();
        model.addAttribute("reqA", archivedRequests);
        return "requests";
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
