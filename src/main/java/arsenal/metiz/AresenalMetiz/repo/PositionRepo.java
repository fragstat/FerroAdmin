package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.assets.PositionLocation;
import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.models.Position;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PositionRepo extends CrudRepository<Position, Long> {

    List<Position> findAllByPlavAndLocationAndStatus(String plav, PositionLocation location, PositionStatus status);

    List<Position> findPositionsByStatus(PositionStatus status);

    List<Position> findPositionsByLocation(PositionLocation location);

    List<Position> findPositionsByStatusAndLocation(PositionStatus status, PositionLocation location);

    List<Position> findByPlavContains(String plav);

    Boolean existsByIdAndLocation(Long id, PositionLocation location);

}
