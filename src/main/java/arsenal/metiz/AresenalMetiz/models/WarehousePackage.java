package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionDataException;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class WarehousePackage {
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
    private List<WarehousePosition> positionsList;

    public void attach(WarehousePosition position) throws PositionDataException {
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
        mass += position.getMass();
        position.setPack(this);
    }

    @Transactional(rollbackFor = PositionDataException.class)
    public void attachAll(List<WarehousePosition> positions) {
        positions.forEach(this::attach);
    }

    public void remove(WarehousePosition position) {
        positionsList.remove(position);
        position.setPack(null);
        mass -= position.getMass();
    }

    public void removeFromList(List<WarehousePosition> positions) {
        positions.forEach(this::remove);
    }

    public void removeAll() {
        positionsList.forEach(this::remove);
    }
}
