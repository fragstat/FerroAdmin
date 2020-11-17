package arsenal.metiz.AresenalMetiz.service.addservice;

import arsenal.metiz.AresenalMetiz.assets.VerifyView;
import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface AddService {

    Long addPosition(String mark, String diameter, String packing, String mass, String comment, String plav,
                                  String part, String manufacturer);

    Map<String, List<Long>> doMultiInput(List<WarehouseAddPosition> in);

    boolean verify(VerifyView view);

    void update(Long id);
}
