package arsenal.metiz.AresenalMetiz.models;


import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity
@NoArgsConstructor
public class DepartureOperation {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operation_id;

    private String bill;

    private float weight;

    @OneToMany
    private List<WarehousePosition> positions;

    private String customer;

    private String username;

    private String date;

    public DepartureOperation(long operation_id, String bill, float weight, String customer, String username) {
        this.operation_id = operation_id;
        this.bill = bill;
        this.weight = weight;
        this.customer = customer;
        this.username = username;
        this.date = dateFormat.format(Calendar.getInstance().getTime());
    }
}


