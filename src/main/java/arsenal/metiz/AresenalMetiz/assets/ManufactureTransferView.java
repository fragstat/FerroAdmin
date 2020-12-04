package arsenal.metiz.AresenalMetiz.assets;

import lombok.Data;

import java.util.List;

@Data
public class ManufactureTransferView {

    public Long transferId;

    public String destination;

    public String carPlate;

    public String billNumber;

    public List<Long> positions;

}
