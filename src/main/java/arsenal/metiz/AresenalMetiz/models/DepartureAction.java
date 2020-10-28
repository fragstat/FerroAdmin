package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartureAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long operation_id;

    private long source_id;
    private long output_id;
    private long account_number;
    private double weight;
    private String customer;
    private String account_name;
    private Date date;
}
