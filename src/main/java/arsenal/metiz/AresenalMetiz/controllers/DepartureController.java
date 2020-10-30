package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.DocCreating;
import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.assets.MassException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.DepartureActionRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public double countWeight(@RequestParam String query) {
        double weight;
        String[] positions = query.split(",");
        updateTags();
        Set<WarehousePosition> set = new HashSet<>();
        try {
            for (String p : positions) {
                long ean = decodeEAN(p);
                System.out.println(ean);
                if (warehouse.findById(ean).isPresent()) {
                    var position = warehouse.findById(ean).get();
                    if (set.contains(position)) {
                        set.remove(position);
                    } else {
                        set.add(warehouse.findById(ean).get());
                    }
                } else if (warehousePackage.findById(ean).isPresent()) {
                    List<WarehousePosition> positionList = warehousePackage.findById(ean).get().getPositionsList();
                    for (WarehousePosition position : positionList) {
                        if (set.contains(position)) {
                            set.remove(position);
                        } else {
                            set.add(position);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        set.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        System.out.println(set);
        weight = set.stream().mapToDouble(WarehousePosition::getMass).sum();
        return APIController.round((float) weight, 2);
    }

    @PostMapping("/api/departure")
    public Set<WarehousePosition> doMultipleDeparture(@RequestParam String request) {
        String[] positions = request.split(",");
        Set<WarehousePosition> set = new HashSet<>();
        try {
            Arrays.stream(positions).forEachOrdered(p -> set.add(warehouse.findById(APIController.decodeEAN(p.trim())).get()));
        } catch (Exception ignored) {

        }
        set.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        updateTags();
        return set;
    }

    @PostMapping("/api/departureConfirmation")
    public @ResponseBody
    IdToPrint confirmDeparture(@RequestBody MDeparture departure) {
        var data = departure.getData();
        String contrAgent = departure.getContrAgent();
        List<Long> id = new ArrayList<>();
        long account = departure.getAccount();
        ArrayList<WarehousePosition> list = new ArrayList<>();
        for (SimpleDepartureObj o : data) {
            try {
                System.out.println(o.getId() + " " + o.getWeight());
                WarehousePosition afterDeparture = departure(o.getId(), o.getWeight(), contrAgent, account);
                list.add(afterDeparture);
                if (o.getId() != afterDeparture.getId()) {
                    id.add(afterDeparture.getId());
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
    public long doDeparture(@RequestParam Long id, @RequestParam Float weight, @RequestParam String contrAgent,
                            @RequestParam long account) {
        WarehousePosition result = departure(id, weight, contrAgent, account);
        return result.getId();
    }

    public WarehousePosition departure(long id, float weight, String contrAgent, long account) throws MassException {
        WarehousePosition a = warehouse.findById(id).get();
        if (weight <= a.getMass() && round(a.getMass() - weight, 1) < 0.5) {
            a.setMass(weight);
            a.setStatus(PositionStatus.Departured);
            warehouse.save(a);
            removeFromPackage(a);
            return actionByPosition(id, weight, contrAgent, a, account);
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

            return actionByPosition(id, weight, contrAgent, newPosition, account);
        } else {
            throw new MassException();
        }

    }

    private WarehousePosition actionByPosition(long id, float weight, String contrAgent, WarehousePosition newPosition,
                                               long account) {
        updateTags();
        DepartureAction action = new DepartureAction();
        action.setCustomer(contrAgent);
        action.setWeight(round(weight, 2));
        action.setSource_id(id);
        action.setDate(Calendar.getInstance().getTime());
        action.setOutput_id(newPosition.getId());
        action.setAccount_number(account);
        action.setAccount_name(getAuthedUserName());
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
