package arsenal.metiz.AresenalMetiz.service.manufactureservice;

import arsenal.metiz.AresenalMetiz.assets.ManufactureTransferView;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.models.*;
import arsenal.metiz.AresenalMetiz.repo.*;
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

    @Autowired
    public ManufactureServiceImpl(TransferRepo transferRepo, WarehouseRepo warehouseRepo,
                                  WarehousePackageRepo warehousePackageRepo, ManufacturePositionRepo manufacturePositionRepo,
                                  ManufacturePackageRepo manufacturePackageRepo) {
        this.transferRepo = transferRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehousePackageRepo = warehousePackageRepo;
        this.manufacturePositionRepo = manufacturePositionRepo;
        this.manufacturePackageRepo = manufacturePackageRepo;
    }

    @Override
    public Map<String, List<Long>> doMultipleAdd(List<WarehouseAddPosition> list) {
        return null;
    }

    @Override
    public void transfer(ManufactureTransferView transferView) {
        writeTransfer(transferView);
        transferPositions(transferView);
    }

    private void transferPositions(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        List<ManufacturePosition> positions = new ArrayList<>();
        positionsId.forEach(p -> positions.add(manufacturePositionRepo.findById(p).get()));
        map(positions);
    }

    private void writeTransfer(ManufactureTransferView transferView) {
        List<Long> positionsId = transferView.getPositions();
        List<ManufacturePosition> positions = new ArrayList<>();
        positionsId.forEach(p -> positions.add(manufacturePositionRepo.findById(p).get()));
        Transfer transfer = new Transfer(transferView.destination, transferView.carPlate, transferView.billNumber, positions);
        transferRepo.save(transfer);
    }

    private void map(List<ManufacturePosition> positions) {
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
                WarehousePosition warehousePosition = new WarehousePosition(position.getId(),
                        position.getCreatedFrom(),
                        position.getMark(), position.getDiameter(), position.getPacking(), position.getDate(),
                        position.getComment(), position.getPart(), position.getPlav(), position.getManufacturer(),
                        position.getMass(), PositionStatus.Arriving, null);
                warehousePositions.add(warehousePosition);
            }
            WarehousePackage warehousePackage = new WarehousePackage();
            warehousePackage.attachAll(warehousePositions);
            warehousePackage.setId(manufacturePackage.getId());
            warehousePackages.add(warehousePackage);
        }
        warehousePackageRepo.saveAll(warehousePackages);
    }
}
