package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.PositionDataException;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Controller
public class WarehouseController {

    @Autowired
    WarehouseDao warehouseDao;

    @Autowired
    WarehousePackageRepo warehousePackageRepo;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @GetMapping("/sklad")
    public String getWarehouse() {
        APIController.updateTags();
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
        WarehousePackage pack = new WarehousePackage();
        for (String id : idsList) {
            try {
                pack.attach(warehouseDao.getById(APIController.decodeEAN(id)).get());
            } catch (PositionDataException e) {
                pack.removeAll();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        warehousePackageRepo.save(pack);
        return ResponseEntity.ok(pack.getId());
    }

}
