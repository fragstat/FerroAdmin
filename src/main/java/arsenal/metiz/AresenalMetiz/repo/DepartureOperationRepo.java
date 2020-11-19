package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.DepartureOperation;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import org.springframework.data.repository.CrudRepository;

public interface DepartureOperationRepo extends CrudRepository<DepartureOperation, Long> {

    DepartureOperation findByPositionsContains(WarehousePosition position);

}
