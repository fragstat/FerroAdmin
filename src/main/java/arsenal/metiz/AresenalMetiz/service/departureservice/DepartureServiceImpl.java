package arsenal.metiz.AresenalMetiz.service.departureservice;

import arsenal.metiz.AresenalMetiz.assets.DocCreating;
import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.assets.MassException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.assets.SDeparture;
import arsenal.metiz.AresenalMetiz.models.Package;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.*;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static arsenal.metiz.AresenalMetiz.controllers.APIController.decodeEAN;
import static arsenal.metiz.AresenalMetiz.controllers.APIController.round;

@Service
public class DepartureServiceImpl implements DepartureService {

    final PositionRepo warehouse;

    final WarehouseDao dao;

    final DepartureActionRepo departure;

    final DepartureOperationRepo departureOperationRepo;

    final PackageRepo warehousePackage;

    Iterable<Position> allPositions;

    @Autowired
    public DepartureServiceImpl(PositionRepo warehouse, DepartureActionRepo departure,
                                DepartureOperationRepo departureOperationRepo, PackageRepo warehousePackage, WarehouseDao dao) {
        this.warehouse = warehouse;
        this.departure = departure;
        this.departureOperationRepo = departureOperationRepo;
        this.warehousePackage = warehousePackage;
        this.dao = dao;
    }

    public void updatePositions() {
        allPositions = warehouse.findAll();
    }

    @Override
    public double countWeight(String query, String except) {
        if (except == null) {
            except = "";
        }
        double weight;
        String[] positions = query.split(",");
        updatePositions();
        Set<Position> set = new HashSet<>();
        List<String> excepted = (Arrays.asList(except.split(",")));
        try {
            Arrays.stream(positions).filter(p -> !excepted.contains(p)).forEachOrdered(p -> {
                long id = decodeEAN(p.trim());
                if (warehouse.existsById(id)) {
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
        weight = set.stream().mapToDouble(Position::getMass).sum();
        return Precision.round(weight, 2);
    }

    @Override
    public Set<Position> doMultipleDeparture(String request, String except) {
        String[] positions = request.split(",");
        Set<Position> set = new HashSet<>();
        Set<Position> ex_set = new HashSet<>();
        List<String> excepted = Arrays.asList(except.split(","));
        try {
            Arrays.stream(positions).filter(p -> !excepted.contains(p)).forEachOrdered(p -> {
                long id = decodeEAN(p.trim());
                if (warehouse.existsById(id)) {
                    set.add(warehouse.findById(id).get());
                } else if (warehousePackage.findById(id).isPresent()) {
                    set.addAll(warehousePackage.findById(id).get().getPositionsList());
                }
            });
            excepted.forEach(p -> {
                long id = decodeEAN(p.trim());
                if (warehouse.existsById(id)) {
                    ex_set.add(warehouse.findById(id).get());
                } else if (warehousePackage.findById(id).isPresent()) {
                    ex_set.addAll(warehousePackage.findById(id).get().getPositionsList());
                }
            });
        } catch (Exception ignored) {
        }
        set.removeIf(p -> !p.getStatus().equals(PositionStatus.In_stock));
        set.removeAll(ex_set);
        updatePositions();
        return set;
    }

    @Override
    public IdToPrint confirmDeparture(MDeparture departure) {
        List<SimpleDepartureObj> data = departure.getData();
        String username = "not available";
        String contrAgent = departure.getContrAgent();
        List<Long> listToPrint = new ArrayList<>();
        String account = departure.getAccount();
        ArrayList<Position> listToDoc = new ArrayList<>();
        for (SimpleDepartureObj o : data) {
            try {
                Position afterDeparture = departure(o.getId(), o.getWeight());
                if (afterDeparture != null) {
                    listToDoc.add(afterDeparture);
                    if (o.getId() != afterDeparture.getId()) {
                        listToPrint.add(afterDeparture.getId());
                    }
                }
            } catch (MassException e) {
                e.printStackTrace();
                return null;
            }
        }
        DepartureOperation operation = operationByPositions(contrAgent, listToDoc, account, username);
        updatePositions();
        /*
        TODO отображение общих весов в документе
         */
        return new IdToPrint(listToPrint, DocCreating.createDoc(listToDoc, operation.getOperation_id()));
    }

    @Override
    public ResponseEntity<?> soloDeparture(SDeparture departure) {
        SimpleDepartureObj obj = departure.data;
        ArrayList<Position> listToDoc = new ArrayList<>();
        try {
            Position afterDeparture = departure(obj.getId(), obj.getWeight());
            if (afterDeparture != null) {
                if (obj.getId() != afterDeparture.getId()) {
                    listToDoc.add(afterDeparture);
                }
            }
        } catch (MassException e) {
            e.printStackTrace();
            return null;
        }
        operationByPositions(departure.contrAgent, listToDoc, departure.account,
                "not avaliable");
        updatePositions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public Position departure(long id, float weight) throws MassException {
        Optional<Position> optional = warehouse.findById(id);
        Position a;
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
            dao.update(a);
            removeFromPackage(a);
            return a;
        } else if (weight <= a.getMass()) {
            a.setMass(round(a.getMass() - weight, 2));
            a.setStatus(PositionStatus.In_stock);
            dao.update(a);
            Package pack = a.getPack();
            if (pack != null) {
                pack.setMass(round((float) (pack.getMass() - weight), 2));
            }
            Position newPosition = new Position(a.getMark(), a.getDiameter(), a.getPacking(),
                    a.getComment(), a.getPart(), a.getPlav(), weight, id, a.getManufacturer(),
                    PositionStatus.Departured, a.getLocation());
            warehouse.save(newPosition);
            removeFromPackage(newPosition);

            return newPosition;
        } else {
            throw new MassException();
        }

    }

    private DepartureOperation operationByPositions(String contrAgent, ArrayList<Position> newPosition, String bill,
                                                    String username) {
        updatePositions();
        DepartureOperation operation = new DepartureOperation(bill, contrAgent, username, newPosition);
        departureOperationRepo.save(operation);
        return operation;
    }

    private void removeFromPackage(Position p) {
        Package pack = p.getPack();
        if (pack != null) {
            pack.remove(p);
            if (pack.getMass() < 0.5) {
                pack.setStatus(PositionStatus.Departured);
            }
        }
    }

}
