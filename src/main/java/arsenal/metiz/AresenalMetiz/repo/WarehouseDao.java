package arsenal.metiz.AresenalMetiz.repo;

public interface WarehouseDao {

    boolean exists(String mark, String diameter, String part, String plav);
}
