package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AutoRegisteredCertificate {
    @Id
    private String id;
}
