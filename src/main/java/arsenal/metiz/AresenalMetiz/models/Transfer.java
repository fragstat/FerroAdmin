package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String destination;
    String carPlate;
    String billNumber;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ManufacturePosition> positions;

    public Transfer(String destination, String carPlate, String billNumber, List<ManufacturePosition> positions) {
        this.destination = destination;
        this.carPlate = carPlate;
        this.billNumber = billNumber;
        this.positions = positions;
    }
}
