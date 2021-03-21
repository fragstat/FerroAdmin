package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.assets.SDeparture;
import arsenal.metiz.AresenalMetiz.models.MDeparture;
import arsenal.metiz.AresenalMetiz.models.Position;
import arsenal.metiz.AresenalMetiz.service.departureservice.DepartureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class DepartureController {

    private final DepartureService departureService;

    @Autowired
    public DepartureController(DepartureService departureService) {
        this.departureService = departureService;
    }

    @RequestMapping(value = "/api/departure", method = RequestMethod.GET)
    public double countWeight(@RequestParam String query, @RequestParam(required = false) String except) {
        return departureService.countWeight(query, except);
    }

    @PostMapping("/api/departure")
    public Set<Position> doMultipleDeparture(@RequestParam String request,
                                             @RequestParam(required = false) String except) {
        return departureService.doMultipleDeparture(request, except);
    }

    @PostMapping("/api/departureConfirmation")
    public @ResponseBody
    IdToPrint confirmDeparture(@RequestBody MDeparture departure) {
        return departureService.confirmDeparture(departure);
    }

    @PostMapping("/api/position/departure")
    public ResponseEntity<?> soloDeparture(@RequestBody SDeparture departure) {
        return departureService.soloDeparture(departure);
    }

}
