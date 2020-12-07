package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WarehouseRepo extends CrudRepository<WarehousePosition, Long> {

    List<WarehousePosition> findAllByPlav(String plav);

    List<WarehousePosition> findByPlavContains(String plav);

}
