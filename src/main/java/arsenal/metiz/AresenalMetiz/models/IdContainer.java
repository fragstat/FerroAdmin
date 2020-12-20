package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdContainer {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private WarehousePosition position;

}
