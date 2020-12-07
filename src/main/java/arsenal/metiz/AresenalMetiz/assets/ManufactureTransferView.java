package arsenal.metiz.AresenalMetiz.assets;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ManufactureTransferView {

    public String destination;

    public String carPlate;

    public String billNumber;

    public List<Long> positions;

}
