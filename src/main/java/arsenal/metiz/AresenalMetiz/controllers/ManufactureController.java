package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.DeparturePreProcessRequestView;
import arsenal.metiz.AresenalMetiz.assets.DeparturePreProcessResponseView;
import arsenal.metiz.AresenalMetiz.assets.ManufactureTransferView;
import arsenal.metiz.AresenalMetiz.models.Position;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.service.manufactureservice.ManufactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ManufactureController {

    private final ManufactureService manufactureService;

    @Autowired
    public ManufactureController(ManufactureService manufactureService) {
        this.manufactureService = manufactureService;
    }

    @PostMapping("api/manufacture/add")
    public Map<String, List<Long>> multipleAdding(@RequestBody List<WarehouseAddPosition> list) {
        return manufactureService.doMultipleAdd(list);
    }

    @PostMapping("api/manufacture/transfer")
    public ResponseEntity<Long> confirmTransfer(@RequestBody ManufactureTransferView view) {
        Long result = manufactureService.transfer(view);
        if (result == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @GetMapping("api/manufacture/position/{id}")
    public Position getById(@PathVariable("id") Long id) {
        return manufactureService.getById(id);
    }

    @PostMapping("api/manufacture/departurePreProcess")
    public DeparturePreProcessResponseView departurePreProcess(@RequestBody DeparturePreProcessRequestView view) {
        return manufactureService.departurePreProcess(view);
    }


}
