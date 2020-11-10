package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.DocCreating;
import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.assets.MassException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.DepartureActionRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static arsenal.metiz.AresenalMetiz.controllers.APIController.*;

@RestController
public class DepartureController {

    @Autowired
    WarehouseRepo warehouse;

    @Autowired
    DepartureActionRepo departure;

    Iterable<WarehousePosition> allPositions;

    public void updateTags() {
        allPositions = warehouse.findAll();
    }

    @GetMapping("/api/departure")
    public double countWeight(@RequestParam String query, @RequestParam(required = false) String except) {
        double weight;
        String[] positions = query.split(",");
        updateTags();
        Set<WarehousePosition> set = new HashSet<>();
        List<String> excepted = (Arrays.asList(except.split(",")));
        try {
            Arrays.stream(positions).filter(p -> !excepted.contains(p)).forEachOrdered(p -> {
                long id = decodeEAN(p.trim());
                if (warehouse.findById(id).isPresent()) {
                    set.add(warehouse.findById(id).get());
                } else if (warehousePackage.findById(id).isPresent()) {
                    set.addAll(warehousePackage.findById(id).get().getPositionsList());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        set.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        System.out.println(set);
        weight = set.stream().mapToDouble(WarehousePosition::getMass).sum();
        return APIController.round((float) weight, 2);
    }

    @PostMapping("/api/departure")
    public Set<WarehousePosition> doMultipleDeparture(@RequestParam String request,
                                                      @RequestParam(required = false) String except) {
        String[] positions = request.split(",");
        Set<WarehousePosition> set = new HashSet<>();
        List<String> excepted = (Arrays.asList(except.split(",")));
        try {
            Arrays.stream(positions).filter(p -> !excepted.contains(p)).forEachOrdered(p -> {
                long id = decodeEAN(p.trim());
                if (warehouse.findById(id).isPresent()) {
                    set.add(warehouse.findById(id).get());
                } else if (warehousePackage.findById(id).isPresent()) {
                    set.addAll(warehousePackage.findById(id).get().getPositionsList());
                }
            });
        } catch (Exception ignored) {

        }
        set.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        updateTags();
        return set;
    }

    @PostMapping("/api/departureConfirmation")
    public @ResponseBody
    IdToPrint confirmDeparture(@RequestBody MDeparture departure, Authentication auth) {
        var data = departure.getData();
        String contrAgent = departure.getContrAgent();
        List<Long> id = new ArrayList<>();
        long account = departure.getAccount();
        ArrayList<WarehousePosition> list = new ArrayList<>();
        for (SimpleDepartureObj o : data) {
            try {
                System.out.println(o.getId() + " " + o.getWeight());
                WarehousePosition afterDeparture = departure(o.getId(), o.getWeight(), contrAgent, account, auth.getName());
                if (afterDeparture != null) {
                    list.add(afterDeparture);
                    if (o.getId() != afterDeparture.getId()) {
                        id.add(afterDeparture.getId());
                    }
                }
            } catch (MassException e) {
                e.printStackTrace();
                return null;
            }
        }
        updateTags();
        return new IdToPrint(id, DocCreating.createDoc(list));
    }

    @PostMapping("api/position/departure")
    public ResponseEntity<Long> doDeparture(@RequestParam Long id, @RequestParam Float weight, @RequestParam String contrAgent,
                                            @RequestParam(required = false) Long account, Authentication auth) {
        WarehousePosition result = departure(id, weight, contrAgent, account, auth.getName());
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result.getId());
    }

    public WarehousePosition departure(long id, float weight, String contrAgent, long l, String account) throws MassException {
        Optional<WarehousePosition> optional = warehouse.findById(id);
        WarehousePosition a;
        if (optional.isPresent()) {
            a = optional.get();
            if (!a.getStatus().equals(PositionStatus.In_stock)) {
                return null;
            }
        } else {
            return null;
        }
        if (weight <= a.getMass() && round(a.getMass() - weight, 1) < 0.5) {
            a.setMass(weight);
            a.setStatus(PositionStatus.Departured);
            warehouse.save(a);
            removeFromPackage(a);
            return actionByPosition(id, weight, contrAgent, a, l, account);
        } else if (weight <= a.getMass()) {
            a.setMass(round(a.getMass() - weight, 2));
            a.setStatus(PositionStatus.In_stock);
            warehouse.save(a);
            WarehousePackage pack = a.getPack();
            if (pack != null) {
                pack.setMass(round((float) (pack.getMass() - weight), 2));
            }
            WarehousePosition newPosition = new WarehousePosition(a.getMark(), a.getDiameter(), a.getPacking(),
                    a.getComment(), a.getPart(), a.getPlav(), weight, id, a.getManufacturer(),
                    PositionStatus.Departured);
            warehouse.save(newPosition);
            removeFromPackage(newPosition);

            return actionByPosition(id, weight, contrAgent, newPosition, l, account);
        } else {
            throw new MassException();
        }

    }

    private WarehousePosition actionByPosition(long id, float weight, String contrAgent, WarehousePosition newPosition,
                                               Long account, String name) {
        updateTags();
        DepartureAction action = new DepartureAction();
        action.setCustomer(contrAgent);
        action.setWeight(round(weight, 2));
        action.setSource_id(id);
        action.setDate(Calendar.getInstance().getTime());
        action.setOutput_id(newPosition.getId());
        action.setAccount_number(account);
        action.setAccount_name(name);
        departure.save(action);
        updateTags();
        return newPosition;
    }

    private String getAuthedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return "no info";
    }

    private void removeFromPackage(WarehousePosition p) {
        WarehousePackage pack = p.getPack();
        if (pack != null) {
            pack.remove(p);
        }
    }

}
