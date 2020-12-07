package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.WarehousePosition;

public interface WarehouseDao {

    boolean exists(String mark, String diameter, String part, String plav);

    void saveWithId(WarehousePosition position);
}
