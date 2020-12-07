package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManufacturePosition {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private ManufacturePackage pack;

    public ManufacturePosition(@NonNull String mark, @NonNull String diameter, @NonNull String packing, String comment,
                               @NonNull String part, @NonNull String plav, @NonNull String manufacturer, Float mass,
                               PositionStatus status) {
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
}
