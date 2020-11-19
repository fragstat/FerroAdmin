package arsenal.metiz.AresenalMetiz.service.historyservice;

import arsenal.metiz.AresenalMetiz.assets.HistoryView;
import arsenal.metiz.AresenalMetiz.models.DepartureOperation;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.DepartureOperationRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HistoryServiceImpl implements HistoryService {

    final DepartureOperationRepo departureOperationDao;
    final WarehouseRepo dao;

    @Autowired
    public HistoryServiceImpl(DepartureOperationRepo departureOperationDao, WarehouseRepo dao) {
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
        WarehousePosition position = dao.findById(id).get();
        return map(departureOperationDao.findByPositionsContains(position));
    }

    private HistoryView map(DepartureOperation operation) {
        return new HistoryView(operation.getOperation_id(), operation.getBill(), operation.getCustomer(),
                operation.getDate(), operation.getWeight());
    }
}
