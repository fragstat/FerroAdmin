package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseAddPosition {

    private String mark, diameter, packing, comment, part, plav, manufacturer;

    private Float mass;

}
