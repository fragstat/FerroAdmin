package arsenal.metiz.AresenalMetiz.service.departureservice;

import arsenal.metiz.AresenalMetiz.assets.IdToPrint;
import arsenal.metiz.AresenalMetiz.models.MDeparture;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import java.util.Set;

public interface DepartureService {

    double countWeight(String query, String except);

    Set<WarehousePosition> doMultipleDeparture(String request, String except);

    IdToPrint confirmDeparture(MDeparture departure);
}
