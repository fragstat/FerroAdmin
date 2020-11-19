package arsenal.metiz.AresenalMetiz.service.historyservice;

import arsenal.metiz.AresenalMetiz.assets.HistoryView;

import java.util.List;

public interface HistoryService {

    List<HistoryView> getHistoryView();

    HistoryView getHistoryById(Long id);

}
