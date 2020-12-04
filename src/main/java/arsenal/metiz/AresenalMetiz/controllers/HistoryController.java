package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.HistoryView;
import arsenal.metiz.AresenalMetiz.service.historyservice.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/history")
public class HistoryController {

    final HistoryService service;

    @Autowired
    public HistoryController(HistoryService service) {
        this.service = service;
    }

    @GetMapping("/all")
    List<HistoryView> getHistory() {
        return service.getHistoryView();
    }

    @GetMapping("/departure/{id}")
    HistoryView getHistoryDeparture(@PathVariable("id") Long id) {
        return service.getHistoryByDepartureId(id);
    }
}
