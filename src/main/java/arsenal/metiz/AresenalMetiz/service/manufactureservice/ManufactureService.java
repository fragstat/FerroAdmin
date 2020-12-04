package arsenal.metiz.AresenalMetiz.service.manufactureservice;

import arsenal.metiz.AresenalMetiz.assets.ManufactureTransferView;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;

import java.util.List;
import java.util.Map;

public interface ManufactureService {

    Map<String, List<Long>> doMultipleAdd(List<WarehouseAddPosition> list);

    void transfer(ManufactureTransferView transferView);


}
