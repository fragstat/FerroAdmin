package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.assets.PositionLocation;
import arsenal.metiz.AresenalMetiz.models.Package;
import org.springframework.data.repository.CrudRepository;

public interface PackageRepo extends CrudRepository<Package, Long> {

    Boolean existsByIdAndLocation(Long id, PositionLocation location);

}
