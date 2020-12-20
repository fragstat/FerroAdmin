package arsenal.metiz.AresenalMetiz.service.manufactureservice;

import arsenal.metiz.AresenalMetiz.assets.DeparturePreProcessRequestView;
import arsenal.metiz.AresenalMetiz.assets.DeparturePreProcessResponseView;
import arsenal.metiz.AresenalMetiz.assets.ManufactureTransferView;
import arsenal.metiz.AresenalMetiz.models.ManufacturePosition;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;

import java.util.List;
import java.util.Map;

public interface ManufactureService {

    Map<String, List<Long>> doMultipleAdd(List<WarehouseAddPosition> list);

    Long transfer(ManufactureTransferView transferView);

    ManufacturePosition getById(Long id);

    DeparturePreProcessResponseView departurePreProcess(DeparturePreProcessRequestView view);
}
