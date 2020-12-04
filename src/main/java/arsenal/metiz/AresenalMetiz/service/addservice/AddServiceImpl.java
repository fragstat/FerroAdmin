package arsenal.metiz.AresenalMetiz.service.addservice;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.assets.VerifyView;
import arsenal.metiz.AresenalMetiz.assets.WarehouseEditPosition;
import arsenal.metiz.AresenalMetiz.controllers.APIController;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddServiceImpl implements AddService{

    @Autowired
    WarehouseDao dao;

    @Autowired
    WarehouseRepo warehouse;

    @Autowired
    WarehousePackageRepo warehousePackage;

    @Override
    public Long addPosition(String mark, String diameter, String packing, String mass, String comment, String plav,
                         String part, String manufacturer) {
        if (mark.isEmpty() || diameter.isEmpty() || packing.isEmpty() || mass.isEmpty()) {
            return null;
        }
        String massF = mass.replaceAll(",", ".");
        WarehousePosition warehousePosition = new WarehousePosition(mark,
                diameter.replaceAll(",", "."), packing, comment, part, plav,
                Float.valueOf(massF), manufacturer, PositionStatus.In_stock);
        warehouse.save(warehousePosition);
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
                warehouse.save(warehousePosition);
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
        Float mass = 0F;
        System.out.println(pack.getId());
        List<WarehousePosition> positionsList = pack.getPositionsList();
        Vector<WarehousePosition> vector = new Vector<>(positionsList);
        for (WarehousePosition p : vector) {
            mass += p.getMass();
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
                warehouse.save(p);
            }
        }
        pack.setMass(mass);
    }

    @Override
    public void edit(WarehouseEditPosition edited) {
        WarehousePosition position = warehouse.findById(edited.getId()).get();
        position.setMark(edited.getMark());
        position.setDiameter(edited.getDiameter());
        position.setPacking(edited.getPacking());
        position.setPart(edited.getPart());
        position.setPlav(edited.getPlav());
        position.setManufacturer(edited.getManufacturer());
        position.setMass(edited.getMass());
        position.setComment(edited.getComment());
        warehouse.save(position);
//        updateWeight(position.getPack().getId());
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
