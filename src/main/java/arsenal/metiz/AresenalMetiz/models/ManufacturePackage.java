package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionDataException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ManufacturePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String mark, diameter, packing, date;

    private String comment;

    private String part, plav, manufacturer;

    private PositionStatus status;

    @Column(name = "weight")
    private double mass = 0.0;

    @OneToMany(mappedBy = "pack", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ManufacturePosition> positionsList;

    @Transactional(rollbackFor = PositionDataException.class)
    public void attach(ManufacturePosition position) throws PositionDataException {
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
        } else if (!mark.equals(position.getMark()) || !diameter.equals(position.getDiameter()) ||
                !packing.equals(position.getPacking()) ||
                !part.equals(position.getPart()) || !plav.equals(position.getPlav()) ||
                !manufacturer.equals(position.getManufacturer()) || status != PositionStatus.In_stock ||
                position.getPack() != null) {

            throw new PositionDataException();
        }
        positionsList.add(position);
        this.countWeight();
        position.setPack(this);
    }

    @Transactional(rollbackFor = PositionDataException.class)
    public void attachAll(List<ManufacturePosition> positions) {
        positions.forEach(this::attach);
    }

    public void remove(ManufacturePosition position) {
        positionsList.remove(position);
        position.setPack(null);
        mass -= position.getMass();
        if (mass < 0.2) {
            status = PositionStatus.Departured;
        }
    }

    public void removeFromList(List<ManufacturePosition> positions) {
        positions.forEach(this::remove);
    }

    public void removeAll() {
        positionsList.forEach(this::remove);
    }

    private void countWeight() {
        this.mass = Precision.round(positionsList.stream().mapToDouble(ManufacturePosition::getMass).sum(), 2);
    }

}
