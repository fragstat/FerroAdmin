package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Controller
public class WarehouseController {

    @Autowired
    WarehouseRepo warehouseRepo;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @GetMapping("/sklad")
    public String getWarehouse() {
        return "registration_example";
    }

    @GetMapping("/product")
    public String getWarehousePosition() {
        return "product";
    }

    @GetMapping("/package")
    public String getWarehousePackage() {
        return "package";
    }


}
