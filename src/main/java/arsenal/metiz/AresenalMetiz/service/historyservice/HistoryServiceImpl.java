package arsenal.metiz.AresenalMetiz.service.historyservice;

import arsenal.metiz.AresenalMetiz.assets.*;
import arsenal.metiz.AresenalMetiz.models.DepartureOperation;
import arsenal.metiz.AresenalMetiz.models.Position;
import arsenal.metiz.AresenalMetiz.models.Transfer;
import arsenal.metiz.AresenalMetiz.repo.DepartureOperationRepo;
import arsenal.metiz.AresenalMetiz.repo.PositionRepo;
import arsenal.metiz.AresenalMetiz.repo.TransferRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HistoryServiceImpl implements HistoryService {

    final DepartureOperationRepo departureOperationDao;
    final WarehouseDao dao;
    final PositionRepo warehouse;
    final TransferRepo transferRepo;
    final SimpleDateFormat uSdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    public HistoryServiceImpl(DepartureOperationRepo departureOperationDao, WarehouseDao dao, PositionRepo warehouse, TransferRepo transferRepo) {
        this.departureOperationDao = departureOperationDao;
        this.dao = dao;
        this.warehouse = warehouse;
        this.transferRepo = transferRepo;
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
        Position position = warehouse.findById(id).get();
        return map(departureOperationDao.findByPositionsContains(position));
    }

    @Override
    public HistoryView getHistoryByDepartureId(Long id) {
        return map(departureOperationDao.findById(id).get());
    }

    @Override
    public HistoryView getTransferHistoryById(Long id) {
        return null;
    }

    @Override
    public List<HistoryStepView> getAllHistorySteps(Long id) {
        List<HistoryStepView> steps = new ArrayList<>();
        HistoryStepView adding = getAddingHistoryStep(id);
        if (adding != null) {
            steps.add(adding);
            HistoryStepView transferDeparture = getTransferDepartureHistoryStep(id);
            steps.add(transferDeparture);
            HistoryStepView transferArrival = getTransferArrivalStep(id);
            steps.add(transferArrival);
            HistoryStepView departure = getDepartureHistoryStep(id);
            steps.add(departure);
        }
        return steps.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private HistoryStepView getDepartureHistoryStep(Long id) {
        HistoryStepView step = new HistoryStepView();
        step.type = HistoryStepType.Departure;
        Position position = warehouse.findById(id).get();
        if (position.getStatus() == PositionStatus.Departured) {
            DepartureOperation operation = departureOperationDao.findByPositionsContains(position);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                step.date = uSdf.format(sdf.parse(operation.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            step.place = position.getLocation();
        } else {
            return null;
        }
        return step;
    }

    private HistoryStepView getTransferArrivalStep(Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        HistoryStepView step = new HistoryStepView();
        step.type = HistoryStepType.Transfer_Arrival;
        Position position;
        if (warehouse.existsById(id)) {
            position = warehouse.findById(id).get();
        } else {
            return null;
        }
        Transfer transfer;
        if (transferRepo.getTransferByPositionsContains(position).isPresent()) {
            transfer = transferRepo.getTransferByPositionsContains(position).get();
        } else {
            return null;
        }
        if (transfer.getArrivalDate() != null) {
            String date = transfer.getArrivalDate();
            try {
                step.date = uSdf.format(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            String dest = transfer.getDestination();
            if (dest.equals("Солнечногорск")) {
                step.place = PositionLocation.Solnechnogorsk;
            } else {
                step.place = PositionLocation.Manufacture;
            }
        } else {
            return null;
        }
        return step;
    }

    private HistoryStepView getTransferDepartureHistoryStep(Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        HistoryStepView step = new HistoryStepView();
        step.type = HistoryStepType.Transfer_Departure;
        Position position;
        if (warehouse.existsById(id)) {
            position = warehouse.findById(id).get();
        } else {
            return null;
        }
        Transfer transfer;
        if (transferRepo.getTransferByPositionsContains(position).isPresent()) {
            transfer = transferRepo.getTransferByPositionsContains(position).get();
        } else {
            return null;
        }
        String date = transfer.getDepartureDate();
        try {
            step.date = uSdf.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        String dest = transfer.getDestination();
        if (dest.equals("Солнечногорск")) {
            step.place = PositionLocation.Manufacture;
        } else {
            step.place = PositionLocation.Solnechnogorsk;
        }
        return step;
    }

    private HistoryStepView getAddingHistoryStep(Long id) {
        HistoryStepView step = new HistoryStepView();
        step.type = HistoryStepType.Adding;
        if (warehouse.existsById(id)) {
            Position position = warehouse.findById(id).get();
            String date = position.getDate();
            try {
                step.date = uSdf.format(DateFormat.getInstance().parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (transferRepo.getTransferByPositionsContains(position).isPresent()) {
                if (position.getLocation() == PositionLocation.Solnechnogorsk) {
                    step.place = PositionLocation.Manufacture;
                }
                if (position.getLocation() == PositionLocation.Manufacture) {
                    step.place = PositionLocation.Solnechnogorsk;
                }
            } else {
                step.place = position.getLocation();
            }
        } else {
            return null;
        }
        return step;
    }

    private HistoryView map(DepartureOperation operation) {
        return new HistoryView(operation.getOperation_id(), operation.getBill(), operation.getCustomer(),
                operation.getDate(), operation.getWeight(), operation.getPositions());
    }
}
