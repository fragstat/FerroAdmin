package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.Position;
import arsenal.metiz.AresenalMetiz.models.Transfer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransferRepo extends CrudRepository<Transfer, Long> {

    Optional<Transfer> getTransferByPositionsContains(Position position);
}
