package arsenal.metiz.AresenalMetiz.service.historyservice;

import arsenal.metiz.AresenalMetiz.assets.HistoryView;
import arsenal.metiz.AresenalMetiz.models.DepartureOperation;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.DepartureOperationRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HistoryServiceImpl implements HistoryService {

    final DepartureOperationRepo departureOperationDao;
    final WarehouseDao dao;

    @Autowired
    public HistoryServiceImpl(DepartureOperationRepo departureOperationDao, WarehouseDao dao) {
        this.departureOperationDao = departureOperationDao;
        this.dao = dao;
    }


    @Override
    public List<HistoryView> getHistoryView() {
        var allDepartures = departureOperationDao.findAll();
        return StreamSupport.stream(allDepartures.spliterator(), false)
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public HistoryView getHistoryById(Long id) {
        WarehousePosition position = dao.getById(id).get();
        return map(departureOperationDao.findByPositionsContains(position));
    }

    @Override
    public HistoryView getHistoryByDepartureId(Long id) {
        return map(departureOperationDao.findById(id).get());
    }

    private HistoryView map(DepartureOperation operation) {
        return new HistoryView(operation.getOperation_id(), operation.getBill(), operation.getCustomer(),
                operation.getDate(), operation.getWeight(), operation.getPositions());
    }
}
