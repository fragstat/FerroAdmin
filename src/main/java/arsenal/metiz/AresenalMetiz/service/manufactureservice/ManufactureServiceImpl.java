package arsenal.metiz.AresenalMetiz.service.manufactureservice;

import arsenal.metiz.AresenalMetiz.assets.*;
import arsenal.metiz.AresenalMetiz.controllers.APIController;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.*;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManufactureServiceImpl implements ManufactureService {

    final TransferRepo transferRepo;

    final WarehouseRepo warehouseRepo;

    final WarehousePackageRepo warehousePackageRepo;

    final ManufacturePositionRepo manufacturePositionRepo;

    final ManufacturePackageRepo manufacturePackageRepo;

    final WarehouseDao warehouseDao;

    final IDContainerRepo idContainerRepo;

    @Autowired
    public ManufactureServiceImpl(TransferRepo transferRepo, WarehouseRepo warehouseRepo,
                                  WarehousePackageRepo warehousePackageRepo, ManufacturePositionRepo manufacturePositionRepo,
                                  ManufacturePackageRepo manufacturePackageRepo, WarehouseDao warehouseDao, IDContainerRepo idContainerRepo) {
        this.transferRepo = transferRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehousePackageRepo = warehousePackageRepo;
        this.manufacturePositionRepo = manufacturePositionRepo;
        this.manufacturePackageRepo = manufacturePackageRepo;
        this.warehouseDao = warehouseDao;
        this.idContainerRepo = idContainerRepo;
    }

    @Override
    public Map<String, List<Long>> doMultipleAdd(List<WarehouseAddPosition> in) {
        List<Long> ids = new ArrayList<>();
        List<Long> packs = new ArrayList<>();
        List<ManufacturePosition> list = new ArrayList<>();
        Map<String, List<Long>> id = new HashMap<>();
        ManufacturePackage packages = new ManufacturePackage();
        id.put("package", null);
        for (WarehouseAddPosition p : in) {
            ManufacturePosition position = mapToManufacturePositionWhileCreating(p);
            manufacturePositionRepo.save(position);
            list.add(position);
            ids.add(position.getId());
        }
        if (verify(in)) {
            try {
                packages.attachAll(list);
                manufacturePackageRepo.save(packages);
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
    public Long transfer(ManufactureTransferView transferView) {
        Long id = writeTransfer(transferView);
        transferPositions(transferView);
        return id;
    }

    @Override
    public ManufacturePosition getById(Long id) {
        return manufacturePositionRepo.findById(id).orElse(null);
    }

    @Override
    public DeparturePreProcessResponseView departurePreProcess(DeparturePreProcessRequestView view) {
        List<Long> ids = Arrays.stream(view.request.split(","))
                .map(String::trim)
                .map(APIController::decodeEAN)
                .collect(Collectors.toList());
        List<ManufacturePosition> positions = collectDataFromDB(ids);
        return new DeparturePreProcessResponseView(toTableViews(positions), ids);
    }

    private List<ManufacturePosition> collectDataFromDB(List<Long> ids) {
        List<ManufacturePosition> positions = new ArrayList<>();
        for (Long id : ids) {
            if (manufacturePositionRepo.existsById(id)) {
                positions.add(manufacturePositionRepo.findById(id).get());
            } else if (manufacturePackageRepo.existsById(id)) {
                positions.addAll(manufacturePackageRepo.findById(id).get().getPositionsList());
            }
        }
        return positions;
    }

    private void transferPositions(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        List<ManufacturePosition> positions = new ArrayList<>();
        positionsId.forEach(p -> {
            if (manufacturePositionRepo.existsById(p)) {
                positions.add(manufacturePositionRepo.findById(p).get());
            } else if (manufacturePackageRepo.existsById(p)) {
                positions.addAll(manufacturePackageRepo.findById(p).get().getPositionsList());
            }
        });
        mapForTransfer(positions);

    }

    private Long writeTransfer(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        Set<ManufacturePosition> positions = new HashSet<>();
        positionsId.forEach(p -> {
            if (manufacturePositionRepo.existsById(p)) {
                positions.add(manufacturePositionRepo.findById(p).get());
            } else if (manufacturePackageRepo.existsById(p)) {
                positions.addAll(manufacturePackageRepo.findById(p).get().getPositionsList());
            }
        });
        Transfer transfer = new Transfer(transferView.destination, transferView.carPlate, transferView.billNumber, new ArrayList<>(positions));
        transferRepo.save(transfer);
        Double weight = positions.stream().mapToDouble(ManufacturePosition::getMass).sum();
        try {
            DocCreating.writeTransferDocument(transferView, transfer.getId(), weight,
                    toTableViews(new ArrayList<>(positions)));
            return transfer.getId();
        } catch (Exception e) {
            return null;
        }
    }

    private void mapForTransfer(List<ManufacturePosition> positions) {
        Set<ManufacturePackage> packages = new HashSet<>();
        positions.forEach(p -> packages.add(p.getPack()));
        List<ManufacturePackage> newPacks = new ArrayList<>();
        for (ManufacturePackage manufacturePackage : packages) {
            List<ManufacturePosition> positionsFromPackage =
                    positions.stream()
                            .filter(p -> p.getPack().getId() == manufacturePackage.getId())
                            .collect(Collectors.toList());
            ManufacturePackage newManufacturePackage = new ManufacturePackage();
            for (ManufacturePosition position : positionsFromPackage) {
                manufacturePackage.remove(position);
                newManufacturePackage.attach(position);
            }
            if (manufacturePackage.getStatus().equals(PositionStatus.Departured)) {
                newManufacturePackage.setId(manufacturePackage.getId());
            } else {
                manufacturePackageRepo.save(newManufacturePackage);
            }
            newPacks.add(newManufacturePackage);
        }
        changeDatabaseForPackage(newPacks);
    }

    private void changeDatabaseForPackage(List<ManufacturePackage> packages) {
        List<WarehousePackage> warehousePackages = new ArrayList<>();
        for (ManufacturePackage manufacturePackage : packages) {
            List<ManufacturePosition> positions = manufacturePackage.getPositionsList();
            List<WarehousePosition> warehousePositions = new ArrayList<>();
            for (ManufacturePosition position : positions) {
                System.out.println(position.getId());
                WarehousePosition warehousePosition = new WarehousePosition();
                warehousePosition.setId(position.getId());
                warehousePosition.setCreatedFrom(-1);
                warehousePosition.setMark(position.getMark());
                warehousePosition.setDiameter(position.getDiameter());
                warehousePosition.setPacking(position.getPacking());
                warehousePosition.setDate(position.getDate());
                warehousePosition.setComment(position.getComment());
                warehousePosition.setPart(position.getPart());
                warehousePosition.setPlav(position.getPlav());
                warehousePosition.setManufacturer(position.getManufacturer());
                warehousePosition.setMass(position.getMass());
                warehousePosition.setStatus(PositionStatus.Arriving);

                warehousePositions.add(warehousePosition);
                warehouseDao.save(warehousePosition);
                manufacturePositionRepo.delete(position);

                System.out.println(warehousePosition.getId());
                System.out.println(warehouseRepo.existsById(warehousePosition.getId()));
            }
            WarehousePackage warehousePackage = new WarehousePackage();
            warehousePackage.setId(manufacturePackage.getId());
            warehousePackageRepo.save(warehousePackage);
            warehousePackage.attachAll(warehousePositions);
            warehousePackages.add(warehousePackage);
            warehousePackage.getPositionsList().forEach(p -> System.out.println(p.toString()));
        }

    }

    public boolean verify(List<WarehouseAddPosition> list) {
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

    private ManufacturePosition mapToManufacturePositionWhileCreating(WarehouseAddPosition position) {
        return new ManufacturePosition(position.getMark(), position.getDiameter().replaceAll(",", "."),
                position.getPacking(), position.getComment(), position.getPart(), position.getPlav(),
                position.getManufacturer(), position.getMass(), PositionStatus.In_stock);
    }

    private Set<TableView> toTableViews(List<ManufacturePosition> positions) {
        Set<TableView> tableViews = new TreeSet<>();
        for (ManufacturePosition p : positions) {
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
