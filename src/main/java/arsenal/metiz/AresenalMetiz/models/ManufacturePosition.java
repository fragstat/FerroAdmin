package arsenal.metiz.AresenalMetiz.models;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private ManufacturePackage pack;
}
