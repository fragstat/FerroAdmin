package arsenal.metiz.AresenalMetiz.service.departureservice;

import arsenal.metiz.AresenalMetiz.assets.DocCreating;
import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.assets.MassException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.controllers.APIController;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.DepartureActionRepo;
import arsenal.metiz.AresenalMetiz.repo.DepartureOperationRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static arsenal.metiz.AresenalMetiz.controllers.APIController.decodeEAN;
import static arsenal.metiz.AresenalMetiz.controllers.APIController.round;

@Service
public class DepartureServiceImpl implements DepartureService{

    final WarehouseRepo warehouse;

    final DepartureActionRepo departure;

    final DepartureOperationRepo departureOperationRepo;

    final WarehousePackageRepo warehousePackage;

    Iterable<WarehousePosition> allPositions;

    @Autowired
    public DepartureServiceImpl(WarehouseRepo warehouse, DepartureActionRepo departure,
                                DepartureOperationRepo departureOperationRepo, WarehousePackageRepo warehousePackage) {
        this.warehouse = warehouse;
        this.departure = departure;
        this.departureOperationRepo = departureOperationRepo;
        this.warehousePackage = warehousePackage;
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

    @Override
    public Set<WarehousePosition> doMultipleDeparture(String request, String except) {
        String[] positions = request.split(",");
        Set<WarehousePosition> set = new HashSet<>();
        List<String> excepted = Arrays.asList(except.split(","));
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
        updatePositions();
        return set;
    }

    @Override
    public IdToPrint confirmDeparture(MDeparture departure) {
        List<SimpleDepartureObj> data = departure.getData();
        Integer amount = data.size();
        String username = "not available";
        String contrAgent = departure.getContrAgent();
        List<Long> listToPrint = new ArrayList<>();
        String account = departure.getAccount();
        ArrayList<WarehousePosition> listToDoc = new ArrayList<>();
        for (SimpleDepartureObj o : data) {
            try {
                WarehousePosition afterDeparture = departure(o.getId(), o.getWeight());
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
        var operation = operationByPositions(contrAgent, listToDoc, account, username);
        updatePositions();
        /*
        TODO отображение общих весов в документе
         */
        return new IdToPrint(listToPrint, DocCreating.createDoc(listToDoc, operation.getOperation_id()));
    }

    public WarehousePosition departure(long id, float weight) throws MassException {
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
            return a;
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

            return newPosition;
        } else {
            throw new MassException();
        }

    }

    private DepartureOperation operationByPositions(String contrAgent, ArrayList<WarehousePosition> newPosition, String bill,
                                      String username) {
        updatePositions();
        DepartureOperation operation = new DepartureOperation(bill, contrAgent, username, newPosition);
        departureOperationRepo.save(operation);
        return operation;
    }

    private void removeFromPackage(WarehousePosition p) {
        WarehousePackage pack = p.getPack();
        if (pack != null) {
            pack.remove(p);
            if (pack.getMass() < 0.5) {
                pack.setStatus(PositionStatus.Departured);
            }
        }
    }

}
