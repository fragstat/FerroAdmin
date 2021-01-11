package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.Position;

public interface WarehouseDao {

    void update(Position position);

    boolean exists(String mark, String diameter, String part, String plav);

    void saveWithId(Position position);
}
