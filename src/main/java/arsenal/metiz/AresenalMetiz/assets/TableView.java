package arsenal.metiz.AresenalMetiz.assets;

import arsenal.metiz.AresenalMetiz.models.ManufacturePosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Data
public class TableView implements Comparable<TableView> {
    private String mark;
    private float diameter;
    private String packing;
    private double mass;

    public TableView(WarehousePosition position) {
        this.mark = position.getMark();
        this.diameter = Float.parseFloat(position.getDiameter().replaceAll(",", "."));
        this.packing = position.getPacking();
        this.mass = position.getMass();
    }

    public TableView(ManufacturePosition position) {
        this.mark = position.getMark();
        this.diameter = Float.parseFloat(position.getDiameter().replaceAll(",", "."));
        this.packing = position.getPacking();
        this.mass = position.getMass();
    }

    public int compareTo(@NonNull TableView t) {
        String prepareM1, prepareM2;
        if (mark.contains("Св-")) {
            prepareM1 = mark.substring(3);
        } else {
            prepareM1 = mark;
        }
        if (t.getMark().contains("Св-")) {
            prepareM2 = t.getMark().substring(3);
        } else {
            prepareM2 = t.getMark();
        }
        if (!prepareM1.equalsIgnoreCase(prepareM2)) {
            return prepareM1.compareTo(prepareM2);
        } else if (diameter != t.getDiameter()) {
            return Float.compare(diameter, t.getDiameter());
        } else if (!packing.equalsIgnoreCase(t.getPacking())) {
            return packing.compareTo(t.getPacking());
        }
        return 0;
    }
}
