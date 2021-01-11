package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionDataException;
import arsenal.metiz.AresenalMetiz.assets.PositionLocation;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String mark, diameter, packing, date;

    private String comment;

    private String part, plav, manufacturer;

    private PositionStatus status;

    private PositionLocation location;

    @Column(name = "weight")
    private double mass = 0.0;

    @OneToMany(mappedBy = "pack", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Position> positionsList;

    @Transactional(rollbackFor = PositionDataException.class)
    public void attach(Position position) throws PositionDataException {
        if (mark == null && position.getPack() == null) {
            positionsList = new ArrayList<>();
            mark = position.getMark();
            diameter = position.getDiameter();
            packing = position.getPacking();
            date = position.getDate();
            comment = position.getComment();
            part = position.getPart();
            plav = position.getPlav();
            manufacturer = position.getManufacturer();
            status = PositionStatus.In_stock;
            location = position.getLocation();
        } else if (!mark.equals(position.getMark()) || !diameter.equals(position.getDiameter()) ||
                !packing.equals(position.getPacking()) ||
                !part.equals(position.getPart()) || !plav.equals(position.getPlav()) ||
                !manufacturer.equals(position.getManufacturer()) || status != PositionStatus.In_stock ||
                position.getPack() != null || location != position.getLocation()) {

            throw new PositionDataException();
        }
        positionsList.add(position);
        mass += position.getMass();
        position.setPack(this);
    }

    @Transactional(rollbackFor = PositionDataException.class)
    public void attachAll(List<Position> positions) {
        positions.forEach(this::attach);
    }

    @Transactional
    public void remove(Position position) {
        positionsList.remove(position);
        position.setPack(null);
        mass -= position.getMass();
        if (mass < 0.2) {
            status = PositionStatus.Departured;
        }
    }

    public void removeFromList(List<Position> positions) {
        positions.forEach(this::remove);
    }

    public void removeAll() {
        positionsList.forEach(this::remove);
    }

    public void countWeight() {
        this.mass = positionsList.stream().mapToDouble(Position::getMass).sum();
    }

    public boolean verify(List<WarehouseAddPosition> positions) {
        Package pkg = new Package();
        for (WarehouseAddPosition position : positions) {
            if (pkg.mark == null) {
                pkg.positionsList = new ArrayList<>();
                pkg.mark = position.getMark();
                pkg.diameter = position.getDiameter();
                pkg.packing = position.getPacking();
                pkg.part = position.getPart();
                pkg.plav = position.getPlav();
                pkg.manufacturer = position.getManufacturer();
                pkg.status = PositionStatus.In_stock;
            } else if (!pkg.mark.equals(position.getMark()) || !pkg.diameter.equals(position.getDiameter()) ||
                    !pkg.packing.equals(position.getPacking()) ||
                    !pkg.part.equals(position.getPart()) || !pkg.plav.equals(position.getPlav()) ||
                    !pkg.manufacturer.equals(position.getManufacturer()) || pkg.status != PositionStatus.In_stock) {
                return false;
            }
        }
        return true;
    }


}
