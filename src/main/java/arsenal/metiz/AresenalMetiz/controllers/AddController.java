package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.assets.VerifyView;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AddController {

    @Autowired
    WarehouseDao dao;

    @Autowired
    WarehouseRepo warehouse;

    @Autowired
    WarehousePackageRepo warehousePackage;

    @PostMapping("api/position/add")
    @ResponseBody
    public ResponseEntity<?> addPosition(@RequestParam String mark, @RequestParam String diameter,
                                         @RequestParam String packing, @RequestParam String mass,
                                         @RequestParam String comment, @RequestParam String plav,
                                         @RequestParam String part, @RequestParam String manufacturer) {
        if (mark.isEmpty() || diameter.isEmpty() || packing.isEmpty() || mass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String massF = mass.replaceAll(",", ".");
        WarehousePosition warehousePosition = new WarehousePosition(mark, diameter.replaceAll(",", "."), packing, comment, part, plav,
                Float.valueOf(massF), manufacturer, PositionStatus.In_stock);
        warehouse.save(warehousePosition);
        APIController.updateTags();
        return ResponseEntity.ok(warehousePosition.getId());
    }

    @PostMapping("api/multipleAdd")
    public Map<String, List<Long>> doMultiInput(@RequestBody List<WarehouseAddPosition> in) {
        List<Long> ids = new ArrayList<>();
        List<Long> packs = new ArrayList<>();
        List<WarehousePosition> list = new ArrayList<>();
        Map<String, List<Long>> id = new HashMap<>();
        WarehousePackage packages = new WarehousePackage();
        if (packages.verify(in)) {
            for (WarehouseAddPosition p : in) {
                WarehousePosition warehousePosition = new WarehousePosition(p.getMark(), p.getDiameter().replaceAll(",", "."), p.getPacking(),
                        p.getComment(), p.getPart(), p.getPlav(),
                        p.getMass(), p.getManufacturer(), PositionStatus.In_stock);
                warehouse.save(warehousePosition);
                list.add(warehousePosition);
                ids.add(warehousePosition.getId());
            }
            try {
                packages.attachAll(list);
                warehousePackage.save(packages);
                packs.add(packages.getId());
                id.put("package", packs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            id.put("id", ids);

            return id;
        } else {
            return null;
        }
    }

    @PostMapping("/api/add/validate")
    public boolean verify(@RequestBody(required = false) VerifyView view) {
        return dao.exists(view.mark, view.diameter, view.part, view.plav);
    }

    @GetMapping("/api/package/updateDataInside/{id}")
    public void update(@PathVariable Long id) {
        WarehousePackage pack = warehousePackage.findById(id).get();
        Float mass = 0F;
        System.out.println(pack.getId());
        List<WarehousePosition> positionsList = pack.getPositionsList();
        Vector<WarehousePosition> vector = new Vector<>(positionsList);
        for (WarehousePosition p : vector) {
            mass += p.getMass();
            boolean save = false;
            if (!p.getMark().equals(pack.getMark())) {
                p.setMark(pack.getMark());
                save = true;
            }
            if (!p.getDiameter().equals(pack.getDiameter())) {
                p.setDiameter(pack.getDiameter());
                save = true;
            }
            if (!p.getPlav().equals(pack.getPlav())) {
                p.setPlav(pack.getPlav());
                save = true;
            }
            if (!p.getPart().equals(pack.getPart())) {
                p.setPart(pack.getPart());
                save = true;
            }
            if (!p.getPacking().equals(pack.getPacking())) {
                p.setPacking(pack.getPacking());
                save = true;
            }
            if (!p.getManufacturer().equals(pack.getManufacturer())) {
                p.setManufacturer(pack.getManufacturer());
                save = true;
            }
            if (save) {
                warehouse.save(p);
            }
        }
        pack.setMass(mass);
    }
}
