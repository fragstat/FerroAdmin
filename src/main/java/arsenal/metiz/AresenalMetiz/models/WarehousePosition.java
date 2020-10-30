package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class WarehousePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long createdFrom;

    @NonNull
    private String mark, diameter, packing, date;

    private String comment;

    @NonNull
    private String part, plav, manufacturer;

    private Float mass;

    private PositionStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private WarehousePackage pack;

    public WarehousePosition() {
    }

    public WarehousePosition(String mark, String diameter, String packing, String comment, String part, String plav,
                             Float mass, String manufacturer, PositionStatus status) {
        this.mark = mark;
        this.diameter = diameter;
        this.packing = packing;
        this.date = DateFormat.getInstance().format(new Date()).trim();
        this.comment = comment;
        this.part = part;
        this.plav = plav;
        this.mass = mass;
        this.createdFrom = -1;
        this.manufacturer = manufacturer.trim().toUpperCase();
        this.status = status;
    }

    public WarehousePosition(String mark, String diameter, String packing, String comment, String part, String plav,
                             Float mass, long createdFrom, String manufacturer, PositionStatus status) {
        this.mark = mark;
        this.diameter = diameter;
        this.packing = packing;
        this.date = DateFormat.getInstance().format(new Date()).trim();
        this.comment = comment;
        this.part = part;
        this.plav = plav;
        this.mass = mass;
        this.createdFrom = createdFrom;
        this.manufacturer = manufacturer;
        this.status = status;
    }

    public List<String> getAll() {
        List<String> info = new ArrayList<>();
        info.add(mark);
        info.add(diameter);
        info.add(packing);
        info.add(part);
        info.add(plav);
        info.add(manufacturer);
        return info;
    }
}
