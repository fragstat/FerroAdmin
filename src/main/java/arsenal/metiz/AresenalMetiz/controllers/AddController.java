package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AddController {

    @Autowired
    WarehouseRepo warehouse;

    @Autowired
    WarehousePackageRepo warehousePackage;

    @PostMapping("api/position/add")
    @ResponseBody
    public ResponseEntity addPosition(@RequestParam String mark, @RequestParam String diameter,
                                      @RequestParam String packing, @RequestParam String mass,
                                      @RequestParam String comment, @RequestParam String plav,
                                      @RequestParam String part, @RequestParam String manufacturer) {
        if (mark.isEmpty() || diameter.isEmpty() || packing.isEmpty() || mass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String massF = mass.replaceAll(",", ".");
        WarehousePosition warehousePosition = new WarehousePosition(mark, diameter, packing, comment, part, plav,
                Float.valueOf(massF), manufacturer, PositionStatus.In_stock);
        warehouse.save(warehousePosition);
        APIController.updateTags();
        return new ResponseEntity(warehousePosition.getId(), HttpStatus.OK);
    }

    @PostMapping("api/multipleAdd")
    public Map<String, List<Long>> doMultiInput(@RequestBody List<WarehouseAddPosition> in) {
        List<Long> ids = new ArrayList<>();
        List<Long> packs = new ArrayList<>();
        Map<String, List<Long>> id = new HashMap<>();
        WarehousePackage packages = new WarehousePackage();
        for (WarehouseAddPosition p : in) {
            WarehousePosition warehousePosition = new WarehousePosition(p.getMark(), p.getDiameter(), p.getPacking(),
                    p.getComment(), p.getPart(), p.getPlav(),
                    p.getMass(), p.getManufacturer(), PositionStatus.In_stock);
            warehouse.save(warehousePosition);
            packages.attach(warehousePosition);
            ids.add(warehousePosition.getId());
        }
        warehousePackage.save(packages);
        packs.add(packages.getId());
        id.put("id", ids);
        id.put("package", packs);
        return id;
    }
}
