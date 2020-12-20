package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.WarehousePosition;

import java.util.Optional;

public interface WarehouseDao {

    WarehousePosition save(WarehousePosition position);

    Optional<WarehousePosition> getById(Long id);

    void update(WarehousePosition position);

    boolean exists(String mark, String diameter, String part, String plav);

    void saveWithId(WarehousePosition position);
}
