package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.HistoryView;
import arsenal.metiz.AresenalMetiz.service.historyservice.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/history")
public class HistoryController {

    final HistoryService service;

    @Autowired
    public HistoryController(HistoryService service) {
        this.service = service;
    }

    @GetMapping("/")
    List<HistoryView> getHistory() {
        return service.getHistoryView();
    }

}
