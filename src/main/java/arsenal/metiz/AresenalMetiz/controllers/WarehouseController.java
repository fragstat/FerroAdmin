package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.PositionDataException;
import arsenal.metiz.AresenalMetiz.assets.PositionLocation;
import arsenal.metiz.AresenalMetiz.models.Package;
import arsenal.metiz.AresenalMetiz.repo.PackageRepo;
import arsenal.metiz.AresenalMetiz.repo.PositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.StreamSupport;

@Controller
public class WarehouseController {

    @Autowired
    PositionRepo warehouseDao;

    @Autowired
    PackageRepo packageRepo;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @GetMapping("/sklad")
    public String getWarehouse() {
        APIController.updateTags();
        Iterable<Package> positionList = packageRepo.findAll();
        StreamSupport.stream(positionList.spliterator(), false).forEach(p -> {
            p.setLocation(PositionLocation.Solnechnogorsk);
            packageRepo.save(p);
        });
        return "registration_example";
    }

    @GetMapping("/manufacture")
    public String getManufacture() {
        return "manufacture";
    }

    @GetMapping("/product")
    public String getWarehousePosition() {
        return "product";
    }

    @GetMapping("/package")
    public String getWarehousePackage() {
        return "package";
    }

    @GetMapping("/shipment_history")
    public String getWarehouseShipmentHistory() {
        return "shipment_history";
    }

    @GetMapping("/api/union")
    public ResponseEntity<Long> unionProducts(@RequestParam String ids) {
        APIController.updateTags();
        String[] idsList = ids.split(",");
        Package pack = new Package();
        for (String id : idsList) {
            try {
                pack.attach(warehouseDao.findById(APIController.decodeEAN(id)).get());
            } catch (PositionDataException e) {
                pack.removeAll();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        packageRepo.save(pack);
        return ResponseEntity.ok(pack.getId());
    }

}
