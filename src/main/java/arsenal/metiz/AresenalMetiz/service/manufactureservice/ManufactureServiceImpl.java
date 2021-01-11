package arsenal.metiz.AresenalMetiz.service.manufactureservice;

import arsenal.metiz.AresenalMetiz.assets.*;
import arsenal.metiz.AresenalMetiz.controllers.APIController;
import arsenal.metiz.AresenalMetiz.models.Package;
import arsenal.metiz.AresenalMetiz.models.Position;
import arsenal.metiz.AresenalMetiz.models.Transfer;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.repo.PackageRepo;
import arsenal.metiz.AresenalMetiz.repo.PositionRepo;
import arsenal.metiz.AresenalMetiz.repo.TransferRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManufactureServiceImpl implements ManufactureService {

    final TransferRepo transferRepo;

    final PositionRepo positionRepo;

    final PackageRepo packageRepo;

    final WarehouseDao warehouseDao;

    @Autowired
    public ManufactureServiceImpl(TransferRepo transferRepo, PositionRepo positionRepo,
                                  PackageRepo packageRepo, WarehouseDao warehouseDao) {
        this.transferRepo = transferRepo;
        this.positionRepo = positionRepo;
        this.packageRepo = packageRepo;
        this.warehouseDao = warehouseDao;
    }

    public static boolean verify(List<WarehouseAddPosition> list) {
        String mark = null, diameter = null, plav = null, part = null, packing = null, manufacturer = null;
        if (list.size() <= 1) {
            return false;
        }
        for (WarehouseAddPosition position : list) {
            if (mark == null) {
                mark = position.getMark();
                diameter = position.getDiameter();
                plav = position.getPlav();
                part = position.getPart();
                packing = position.getPacking();
                manufacturer = position.getManufacturer();
            } else {
                if (!(mark.equals(position.getMark()) && diameter.equals(position.getDiameter())
                        && plav.equals(position.getPlav()) && part.equals(position.getPart())
                        && packing.equals(position.getPacking()) && manufacturer.equals(position.getManufacturer()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Long transfer(ManufactureTransferView transferView) {
        Long id = writeTransfer(transferView);
        transferPositions(transferView);
        return id;
    }

    @Override
    public Map<String, List<Long>> doMultipleAdd(List<WarehouseAddPosition> in) {
        List<Long> ids = new ArrayList<>();
        List<Long> packs = new ArrayList<>();
        List<Position> list = new ArrayList<>();
        Map<String, List<Long>> id = new HashMap<>();
        Package packages = new Package();
        id.put("package", null);
        for (WarehouseAddPosition p : in) {
            Position position = mapToManufacturePositionWhileCreating(p);
            positionRepo.save(position);
            list.add(position);
            ids.add(position.getId());
        }
        if (verify(in)) {
            try {
                packages.attachAll(list);
                packageRepo.save(packages);
                packs.add(packages.getId());
                id.put("package", packs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        id.put("id", ids);
        return id;

    }

    @Override
    public Position getById(Long id) {
        return positionRepo.findById(id).orElse(null);
    }

    @Override
    public DeparturePreProcessResponseView departurePreProcess(DeparturePreProcessRequestView view) {
        List<Long> ids = Arrays.stream(view.request.split(","))
                .map(String::trim)
                .map(APIController::decodeEAN)
                .collect(Collectors.toList());
        List<Position> positions = collectDataFromDB(ids);
        return new DeparturePreProcessResponseView(toTableViews(positions), ids);
    }

    private List<Position> collectDataFromDB(List<Long> ids) {
        List<Position> positions = new ArrayList<>();
        for (Long id : ids) {
            if (positionRepo.existsById(id)) {
                positions.add(positionRepo.findById(id).get());
            } else if (packageRepo.existsById(id)) {
                positions.addAll(packageRepo.findById(id).get().getPositionsList());
            }
        }
        return positions;
    }

    private void transferPositions(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        List<Position> positions = new ArrayList<>();
        positionsId.forEach(p -> {
            if (positionRepo.existsById(p)) {
                positions.add(positionRepo.findById(p).get());
            } else if (packageRepo.existsById(p)) {
                positions.addAll(packageRepo.findById(p).get().getPositionsList());
            }
        });
        mapForTransfer(positions);

    }

    private Long writeTransfer(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        Set<Position> positions = new HashSet<>();
        positionsId.forEach(p -> {
            if (positionRepo.existsById(p)) {
                positions.add(positionRepo.findById(p).get());
            } else if (packageRepo.existsById(p)) {
                positions.addAll(packageRepo.findById(p).get().getPositionsList());
            }
        });
        Transfer transfer = new Transfer(transferView.destination, transferView.carPlate, transferView.billNumber,
                new ArrayList<>(positions));
        transferRepo.save(transfer);
        Double weight = positions.stream().mapToDouble(Position::getMass).sum();
        try {
            DocCreating.writeTransferDocument(transferView, transfer.getId(), weight,
                    toTableViews(new ArrayList<>(positions)));
            return transfer.getId();
        } catch (Exception e) {
            return null;
        }
    }

    private void mapForTransfer(List<Position> positions) {
        positions.forEach(p -> {
            p.setLocation(PositionLocation.Solnechnogorsk);
            p.setStatus(PositionStatus.Arriving);
            positionRepo.save(p);
        });
    }

    private Position mapToManufacturePositionWhileCreating(WarehouseAddPosition position) {
        return new Position(position.getMark().replaceAll("СВ", "Св"),
                position.getDiameter().replaceAll(",", "."),
                position.getPacking(), position.getComment(), position.getPart(), position.getPlav(),
                position.getMass(), position.getManufacturer(), PositionStatus.In_stock, PositionLocation.Manufacture);
    }

    private Set<TableView> toTableViews(List<Position> positions) {
        Set<TableView> tableViews = new TreeSet<>();
        for (Position p : positions) {
            if (p.getStatus() == PositionStatus.In_stock) {
                TableView tableView = new TableView(p);
                if (tableViews.contains(tableView)) {
                    for (TableView tableViewFromIterator : tableViews) {
                        if (tableViewFromIterator.compareTo(tableView) == 0) {
                            tableViewFromIterator
                                    .setMass(Precision.round((tableView.getMass() + tableViewFromIterator.getMass()), 2));
                        }
                    }
                } else {
                    tableViews.add(tableView);
                }
            }
        }
        tableViews.forEach(p -> p.setMass(Precision.round(p.getMass(), 2)));
        return tableViews;
    }
}
