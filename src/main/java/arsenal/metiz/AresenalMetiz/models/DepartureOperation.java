package arsenal.metiz.AresenalMetiz.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class DepartureOperation {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operation_id;

    private String bill;

    private float weight;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Position> positions;

    private String customer;

    private String username;

    private String date;

    public DepartureOperation(String bill, String customer, String username, List<Position> positions) {
        this.bill = bill;
        this.customer = customer;
        this.username = username;
        this.positions = positions;
        this.date = dateFormat.format(Calendar.getInstance().getTime());
        this.weight = (float) positions.stream().mapToDouble(Position::getMass).sum();
    }
}


