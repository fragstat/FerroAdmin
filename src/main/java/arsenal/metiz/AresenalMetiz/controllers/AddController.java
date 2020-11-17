package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.VerifyView;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.service.addservice.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AddController {

    @Autowired
    AddService service;

    @PostMapping("api/position/add")
    @ResponseBody
    public ResponseEntity<?> addPosition(@RequestParam String mark, @RequestParam String diameter,
                                         @RequestParam String packing, @RequestParam String mass,
                                         @RequestParam String comment, @RequestParam String plav,
                                         @RequestParam String part, @RequestParam String manufacturer) {
        return ResponseEntity.ok(service.addPosition(mark, diameter, packing, mass, comment, plav, part, manufacturer));
    }

    @PostMapping("api/multipleAdd")
    public Map<String, List<Long>> doMultiInput(@RequestBody List<WarehouseAddPosition> in) {
        return service.doMultiInput(in);
    }

    @PostMapping("/api/add/validate")
    public boolean verify(@RequestBody(required = false) VerifyView view) {
        return service.verify(view);
    }

    @GetMapping("/api/package/updateDataInside/{id}")
    public void update(@PathVariable Long id) {
        service.update(id);
    }
}
