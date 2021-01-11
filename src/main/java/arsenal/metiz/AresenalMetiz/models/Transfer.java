package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    String departureDate;
    String arrivalDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Position> positions;


    public Transfer(String destination, String carPlate, String billNumber, List<Position> positions) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.destination = destination;
        this.carPlate = carPlate;
        this.billNumber = billNumber;
        this.positions = positions;
        this.departureDate = sdf.format(Calendar.getInstance().getTime());
    }

    public void setArrivalDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.arrivalDate = sdf.format(Calendar.getInstance().getTime());
    }
}
