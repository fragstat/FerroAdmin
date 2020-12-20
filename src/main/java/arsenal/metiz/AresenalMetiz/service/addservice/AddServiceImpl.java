package arsenal.metiz.AresenalMetiz.service.addservice;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.assets.VerifyView;
import arsenal.metiz.AresenalMetiz.assets.WarehouseEditPosition;
import arsenal.metiz.AresenalMetiz.controllers.APIController;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.IDContainerRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AddServiceImpl implements AddService {

    final
    IDContainerRepo idContainer;

    final
    WarehouseDao dao;

    final
    WarehouseRepo warehouse;

    final
    WarehousePackageRepo warehousePackage;

    public AddServiceImpl(IDContainerRepo idContainer, WarehouseRepo warehouse, WarehousePackageRepo warehousePackage,
                          WarehouseDao dao) {
        this.idContainer = idContainer;
        this.warehouse = warehouse;
        this.warehousePackage = warehousePackage;
        this.dao = dao;
    }

    @Override
    public Long addPosition(String mark, String diameter, String packing, String mass, String comment, String plav,
                            String part, String manufacturer) {
        if (mark.isEmpty() || diameter.isEmpty() || packing.isEmpty() || mass.isEmpty()) {
            return null;
        }
        String massF = mass.replaceAll(",", ".");
        WarehousePosition warehousePosition = new WarehousePosition(mark.trim().replaceAll("СВ", "Св"),
                diameter.replaceAll(",", "."), packing, comment, part, plav,
                Float.valueOf(massF), manufacturer, PositionStatus.In_stock);
        dao.save(warehousePosition);
        APIController.updateTags();
        return warehousePosition.getId();
    }

    @Override
    public Map<String, List<Long>> doMultiInput(List<WarehouseAddPosition> in) {
        List<Long> ids = new ArrayList<>();
        List<Long> packs = new ArrayList<>();
        List<WarehousePosition> list = new ArrayList<>();
        Map<String, List<Long>> id = new HashMap<>();
        WarehousePackage packages = new WarehousePackage();
        if (packages.verify(in)) {
            for (WarehouseAddPosition p : in) {
                WarehousePosition warehousePosition = new WarehousePosition(p.getMark(), p.getDiameter().replaceAll(",", "."), p.getPacking(),
                        p.getComment(), p.getPart(), p.getPlav(),
                        p.getMass(), p.getManufacturer(), PositionStatus.In_stock);
                warehousePosition = dao.save(warehousePosition);
                System.out.println(warehousePosition.getId());
                list.add(warehousePosition);
                ids.add(warehousePosition.getId());
            }
            try {
                packages.attachAll(list);
                warehousePackage.save(packages);
                packs.add(packages.getId());
                id.put("package", packs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            id.put("id", ids);

            return id;
        } else {
            return null;
        }
    }

    @Override
    public boolean verify(VerifyView view) {
        return dao.exists(view.mark, view.diameter, view.part, view.plav);
    }

    @Override
    public void update(Long id) {
        WarehousePackage pack = warehousePackage.findById(id).get();
        AtomicReference<Float> mass = new AtomicReference<>(0F);
        System.out.println(pack.getId());
        List<WarehousePosition> positionsList = pack.getPositionsList();
        ArrayList<WarehousePosition> positions = new ArrayList<>(positionsList);
        positions.forEach(p -> {
            mass.updateAndGet(v -> v + p.getMass());
            boolean save = false;
            if (!p.getMark().equals(pack.getMark())) {
                p.setMark(pack.getMark());
                save = true;
            }
            if (!p.getDiameter().equals(pack.getDiameter())) {
                p.setDiameter(pack.getDiameter());
                save = true;
            }
            if (!p.getPlav().equals(pack.getPlav())) {
                p.setPlav(pack.getPlav());
                save = true;
            }
            if (!p.getPart().equals(pack.getPart())) {
                p.setPart(pack.getPart());
                save = true;
            }
            if (!p.getPacking().equals(pack.getPacking())) {
                p.setPacking(pack.getPacking());
                save = true;
            }
            if (!p.getManufacturer().equals(pack.getManufacturer())) {
                p.setManufacturer(pack.getManufacturer());
                save = true;
            }
            if (save) {
                dao.update(p);
            }
        });
        pack.setMass(mass.get());
    }

    @Override
    public void edit(WarehouseEditPosition edited) {
        if (dao.getById(edited.getId()).isPresent()) {
            WarehousePosition position = dao.getById(edited.getId()).get();
            position.setMark(edited.getMark());
            position.setDiameter(edited.getDiameter());
            position.setPacking(edited.getPacking());
            position.setPart(edited.getPart());
            position.setPlav(edited.getPlav());
            position.setManufacturer(edited.getManufacturer());
            position.setMass(edited.getMass());
            position.setComment(edited.getComment());
            dao.save(position);
            WarehousePackage warehousePack = position.getPack();
            warehousePack.countWeight();
            warehousePackage.save(warehousePack);
        } else if (warehousePackage.findById(edited.getId()).isPresent()) {
            WarehousePackage pack = warehousePackage.findById(edited.getId()).get();
            pack.setMark(edited.getMark());
            pack.setDiameter(edited.getDiameter());
            pack.setPacking(edited.getPacking());
            pack.setPart(edited.getPart());
            pack.setPlav(edited.getPlav());
            pack.setManufacturer(edited.getManufacturer());
            pack.setMass(edited.getMass());
            pack.setComment(edited.getComment());
            pack.countWeight();
            warehousePackage.save(pack);
            update(pack.getId());
        }
    }

    @Override
    public void updateWeight(Long id) {
        WarehousePackage packages = warehousePackage.findById(id).get();
        List<WarehousePosition> positions = packages.getPositionsList();
        double mass = positions.stream().mapToDouble(WarehousePosition::getMass).sum();
        packages.setMass(Precision.round(mass, 2));
    }

    @Override
    public void fixWeights() {
        Iterable<WarehousePackage> packages = warehousePackage.findAll();
        packages.forEach(p -> updateWeight(p.getId()));
        warehousePackage.saveAll(packages);
    }
}
